package org.vkulinski.demolot.web.controller;

import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.vkulinski.demolot.domain.DrawStatus;
import org.vkulinski.demolot.repository.TeamMemberRepository;
import org.vkulinski.demolot.service.DemoDrawService;
import org.vkulinski.demolot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.vkulinski.demolot.web.controller.ControllerConstants.*;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class DemoDrawController {
    private static final String VIEW_DEMO_DRAW = "demoDraw";
    private static final String EMAIL_TEMPLATE = "email-results";


    @Autowired
    private DemoDrawService demoDrawService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw")
    public Mono<String> startDemoDraw(@PathVariable("demoId") final long demoId,
                                      Model model) {
        return demoDrawView(demoDrawService.startDraw(demoId), model);
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw")
    public Mono<String> showDemoDrawForm(@PathVariable("demoId") final long demoId,
                                         Model model) {
        return demoDrawView(demoDrawService.getOne(demoId), model);
    }


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks")
    public Mono<String> drawAllTasks(@PathVariable("demoId") final long demoId,
                                     Model model) {
        return demoDrawView(
                demoDrawService
                        .drawTasks(demoId)
                        .map(this::withNotificationSent),
                model);
    }


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks/{taskId}")
    public Mono<String> drawTask(@PathVariable("demoId") final long demoId,
                                 @PathVariable("taskId") final long taskId,
                                 Model model) {
        return demoDrawView(
                demoDrawService
                        .drawTask(demoId, taskId)
                        .map(this::withNotificationSent),
                model
        );
    }


    @DeleteMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks")
    public Mono<String> resetDraw(@PathVariable("demoId") final long demoId,
                                  Model model) {
        return demoDrawView(
                demoDrawService.resetDraw(demoId), model
        );
    }


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/notifications")
    public Mono<String> sendNotification(@PathVariable("demoId") final long demoId,
                                         Model model) {
        return demoDrawView(
                demoDrawService
                        .getOne(demoId)
                        .map(this::withNotificationSent),
                model
        );
    }


    private static Mono<String> demoDrawView(final Mono<Demo> monoDemo, Model model) {
        return monoDemo.flatMap(demo -> {
            model.addAttribute(MODEL_DEMO, demo);

            List<DemoTask> demoTasks = new ArrayList<>(demo.getTasks());
            demoTasks.sort(Comparator.comparing(DemoTask::getId));
            model.addAttribute(MODEL_DEMO_TASKS, demoTasks);

            return withDemoRelatedPath(VIEW_DEMO_DRAW);
        });
    }


    private Demo withNotificationSent(Demo demo) {
        if (DrawStatus.FINISHED == demo.getDrawStatus()) {
            emailService.notifyDemoResults(demo, withDemoRelatedPath(EMAIL_TEMPLATE).block());
        }
        return demo;
    }
}