/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/24/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.controller;

import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.domain.TeamMember;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import com.kyriba.tool.demolot.service.DemoDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

import static com.kyriba.tool.demolot.controller.ControllerConstants.*;
import static com.kyriba.tool.demolot.controller.TeamMemberPropertyEditor.DEFAULT_FORMATTER;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class DemoController
{
  private static final String ROOT_URL = "/demos";
  private static final String MODEL_DEMO = "demo";
  private static final String VIEW_DEMO = "demo";

  private static final String MODEL_MEMBERS = "members";
  private static final String MODEL_DEMO_TASK = "task";
  private static final String VIEW_DEMO_TASK = "task";

  @Autowired
  private DemoDrawService demoDrawService;

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @RequestMapping(value = ROOT_URL, method = RequestMethod.GET)
  String showAllDemos(ModelMap modal)
  {
    modal.addAttribute("demos", demoDrawService.findAll());

    return "demos";
  }


  @RequestMapping(value = ROOT_URL + "/form", method = RequestMethod.GET)
  public String showDemoCreateForm(ModelMap modelMap)
  {
    Demo newDemo = new Demo();
    newDemo.setPlannedDate(LocalDate.now());

    modelMap.put(MODEL_DEMO, newDemo);
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_CREATE);
    return VIEW_DEMO;
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/form", method = RequestMethod.GET)
  public String showDemoEditForm(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    modelMap.put(MODEL_DEMO, demoDrawService.getOne(demoId));
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_EDIT);
    return VIEW_DEMO;
  }


  @RequestMapping(value = ROOT_URL, method = RequestMethod.POST)
  public String submitDemo(@Valid @ModelAttribute(MODEL_DEMO) Demo demo,
                           BindingResult result,
                           ModelMap modelMap)
  {
    if (result.hasErrors()) {
      modelMap.put(MODEL_DEMO, demo);
      modelMap.put(MODEL_OPERATION, ControllerConstants.modelOperation(demo));
      return VIEW_DEMO;
    }
    else {
      //submit and go to the list of members
      demoDrawService.submit(demo);
      return "redirect:" + ROOT_URL;
    }
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDemo(@PathVariable("demoId") final long demoId)
  {
    //TODO: handle EmptyResultDataAccessException??
    demoDrawService.deleteById(demoId);
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/formtask", method = RequestMethod.GET)
  String showDemo(ModelMap modal,
                  @PathVariable("demoId") final long demoId)
  {
    modal.addAttribute("demo", demoDrawService.getOne(demoId));
    return "demoWithTasks";
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/tasks/form", method = RequestMethod.GET)
  public String showDemoTaskCreateForm(@PathVariable("demoId") final long demoId,
                                       ModelMap modelMap)
  {
    withTaskModel(modelMap, demoDrawService.getOne(demoId), new DemoTask(), MODEL_OPERATION_CREATE);
    return VIEW_DEMO_TASK;
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/tasks/{taskId}/form", method = RequestMethod.GET)
  public String showDemoTaskEditForm(@PathVariable("demoId") final long demoId,
                                     @PathVariable("taskId") final long taskId,
                                     ModelMap modelMap)
  {
    Demo demo = demoDrawService.getOne(demoId);
    withTaskModel(
        modelMap,
        demoDrawService.getOne(demoId),
        demo.getTaskById(taskId),
        MODEL_OPERATION_EDIT);
    return VIEW_DEMO_TASK;
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/tasks", method = RequestMethod.POST)
  public String submitDemoTask(@PathVariable("demoId") final long demoId,
                               @Valid @ModelAttribute(MODEL_DEMO_TASK) DemoTask demoTask,
                               BindingResult result,
                               ModelMap modelMap)
  {
    Demo demo = demoDrawService.getOne(demoId);

    if (result.hasErrors()) {
      withTaskModel(modelMap, demo, demoTask, ControllerConstants.modelOperation(demoTask));
      return VIEW_DEMO_TASK;
    }
    else {
      demoDrawService.submitTask(demo, demoTask);
      return "redirect:" + ROOT_URL + "/" + demoId + "/formtask/";
    }
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}/tasks/{taskId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDemoTack(@PathVariable("demoId") final long demoId,
                             @PathVariable("taskId") final long taskId)
  {
    //TODO: handle EmptyResultDataAccessException??
    demoDrawService.deleteTask(demoDrawService.getOne(demoId), taskId);
  }


  @InitBinder
  public void initBinder(HttpServletRequest requst, ServletRequestDataBinder binder)
  {
    binder.registerCustomEditor(DrawStatus.class, new DrawStatusPropertyEditor());
    binder.registerCustomEditor(TeamMember.class,
        new TeamMemberPropertyEditor(teamMemberRepository.findAll(), DEFAULT_FORMATTER)
    );
  }


  private void withTaskModel(ModelMap modelMap, Demo demo, DemoTask task, String operation)
  {
    modelMap.put(MODEL_DEMO, demo);
    modelMap.put(MODEL_DEMO_TASK, task);
    modelMap.put(MODEL_MEMBERS,
        teamMemberRepository.findActiveOnly()
            .stream()
            .map(DEFAULT_FORMATTER)
            .sorted()
            .collect(Collectors.toList())
    );
    modelMap.put(MODEL_OPERATION, operation);
  }
}