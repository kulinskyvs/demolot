/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/24/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web.controller;

import com.kyriba.tool.demolot.domain.TeamMember;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

import static com.kyriba.tool.demolot.web.controller.ControllerConstants.*;
import static org.springframework.data.domain.Sort.Order.asc;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class TeamMemberController
{
  private static final String ROOT_URL = "/teamembers";
  private static final String MODEL_MEMBER = "member";
  private static final String VIEW_MEMBER = "teammember";

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @RequestMapping(value = ROOT_URL, method = RequestMethod.GET)
  public String showAllTeamMembers(ModelMap modal)
  {
    modal.addAttribute(
        "members",
        teamMemberRepository.findAll(Sort.by(asc("id")))
    );
    return "team";
  }


  @RequestMapping(value = ROOT_URL + "/form", method = RequestMethod.GET)
  public String showCreateForm(ModelMap modelMap)
  {
    modelMap.put(MODEL_MEMBER, new TeamMember());
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_CREATE);
    return VIEW_MEMBER;
  }


  @RequestMapping(value = ROOT_URL + "/{memberId}/form", method = RequestMethod.GET)
  public String showEditForm(@PathVariable("memberId") final long memberId,
                             ModelMap modelMap)
  {
    modelMap.put(MODEL_MEMBER, teamMemberRepository.findById(memberId));
    modelMap.put(MODEL_OPERATION, MODEL_OPERATION_EDIT);
    return VIEW_MEMBER;
  }


  @RequestMapping(value = ROOT_URL, method = RequestMethod.POST)
  public String submitMember(@Valid @ModelAttribute(MODEL_MEMBER) TeamMember member,
                             BindingResult result,
                             ModelMap modelMap)
  {
    if (result.hasErrors() || isUniquenessViolated(member, modelMap)) {
      modelMap.put(MODEL_MEMBER, member);
      modelMap.put(MODEL_OPERATION, ControllerConstants.modelOperation(member));
      return VIEW_MEMBER;
    }
    else {
      //save and go to the list of members
      teamMemberRepository.save(member);
      return "redirect:" + ROOT_URL;
    }
  }


  @RequestMapping(value = ROOT_URL + "/{memberId}", method = RequestMethod.DELETE)
  @ResponseStatus(value = HttpStatus.OK)
  public void deleteMember(@PathVariable("memberId") final long memberId)
  {
    //TODO: handle EmptyResultDataAccessException??
    teamMemberRepository.deleteById(memberId);
  }


  private boolean isUniquenessViolated(TeamMember member, ModelMap modelMap)
  {
    boolean isViolated = Optional
        .ofNullable(teamMemberRepository.findByNameAndSurname(member.getName(), member.getSurname()))
        .map(foundMember -> !foundMember.equalsById(member))
        .orElse(false);

    if (isViolated) {
      modelMap.put("validationError", "A member with the same name and surname already exists");
    }
    return isViolated;
  }
}