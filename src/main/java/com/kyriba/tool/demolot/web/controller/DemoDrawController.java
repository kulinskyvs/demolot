/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 6/1/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web.controller;

import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import com.kyriba.tool.demolot.service.DemoDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

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
  private TeamMemberRepository teamMemberRepository;


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw", method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  public void startDemoDraw(@PathVariable("demoId") final long demoId,
                            ModelMap modelMap)
  {
    demoDrawService.startDraw(demoId);
  }


  @RequestMapping(value = URL_DEMOS_ROOT + "/{demoId}/draw", method = RequestMethod.GET)
  public String showDemoDrawForm(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    modelMap.put(MODEL_DEMO, demoDrawService.getOne(demoId));
    return VIEW_DEMO_DRAW;
  }

}