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
import com.kyriba.tool.demolot.repository.DemoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Objects;

import static org.springframework.data.domain.Sort.Order.desc;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class DemoController
{
  private static final String ROOT_URL = "/demos";
  private static final String MODEL_OPERATION = "operation";
  private static final String MODEL_DEMO = "demo";
  private static final String VIEW_DEMO = "demo";

  @Autowired
  private DemoRepository demoRepository;


  @RequestMapping(value = ROOT_URL, method = RequestMethod.GET)
  String showAllDemos(ModelMap modal)
  {
    modal.addAttribute(
        "demos",
        demoRepository.findAll(Sort.by(desc("id")))
    );

    return "demos";
  }


  @RequestMapping(value = ROOT_URL + "/form", method = RequestMethod.GET)
  public String showDemoCreateForm(ModelMap modelMap)
  {
    Demo newDemo = new Demo();
    newDemo.setPlannedDate(LocalDate.now());

    modelMap.put(MODEL_DEMO, newDemo);
    modelMap.put(MODEL_OPERATION, "Create");
    return VIEW_DEMO;
  }


  @RequestMapping(value = ROOT_URL + "/form/{demoId}", method = RequestMethod.GET)
  public String showDemoEditForm(@PathVariable("demoId") final long demoId,
                                 ModelMap modelMap)
  {
    modelMap.put(MODEL_DEMO, demoRepository.getOne(demoId));
    modelMap.put(MODEL_OPERATION, "Edit");
    return VIEW_DEMO;
  }


  @RequestMapping(value = ROOT_URL, method = RequestMethod.POST)
  public String submitDemo(@Valid @ModelAttribute(MODEL_DEMO) Demo demo,
                           BindingResult result,
                           ModelMap modelMap)
  {
    if (result.hasErrors()) {
      modelMap.put(MODEL_DEMO, demo);
      modelMap.put(MODEL_OPERATION, Objects.isNull(demo.getId()) ? "Create" : "Edit");
      return VIEW_DEMO;
    }
    else {
      Demo demoToBeSaved = demo;

      if (Objects.nonNull(demo.getId()) && demoRepository.existsById(demo.getId())) {
        //update
        demoToBeSaved = demoRepository.getOne(demo.getId());
        demoToBeSaved.setTitle(demo.getTitle());
        demoToBeSaved.setPlannedDate(demo.getPlannedDate());
        demoToBeSaved.setSummary(demo.getSummary());
        demoToBeSaved.setLink(demo.getLink());
      }

      //save and go to the list of members
      demoRepository.save(demoToBeSaved);
      return "redirect:" + ROOT_URL;
    }
  }


  @RequestMapping(value = ROOT_URL + "/{demoId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteDemo(@PathVariable("demoId") final long demoId)
  {
    //TODO: handle EmptyResultDataAccessException??
    demoRepository.deleteById(demoId);
  }
}