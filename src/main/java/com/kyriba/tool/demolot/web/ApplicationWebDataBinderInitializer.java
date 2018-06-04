/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 6/1/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web;

import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.domain.TeamMember;
import com.kyriba.tool.demolot.domain.editors.DrawStatusPropertyEditor;
import com.kyriba.tool.demolot.domain.editors.TeamMemberPropertyEditor;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import static com.kyriba.tool.demolot.domain.editors.TeamMemberPropertyEditor.DEFAULT_FORMATTER;


/**
 * @author M-VKU
 * @version 1.0
 */
@ControllerAdvice
public class ApplicationWebDataBinderInitializer
{
  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @InitBinder
  public void dataBinding(WebDataBinder binder)
  {
    binder.registerCustomEditor(DrawStatus.class, new DrawStatusPropertyEditor());
    binder.registerCustomEditor(TeamMember.class,
        new TeamMemberPropertyEditor(teamMemberRepository::findAll, DEFAULT_FORMATTER)
    );
  }

}