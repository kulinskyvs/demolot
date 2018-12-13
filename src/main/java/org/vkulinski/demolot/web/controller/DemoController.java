package org.vkulinski.demolot.web.controller;

import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.vkulinski.demolot.repository.TeamMemberRepository;
import org.vkulinski.demolot.service.DemoDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static org.vkulinski.demolot.domain.editors.TeamMemberPropertyEditor.DEFAULT_FORMATTER;
import static org.vkulinski.demolot.web.controller.ControllerConstants.*;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class DemoController {
    private static final String VIEW_DEMO = "demo";
    private static final String VIEW_DEMO_TASK = "task";

    @Autowired
    private DemoDrawService demoDrawService;

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    @GetMapping(value = URL_DEMOS_ROOT)
    public Mono<String> showAllDemos(Model model) {
        model.addAttribute("demos", demoDrawService.findAll());
        return withDemoRelatedPath("demos");
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/form")
    public Mono<String> showDemoCreateForm(Model model) {
        Demo newDemo = new Demo();
        newDemo.setPlannedDate(LocalDate.now());

        model.addAttribute(MODEL_DEMO, newDemo);
        model.addAttribute(MODEL_OPERATION, MODEL_OPERATION_CREATE);
        return withDemoRelatedPath(VIEW_DEMO);
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/form")
    public Mono<String> showDemoEditForm(@PathVariable("demoId") final long demoId,
                                         Model model) {
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> {
                model.addAttribute(MODEL_DEMO, demo);
                model.addAttribute(MODEL_OPERATION, MODEL_OPERATION_EDIT);
                return withDemoRelatedPath(VIEW_DEMO);
            });
    }


    @PostMapping(value = URL_DEMOS_ROOT)
    public Mono<String> submitDemo(@Valid Demo demo,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            model.addAttribute(MODEL_OPERATION, ControllerConstants.modelOperation(demo));
            return withDemoRelatedPath(VIEW_DEMO);
        } else {
            //submit and go to the list of members
            return demoDrawService
                .submit(demo)
                .thenReturn("redirect:" + URL_DEMOS_ROOT);
        }
    }


    @DeleteMapping(value = URL_DEMOS_ROOT + "/{demoId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Void> deleteDemo(@PathVariable("demoId") final long demoId) {
        //TODO: handle EmptyResultDataAccessException??
        return demoDrawService
            .deleteById(demoId)
            .then();
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/formtask")
    public Mono<String> showDemo(Model model,
                                 @PathVariable("demoId") final long demoId) {
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> {
                model.addAttribute("demo", demo);
                return withDemoRelatedPath("demoWithTasks");
            });
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/form")
    public Mono<String> showDemoTaskCreateForm(@PathVariable("demoId") final long demoId,
                                               Model model) {
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> {
                withTaskModel(model, demo, new DemoTask(), MODEL_OPERATION_CREATE);
                return withDemoRelatedPath(VIEW_DEMO_TASK);
            });
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}/form")
    public Mono<String> showDemoTaskEditForm(@PathVariable("demoId") final long demoId,
                                             @PathVariable("taskId") final long taskId,
                                             Model model) {
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> {
                withTaskModel(model, demo, demo.getTaskById(taskId), withDemoRelatedPath(MODEL_OPERATION_EDIT).block());
                return withDemoRelatedPath(VIEW_DEMO_TASK);
            });
    }


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks")
    public Mono<String> submitDemoTask(@PathVariable("demoId") final long demoId,
                                       @Valid DemoTask demoTask,
                                       BindingResult result,
                                       Model model) {
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> submitDemoTask(demo, demoTask, result, model));
    }


    private Mono<String> submitDemoTask(Demo demo, DemoTask demoTask, BindingResult result, Model model) {
        if (result.hasErrors()) {
            withTaskModel(model, demo, demoTask, ControllerConstants.modelOperation(demoTask));
            return withDemoRelatedPath(VIEW_DEMO_TASK);
        } else {
            return demoDrawService
                .submitTask(demo, demoTask)
                .thenReturn("redirect:" + URL_DEMOS_ROOT + "/" + demo.getId() + "/formtask/");
        }
    }


    @DeleteMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Void> deleteDemoTask(@PathVariable("demoId") final long demoId,
                                     @PathVariable("taskId") final long taskId) {
        //TODO: handle EmptyResultDataAccessException??
        return demoDrawService
            .getOne(demoId)
            .flatMap(demo -> demoDrawService.deleteTask(demo, taskId))
            .then();
    }


    private void withTaskModel(Model model, Demo demo, DemoTask task, String operation) {
        model.addAttribute(MODEL_DEMO, demo);
        model.addAttribute(MODEL_DEMO_TASK, task);
        model.addAttribute(
            MODEL_MEMBERS,
            teamMemberRepository
                .findActiveOnly()
                .stream()
                .map(DEFAULT_FORMATTER)
                .sorted()
                .collect(Collectors.toList())
        );
        model.addAttribute(MODEL_OPERATION, operation);
    }

}