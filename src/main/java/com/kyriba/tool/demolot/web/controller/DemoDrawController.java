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
import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import com.kyriba.tool.demolot.service.DemoDrawService;
import com.kyriba.tool.demolot.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.kyriba.tool.demolot.web.controller.ControllerConstants.MODEL_DEMO;
import static com.kyriba.tool.demolot.web.controller.ControllerConstants.URL_DEMOS_ROOT;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class DemoDrawController
{
  private static final String VIEW_DEMO_DRAW = "demoDraw";


  @Autowired
  private DemoDrawService demoDrawService;

  @Autowired
  private EmailService emailService;

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw", method = RequestMethod.POST)
  public String startDemoDraw(@PathVariable("demoId") final long demoId,
                              ModelMap modelMap)
  {
    return demoDrawView(demoDrawService.startDraw(demoId), modelMap);
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw", method = RequestMethod.GET)
  public String showDemoDrawForm(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    return demoDrawView(demoDrawService.getOne(demoId), modelMap);
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks", method = RequestMethod.POST)
  public String drawAllTasks(@PathVariable("demoId") final long demoId,
                             ModelMap modelMap)
  {
    return demoDrawView(
        withNotificationSent(demoDrawService.drawTasks(demoId)),
        modelMap
    );
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks/{taskId}", method = RequestMethod.POST)
  public String drawTask(@PathVariable("demoId") final long demoId,
                         @PathVariable("taskId") final long taskId,
                         ModelMap modelMap)
  {
    return demoDrawView(
        withNotificationSent(demoDrawService.drawTask(demoId, taskId)),
        modelMap
    );
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/tasks", method = RequestMethod.DELETE)
  public String resetDraw(@PathVariable("demoId") final long demoId,
                          ModelMap modelMap)
  {
    return demoDrawView(demoDrawService.resetDraw(demoId), modelMap);
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw/notifications", method = RequestMethod.POST)
  public String sendNotification(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    return demoDrawView(
        withNotificationSent(demoDrawService.getOne(demoId)),
        modelMap);
  }


  private static String demoDrawView(final Demo demo, ModelMap modelMap)
  {
    modelMap.put(MODEL_DEMO, demo);
    return VIEW_DEMO_DRAW;
  }


  private Demo withNotificationSent(Demo demo)
  {
    if (DrawStatus.FINISHED == demo.getDrawStatus()) {
      emailService.notifyDemoResults(demo);
    }
    return demo;
  }
}