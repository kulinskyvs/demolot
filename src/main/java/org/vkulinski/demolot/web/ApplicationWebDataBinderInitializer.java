package org.vkulinski.demolot.web;

import org.vkulinski.demolot.domain.DrawStatus;
import org.vkulinski.demolot.domain.TeamMember;
import org.vkulinski.demolot.domain.editors.DrawStatusPropertyEditor;
import org.vkulinski.demolot.domain.editors.TeamMemberPropertyEditor;
import org.vkulinski.demolot.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import static org.vkulinski.demolot.domain.editors.TeamMemberPropertyEditor.DEFAULT_FORMATTER;


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