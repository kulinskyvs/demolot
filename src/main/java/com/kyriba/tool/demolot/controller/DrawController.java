/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/24/2018     M-VKU   Initial                                                  *
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
public class DrawController
{
  static final String ROOT_URL = "/draws";


  @RequestMapping(ROOT_URL)
  String draws(ModelMap modal)
  {
    return "draws";
  }


  @RequestMapping(ROOT_URL + "/{drawId}")
  String draw(@PathVariable("drawId") final long drawId, ModelMap modal)
  {

    //TODO:!!!
    modal.addAttribute("drawId", drawId);
    return "draw";
  }

}