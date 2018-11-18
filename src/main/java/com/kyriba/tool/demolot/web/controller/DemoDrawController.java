/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 6/1/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web.controller;

import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import com.kyriba.tool.demolot.service.DemoDrawService;
import com.kyriba.tool.demolot.service.EmailService;
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

import static com.kyriba.tool.demolot.web.controller.ControllerConstants.*;


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