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
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
public class DemoController
{
  private static final String VIEW_DEMO = "demo";
  private static final String VIEW_DEMO_TASK = "task";

  @Autowired
  private DemoDrawService demoDrawService;

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @RequestMapping(value = URL_DEMOS_ROOT, method = RequestMethod.GET)
  String showAllDemos(ModelMap modal)
  {
    modal.addAttribute("demos", demoDrawService.findAll());

    return "demos";
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/form", method = RequestMethod.GET)
  public String showDemoCreateForm(ModelMap modelMap)
  {
    Demo newDemo = new Demo();
    newDemo.setPlannedDate(LocalDate.now());

    modelMap.put(MODEL_DEMO, newDemo);
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_CREATE);
    return VIEW_DEMO;
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/form", method = RequestMethod.GET)
  public String showDemoEditForm(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    modelMap.put(MODEL_DEMO, demoDrawService.getOne(demoId));
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_EDIT);
    return VIEW_DEMO;
  }


  @RequestMapping(value = URL_DEMOS_ROOT, method = RequestMethod.POST)
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
      return "redirect:" + URL_DEMOS_ROOT;
    }
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDemo(@PathVariable("demoId") final long demoId)
  {
    //TODO: handle EmptyResultDataAccessException??
    demoDrawService.deleteById(demoId);
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/formtask", method = RequestMethod.GET)
  String showDemo(ModelMap modal,
                  @PathVariable("demoId") final long demoId)
  {
    modal.addAttribute("demo", demoDrawService.getOne(demoId));
    return "demoWithTasks";
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/form", method = RequestMethod.GET)
  public String showDemoTaskCreateForm(@PathVariable("demoId") final long demoId,
                                       ModelMap modelMap)
  {
    withTaskModel(modelMap, demoDrawService.getOne(demoId), new DemoTask(), MODEL_OPERATION_CREATE);
    return VIEW_DEMO_TASK;
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}/form", method = RequestMethod.GET)
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


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks", method = RequestMethod.POST)
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
      return "redirect:" + URL_DEMOS_ROOT + "/" + demoId + "/formtask/";
    }
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/tasks/{taskId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDemoTack(@PathVariable("demoId") final long demoId,
                             @PathVariable("taskId") final long taskId)
  {
    //TODO: handle EmptyResultDataAccessException??
    demoDrawService.deleteTask(demoDrawService.getOne(demoId), taskId);
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