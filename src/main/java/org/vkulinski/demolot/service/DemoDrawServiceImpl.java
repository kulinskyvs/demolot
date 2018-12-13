package org.vkulinski.demolot.service;

import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.vkulinski.demolot.domain.DrawStatus;
import org.vkulinski.demolot.repository.DemoRepository;
import org.vkulinski.demolot.repository.TeamMemberRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.desc;


/**
 * @author M-VKU
 * @version 1.0
 */
@Service
class DemoDrawServiceImpl implements DemoDrawService {

    private final DemoRepository repository;
    private final TeamMemberRepository teamMemberRepository;


    public DemoDrawServiceImpl(DemoRepository repository,
                               TeamMemberRepository teamMemberRepository) {
        this.repository = repository;
        this.teamMemberRepository = teamMemberRepository;
    }


    @Override
    public boolean existsById(long id) {
        return repository.existsById(id);
    }


    @Override
    public Mono<Demo> getOne(long id) {
        return Mono.just(repository.getOne(id));
    }


    @Override
    public Flux<Demo> findAll() {
        return Flux.fromIterable(repository.findAll(Sort.by(desc("id"))));
    }


    @Override
    public Mono<Demo> submit(Demo toBeSubmitted) {
        return Mono.fromSupplier(() -> {
            Demo toBeSaved = Objects.nonNull(toBeSubmitted.getId()) ?
                validateSubmission(toBeSubmitted, foundDemo -> {
                    foundDemo.setTitle(toBeSubmitted.getTitle());
                    foundDemo.setDrawStatus(toBeSubmitted.getDrawStatus());
                    foundDemo.setPlannedDate(toBeSubmitted.getPlannedDate());
                    foundDemo.setSummary(toBeSubmitted.getSummary());
                    foundDemo.setLink(toBeSubmitted.getLink());
                }) :
                toBeSubmitted;

            return repository.save(toBeSaved);
        });
    }


    @Override
    public Mono<Demo> submitTask(Demo demo, DemoTask toBeSubmitted) {
        return Mono.fromSupplier(() -> {
            Demo toBeSaved = validateSubmission(demo,
                validDemo -> {
                    if (Objects.nonNull(toBeSubmitted.getId())) {
                        //update the task
                        DemoTask taskFromDemo = validDemo.getTaskById(toBeSubmitted.getId());
                        taskFromDemo.setKey(toBeSubmitted.getKey());
                        taskFromDemo.setTitle(toBeSubmitted.getTitle());
                        taskFromDemo.setLink(toBeSubmitted.getLink());
                        taskFromDemo.setOwner(toBeSubmitted.getOwner());
                    } else {
                        //add new task
                        validDemo.getTasks().add(toBeSubmitted);
                        toBeSubmitted.setDemo(validDemo);
                    }
                });

            return repository.save(toBeSaved);
        });
    }

    @Override
    public Mono<Demo> deleteTask(Demo demo, long taskId) {
        return Mono.fromSupplier(() -> {
            Demo toBeSaved = validateSubmission(demo,
                validDemo -> {
                    DemoTask demoTask = validDemo.getTaskById(taskId);
                    validDemo.getTasks().remove(demoTask);
                });

            return repository.save(toBeSaved);
        });
    }

    @Override
    public Mono<Void> deleteById(long id) {
        return Mono.fromRunnable(() -> {
            Demo loadedDemo = repository.getOne(id);
            if (DrawStatus.PREPARATION == loadedDemo.getDrawStatus()) {
                repository.deleteById(id);
            } else {
                throw new IllegalStateException(
                    "Unable to delete demo in status '" + loadedDemo.getDrawStatus().getDescription() + "'");
            }
        });
    }


    @Override
    public Mono<Demo> startDraw(long id) {
        return getOne(id).map(demo -> {
            if (demo.getDrawStatus().equals(DrawStatus.PREPARATION)) {
                demo.setDrawStatus(DrawStatus.IN_PROGRESS);
                demo = repository.save(demo);
            }
            return demo;
        });
    }


    @Override
    public Mono<Demo> drawTasks(long id) {
        return getOne(id).map(
            demo -> drawTasks(
                demo,
                demo.getTasks()
                    .stream()
                    .filter(task -> !task.hasWinner())
                    .map(DemoTask::getId)
                    .collect(Collectors.toList())
            )
        );
    }


    @Override
    public Mono<Demo> drawTask(long demoId, long taskId) {
        return getOne(demoId)
            .map(demo -> drawTasks(demo, Collections.singletonList(taskId)));
    }


    @Override
    public Mono<Demo> resetDraw(long demoId) {
        return getOne(demoId)
            .map(demo -> {
                for (DemoTask task : demo.getTasks()) {
                    task.setWinner(null);
                    task.setDrawDateTime(null);
                }
                demo.setDrawStatus(DrawStatus.IN_PROGRESS);
                return repository.save(demo);
            });
    }


    private Demo drawTasks(Demo demo, List<Long> tasksId) {
        LocalDateTime drawTimestamp = LocalDateTime.now();
        DrawWinnerFinder drawWinnerFinder = new DrawWinnerFinder(demo, teamMemberRepository.findActiveOnly());

        if (DrawStatus.IN_PROGRESS == demo.getDrawStatus()) {

            //find the winner for each task
            demo.getTasks()
                .stream()
                .filter(task -> tasksId.contains(task.getId()))
                .forEach(task -> {
                    task.setWinner(drawWinnerFinder.find(task));
                    task.setDrawDateTime(drawTimestamp);
                });

            //reflect the draw status
            recalculateDrawStatus(demo);

            return repository.save(demo);
        } else {
            return demo;
        }
    }


    private Demo validateSubmission(Demo submissionCandidate, Consumer<Demo> submitter) {

        Optional<Demo> loadedDemo = repository.findById(submissionCandidate.getId());
        if (loadedDemo.isPresent()) {
            Demo foundDemo = loadedDemo.get();
            if (DrawStatus.PREPARATION != foundDemo.getDrawStatus()) {
                throw new IllegalStateException(
                    "Unable to submut demo in status '" + foundDemo.getDrawStatus().getDescription() + "'");
            }
            submitter.accept(foundDemo);
            return foundDemo;
        } else {
            throw new IllegalStateException("Unable to find demo with id = " + submissionCandidate.getId());
        }
    }


    private static void recalculateDrawStatus(Demo demo) {
        demo.setDrawStatus(
            demo.getTasks()
                .stream()
                .allMatch(DemoTask::hasWinner) ? DrawStatus.FINISHED : DrawStatus.IN_PROGRESS
        );
    }
}
