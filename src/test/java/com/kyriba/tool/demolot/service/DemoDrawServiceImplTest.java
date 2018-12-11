/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.kyriba.tool.demolot.DemolotApplication;
import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.repository.DemoRepository;
import com.kyriba.tool.demolot.repository.DemoTaskRepository;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import org.hibernate.LazyInitializationException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

import static com.kyriba.tool.demolot.domain.DrawStatus.*;
import static com.kyriba.tool.demolot.service.DemoDrawServiceImplTest.DATASET;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;


/**
 * @author M-VKU
 * @version 1.0
 */
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@DatabaseSetup(DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = {DATASET})
@DirtiesContext
@SpringBootTest(classes = DemolotApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoDrawServiceImplTest {
    static final String DATASET = "classpath:datasets/domain.xml";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private TeamMemberRepository memberRepository;

    @Autowired
    private DemoTaskRepository taskRepository;

    private DemoDrawServiceImpl service;


    @Before
    public void setUp() throws Exception {
        service = new DemoDrawServiceImpl(demoRepository, memberRepository);
    }


    @Test
    public void existsByIdReturnsTrueForExistingId() {
        assertThat(service.existsById(1L), is(true));
    }


    @Test
    public void existsByIdReturnsFalseFornonExistingId() {
        assertThat(service.existsById(11L), is(false));
    }


    @Test
    public void getOneReturnDemoForExistingId() {
        //when
        Demo demo = service.getOne(1L).block();
        //then
        assertThat(demo.getId(), equalTo(1L));
        assertThat(demo.getTitle(), equalTo("Demo1"));
        assertThat(demo.getPlannedDate(), equalTo(LocalDate.of(2018, Month.MAY, 25)));
        assertThat(demo.getDrawStatus(), equalTo(IN_PROGRESS));
        assertThat(demo.getSummary(), nullValue());
        assertThat(demo.getLink(), nullValue());
    }


    @Test(expected = LazyInitializationException.class)
    public void getOneMusFailForNonExistingId() {
        service.getOne(11L).block().getDrawStatus();
    }


    @Test
    public void findAllMustRetunsAllDemosOrderByIdDesc() {
        //when
        List<Demo> demos = service.findAll().collectList().block();
        //then
        assertThat(demos, hasSize(3));
        assertThat(demos.get(0).getId(), equalTo(3L));
        assertThat(demos.get(1).getId(), equalTo(2L));
        assertThat(demos.get(2).getId(), equalTo(1L));
    }


    @Test
    public void deleteByIdMustDeleteExistingDemoInStatusPreparation() {
        //when
        service.deleteById(2).block();
        //then
        assertThat(demoRepository.findById(2L).isPresent(), is(false));
    }


    @Test
    public void deleteByIdMustDeleteExistingDemoInStatusInProgress() {
        //given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to delete demo in status '" + IN_PROGRESS.getDescription() + "'");
        //when
        service.deleteById(1).block();
    }


    @Test
    public void deleteByIdMustDeleteExistingDemoInStatusFinished() {
        //given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to delete demo in status '" + FINISHED.getDescription() + "'");
        //when
        service.deleteById(3).block();
    }


    @Test
    public void deleteByIdMustFailWithNonExistingId() {
        //given
        expectedException.expect(LazyInitializationException.class);
        expectedException.expectMessage("Unable to find com.kyriba.tool.demolot.domain.Demo with id 4");
        //when
        service.deleteById(4).block();
    }


    @Test
    public void submitDemoWithoutIdCreateNew() {
        //given
        Demo demo = new Demo();
        demo.setTitle("New demo");
        demo.setPlannedDate(LocalDate.of(2018, Month.MAY, 28));
        //when
        Demo submittedDemo = service.submit(demo).block();
        //then
        List<Demo> allDemos = service.findAll().collectList().block();
        assertThat(allDemos, hasSize(4));
        assertThat(allDemos.get(0).getId(), equalTo(submittedDemo.getId()));
        assertThat(allDemos.get(0).getPlannedDate(), equalTo(LocalDate.of(2018, Month.MAY, 28)));
        assertThat(allDemos.get(0).getDrawStatus(), equalTo(PREPARATION));
        assertThat(allDemos.get(0).getSummary(), nullValue());
        assertThat(allDemos.get(0).getLink(), nullValue());
    }


    @Test
    public void submitDemoWithNonExistingIdMustFail() {
        //given
        Demo demo = new Demo();
        demo.setId(4L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to find demo with id = 4");
        //when
        service.submit(demo).block();
    }


    @Test
    public void submitDemoInStatusInProgressMustFail() {
        //given
        Demo demo = new Demo();
        demo.setId(1L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to submut demo in status '" + IN_PROGRESS.getDescription() + "'");
        //when
        service.submit(demo).block();
    }


    @Test
    public void submitDemoInStatusFinishedMustFail() {
        //given
        Demo demo = new Demo();
        demo.setId(3L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to submut demo in status '" + FINISHED.getDescription() + "'");
        //when
        service.submit(demo).block();
    }


    @Test
    public void submitDemoInStatusPreparationMustUpdateId() {
        //given
        Demo demo = new Demo();
        demo.setId(2L);
        demo.setTitle("updated demo");
        demo.setPlannedDate(LocalDate.of(2018, Month.MAY, 28));
        demo.setDrawStatus(DrawStatus.IN_PROGRESS);
        demo.setSummary("updated summary");
        demo.setLink("http://google.com");
        //when
        Demo updatedDemo = service.submit(demo).block();
        //then
        assertThat(updatedDemo.getId(), equalTo(2L));
        assertThat(updatedDemo.getTitle(), equalTo("updated demo"));
        assertThat(updatedDemo.getPlannedDate(), equalTo(LocalDate.of(2018, Month.MAY, 28)));
        assertThat(updatedDemo.getDrawStatus(), equalTo(DrawStatus.IN_PROGRESS));
        assertThat(updatedDemo.getSummary(), equalTo("updated summary"));
        assertThat(updatedDemo.getLink(), equalTo("http://google.com"));

        List<Demo> allDemos = service.findAll().collectList().block();
        assertThat(allDemos, hasSize(3));
        assertThat(allDemos.get(1).getTitle(), equalTo("updated demo"));
        assertThat(allDemos.get(1).getPlannedDate(), equalTo(LocalDate.of(2018, Month.MAY, 28)));
        assertThat(allDemos.get(1).getDrawStatus(), equalTo(DrawStatus.IN_PROGRESS));
        assertThat(allDemos.get(1).getSummary(), equalTo("updated summary"));
        assertThat(allDemos.get(1).getLink(), equalTo("http://google.com"));
    }


    @Test
    public void submitTaskForNonExistingDemoMustFail() {
        //given
        Demo demo = new Demo();
        demo.setId(4L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to find demo with id = 4");
        //when
        service.submitTask(demo, new DemoTask()).block();
    }


    @Test
    public void submitTaskForInProgressDemoMustFail() {
        //given
        Demo demo = demoRepository.getOne(1L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to submut demo in status '" + IN_PROGRESS.getDescription() + "'");
        //when
        service.submitTask(demo, new DemoTask()).block();
    }


    @Test
    public void submitTaskForFinishedDemoMustFail() {
        //given
        Demo demo = demoRepository.getOne(3L);
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Unable to submut demo in status '" + FINISHED.getDescription() + "'");
        //when
        service.submitTask(demo, new DemoTask()).block();
    }


    @Test
    public void submitTaskForPreparationDemoAndNewTaskMustCreateItAndLinkWithDemo() {
        //given
        Demo demo = demoRepository.getOne(2L);
        DemoTask demoTask = new DemoTask();
        demoTask.setKey("key");
        demoTask.setTitle("title");
        demoTask.setOwner(memberRepository.getOne(1L));
        demoTask.setLink("http://google.com");
        //when
        Demo updatedDemo = service.submitTask(demo, demoTask).block();
        //then
        DemoTask createdTask = taskRepository.findByDemoAndKey(updatedDemo, "key").get(0);
        assertThat(createdTask.getKey(), equalTo("key"));
        assertThat(createdTask.getTitle(), equalTo("title"));
        assertThat(createdTask.getLink(), equalTo("http://google.com"));
        assertThat(createdTask.getOwner(), equalTo(memberRepository.getOne(1L)));
        assertThat(createdTask.getDemo(), equalTo(updatedDemo));

        assertThat(updatedDemo.getTasks(), hasSize(2));
        DemoTask taskFromUpdatedDemo = updatedDemo.getTaskById(createdTask.getId());
        assertThat(taskFromUpdatedDemo.getKey(), equalTo("key"));
        assertThat(taskFromUpdatedDemo.getTitle(), equalTo("title"));
        assertThat(taskFromUpdatedDemo.getLink(), equalTo("http://google.com"));
        assertThat(taskFromUpdatedDemo.getOwner(), equalTo(memberRepository.getOne(1L)));
        assertThat(taskFromUpdatedDemo.getDemo(), equalTo(updatedDemo));
    }


    @Test
    public void submitTaskForPreparationDemoAndExistingTaskFromAnotherDemoMustFail() {
        //given
        Demo demo = demoRepository.getOne(2L);
        DemoTask demoTask = taskRepository.getOne(4L);
        expectedException.expect(NoSuchElementException.class);
        expectedException.expectMessage("Unable to find a task with id 4 into a demo with id [2]");
        //when
        service.submitTask(demo, demoTask).block();
    }


    @Test
    public void submitTaskForPreparationDemoAndExistingTaskFromThisDemoMustUpdateTask() {
        //given
        Demo demo = demoRepository.getOne(2L);
        DemoTask demoTask = new DemoTask();
        demoTask.setId(3L);
        demoTask.setKey("key");
        demoTask.setTitle("title");
        demoTask.setLink("http://google.com");
        demoTask.setOwner(memberRepository.getOne(2L));
        //when
        Demo updatedDemo = service.submitTask(demo, demoTask).block();
        //then
        DemoTask createdTask = taskRepository.getOne(3L);
        assertThat(createdTask.getKey(), equalTo("key"));
        assertThat(createdTask.getTitle(), equalTo("title"));
        assertThat(createdTask.getLink(), equalTo("http://google.com"));
        assertThat(createdTask.getOwner(), equalTo(memberRepository.getOne(2L)));
        assertThat(createdTask.getDemo(), equalTo(updatedDemo));

        assertThat(updatedDemo.getTasks(), hasSize(1));
        DemoTask taskFromUpdatedDemo = updatedDemo.getTaskById(3L);
        assertThat(taskFromUpdatedDemo.getKey(), equalTo("key"));
        assertThat(taskFromUpdatedDemo.getTitle(), equalTo("title"));
        assertThat(taskFromUpdatedDemo.getLink(), equalTo("http://google.com"));
        assertThat(taskFromUpdatedDemo.getOwner(), equalTo(memberRepository.getOne(2L)));
        assertThat(taskFromUpdatedDemo.getDemo(), equalTo(updatedDemo));
    }


    @Test
    public void startDrawMustUpdateOnlyInPreparationDraws() {
        service.startDraw(1L).block();
        assertThat(demoRepository.getOne(1L).getDrawStatus(), equalTo(DrawStatus.IN_PROGRESS));

        service.startDraw(2L).block();
        assertThat(demoRepository.getOne(2L).getDrawStatus(), equalTo(DrawStatus.IN_PROGRESS));

        service.startDraw(3L).block();
        assertThat(demoRepository.getOne(3L).getDrawStatus(), equalTo(DrawStatus.FINISHED));
    }
}