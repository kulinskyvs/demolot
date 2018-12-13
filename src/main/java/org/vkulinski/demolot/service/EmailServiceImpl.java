package org.vkulinski.demolot.service;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.vkulinski.demolot.domain.TeamMember;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Collection;


/**
 * @author M-VKU
 * @version 1.0
 */
@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    public JavaMailSender emailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void notifyDemoResults(Demo demo, String emailTemplate) {
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


    void notify(TeamMember winner, Collection<DemoTask> wonTasks, Demo demo, String emailTemplate) {
        try {
            Context context = new Context();
            context.setVariables(ImmutableMap.of(
                    "member", winner,
                    "demo", demo,
                    "tasks", wonTasks
            ));
            String html = templateEngine.process(emailTemplate, context);
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setTo(winner.getEmail());
            helper.setSubject("[Demolot]: " + demo.getTitle() + " results");
            helper.setText(html, true);
            helper.addInline("logo.png", new ClassPathResource("/static/images/logo-transparent.png"));

            emailSender.send(message);
        } catch (MessagingException ex) {
            throw new RuntimeException("Unable to send an email", ex);
        }
    }
}
