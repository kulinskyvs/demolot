/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 6/11/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import com.kyriba.tool.demolot.domain.TeamMember;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;


/**
 * @author M-VKU
 * @version 1.0
 */
@Service
public class EmailServiceImpl implements EmailService
{
  @Autowired
  public JavaMailSender emailSender;

  @Autowired
  private Configuration freemarkerConfig;


  @Override
  public void notifyDemoResults(Demo demo, String emailTemplate)
  {
    Multimap<TeamMember, DemoTask> tasksByMember =
        demo
            .getTasks()
            .stream()
            .filter(DemoTask::hasWinner)
            .collect(
                HashMultimap::create,
                (multimap, task) -> multimap.put(task.getWinner(), task),
                (map1, map2) -> map1.putAll(map2));

    for (TeamMember member : tasksByMember.keySet()) {
      notify(member, tasksByMember.get(member), demo, emailTemplate);
    }
  }


  void notify(TeamMember winner, Collection<DemoTask> wonTasks, Demo demo, String emailTemplate)
  {
    try {
      Template template = freemarkerConfig.getTemplate(emailTemplate);
      String html = FreeMarkerTemplateUtils.processTemplateIntoString(
          template,
          ImmutableMap.of(
              "member", winner,
              "demo", demo,
              "tasks", wonTasks
          ));

      MimeMessage message = emailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
      helper.setTo(winner.getEmail());
      helper.setSubject("[Demolot]: " + demo.getTitle() + " results");
      helper.setText(html, true);
      helper.addInline("logo.png", new ClassPathResource("/static/images/logo-transparent.png"));

      emailSender.send(message);
    }
    catch (MessagingException | IOException | TemplateException ex) {
      throw new RuntimeException("Unable to send an email", ex);
    }
  }
}
