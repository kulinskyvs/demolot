/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.domain.editors;

import com.kyriba.tool.demolot.domain.TeamMember;

import java.beans.PropertyEditorSupport;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;


/**
 * @author M-VKU
 * @version 1.0
 */
public class TeamMemberPropertyEditor extends PropertyEditorSupport
{
  public static Function<TeamMember, String> DEFAULT_FORMATTER = member -> Optional
      .ofNullable(member)
      .map(safeMember -> safeMember.getName() + " " + safeMember.getSurname())
      .orElse("");

  private final Function<TeamMember, String> formatter;
  private final List<TeamMember> members;


  public TeamMemberPropertyEditor(List<TeamMember> members)
  {
    this(members, DEFAULT_FORMATTER);
  }


  public TeamMemberPropertyEditor(List<TeamMember> members,
                                  Function<TeamMember, String> formatter)
  {
    this.members = members;
    this.formatter = formatter;
  }


  @Override
  public String getAsText()
  {
    return formatter.apply((TeamMember) getValue());
  }


  @Override
  public void setAsText(String text)
  {
    setValue(members.stream()
        .filter(member -> Objects.equals(text, formatter.apply(member)))
        .findFirst()
        .orElse(null)
    );
  }

}