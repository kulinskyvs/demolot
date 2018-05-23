/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/23/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class ApplicationController
{
  private static final String TEAM_URL_ATTR = "team_url";
  private static final String TEAM_URL_VALUE = "/team";

  private static final String DRAWS_URL_ATTR = "draws_url";
  private static final String DRAWS_URL_VALUE = "/draws";


  @RequestMapping("/")
  String home(ModelMap modal)
  {
    modal.addAttribute("team_url", "/team");
    modal.addAttribute("draws_url", "/draws");
    return "index";
  }


  @RequestMapping("/team")
  String team(ModelMap modal)
  {
    withAppUrls(modal);
    return "team";
  }


  @RequestMapping("/draws")
  String draws(ModelMap modal)
  {
    withAppUrls(modal);
    return "draws";
  }


  @RequestMapping("/draws/{drawId}")
  String draw(@PathVariable("drawId") final long drawId, ModelMap modal)
  {

    //TODO:!!!
    withAppUrls(modal);
    modal.addAttribute("drawId", drawId);

    return "draw";
  }


  private static void withAppUrls(ModelMap modal)
  {
    modal.addAttribute(TEAM_URL_ATTR, TEAM_URL_VALUE);
    modal.addAttribute(DRAWS_URL_ATTR, DRAWS_URL_VALUE);
  }

}