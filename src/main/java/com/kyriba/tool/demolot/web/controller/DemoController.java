/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/24/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web.controller;

import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import com.kyriba.tool.demolot.service.DemoDrawService;
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

import static com.kyriba.tool.demolot.domain.editors.TeamMemberPropertyEditor.DEFAULT_FORMATTER;
import static com.kyriba.tool.demolot.web.controller.ControllerConstants.*;


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
        model.addAttribute(MODEL_DEMO, demoDrawService.getOne(demoId));
        model.addAttribute(MODEL_OPERATION, MODEL_OPERATION_EDIT);
        return withDemoRelatedPath(VIEW_DEMO);
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
            demoDrawService.submit(demo);
            return Mono.just("redirect:" + URL_DEMOS_ROOT);
        }
    }


    @DeleteMapping(value = URL_DEMOS_ROOT + "/{demoId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Void> deleteDemo(@PathVariable("demoId") final long demoId) {
        //TODO: handle EmptyResultDataAccessException??
        demoDrawService.deleteById(demoId);
        return Mono.empty();
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/formtask")
    public Mono<String> showDemo(Model model,
                                 @PathVariable("demoId") final long demoId) {
        model.addAttribute("demo", demoDrawService.getOne(demoId));
        return withDemoRelatedPath("demoWithTasks");
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/form")
    public Mono<String> showDemoTaskCreateForm(@PathVariable("demoId") final long demoId,
                                               Model model) {
        withTaskModel(model, demoDrawService.getOne(demoId), new DemoTask(), MODEL_OPERATION_CREATE);
        return withDemoRelatedPath(VIEW_DEMO_TASK);
    }


    @GetMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}/form")
    public Mono<String> showDemoTaskEditForm(@PathVariable("demoId") final long demoId,
                                             @PathVariable("taskId") final long taskId,
                                             Model model) {
        Demo demo = demoDrawService.getOne(demoId);
        withTaskModel(
                model,
                demoDrawService.getOne(demoId),
                demo.getTaskById(taskId),
                withDemoRelatedPath(MODEL_OPERATION_EDIT).block());
        return withDemoRelatedPath(VIEW_DEMO_TASK);
    }


    @PostMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks")
    public Mono<String> submitDemoTask(@PathVariable("demoId") final long demoId,
                                       @Valid DemoTask demoTask,
                                       BindingResult result,
                                       Model model) {
        Demo demo = demoDrawService.getOne(demoId);

        if (result.hasErrors()) {
            withTaskModel(model, demo, demoTask, ControllerConstants.modelOperation(demoTask));
            return withDemoRelatedPath(VIEW_DEMO_TASK);
        } else {
            demoDrawService.submitTask(demo, demoTask);
            return Mono.just("redirect:" + URL_DEMOS_ROOT + "/" + demoId + "/formtask/");
        }
    }


    @DeleteMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Void> deleteDemoTask(@PathVariable("demoId") final long demoId,
                                     @PathVariable("taskId") final long taskId) {
        //TODO: handle EmptyResultDataAccessException??
        demoDrawService.deleteTask(demoDrawService.getOne(demoId), taskId);
        return Mono.empty();
    }


    private void withTaskModel(Model model, Demo demo, DemoTask task, String operation) {
        model.addAttribute(MODEL_DEMO, demo);
        model.addAttribute(MODEL_DEMO_TASK, task);
        model.addAttribute(MODEL_MEMBERS,
                teamMemberRepository.findActiveOnly()
                        .stream()
                        .map(DEFAULT_FORMATTER)
                        .sorted()
                        .collect(Collectors.toList())
        );
        model.addAttribute(MODEL_OPERATION, operation);
    }


}