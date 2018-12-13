package org.vkulinski.demolot.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import org.vkulinski.demolot.DemolotApplication;
import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import static org.vkulinski.demolot.repository.DemoRepositoryIT.DATASET;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


/**
 * @author M-VKU
 * @version 1.0
 */
@TestExecutionListeners({
    DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class,
    DbUnitTestExecutionListener.class })
@DatabaseSetup(DATASET)
@DatabaseTearDown(type = DatabaseOperation.DELETE_ALL, value = { DATASET })
@DirtiesContext
@SpringBootTest(classes = DemolotApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class DemoRepositoryIT
{
  static final String DATASET = "classpath:datasets/domain.xml";


  @Autowired
  private DemoRepository demoRepository;

  @Autowired
  private DemoTaskRepository demoTaskRepository;

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @Test
  public void removingTaskFromDemoMustDeleteTheTask()
  {
    //given
    Demo demo1 = demoRepository.getOne(1L);
    demo1.getTasks().remove(1);
    //when
    Demo savedDemo = demoRepository.save(demo1);
    //then
    assertThat(savedDemo.getTasks(), hasSize(1));
    assertThat(savedDemo.getTasks(), hasItems(demoTaskRepository.getOne(1L)));
    assertThat(demoTaskRepository.findById(2L).isPresent(), is(false));
  }


  @Test
  public void deleteDemoMustDeleteLinkedTasks()
  {
    //given
    Demo demo1 = demoRepository.getOne(1L);
    //when
    demoRepository.delete(demo1);
    //then
    assertThat(demoTaskRepository.findById(1L).isPresent(), is(false));
    assertThat(demoTaskRepository.findById(2L).isPresent(), is(false));
    assertThat(demoTaskRepository.findById(3L).isPresent(), is(true));
  }


  @Test
  public void addingNewTaskToDemoMustStoreIt()
  {
    //given
    Demo demo1 = demoRepository.getOne(1L);
    DemoTask demo1Task3 = new DemoTask();
    demo1Task3.setKey("demo1Task3");
    demo1Task3.setTitle("some title");
    demo1Task3.setOwner(teamMemberRepository.getOne(1L));
    demo1Task3.setDemo(demo1);
    demo1.getTasks().add(demo1Task3);
    //when
    Demo savedDemo = demoRepository.save(demo1);
    //then
    assertThat(savedDemo.getTasks(), hasSize(3));
    assertThat(savedDemo.getTasks(), hasItems(demoTaskRepository.getOne(1L), demoTaskRepository.getOne(2L)));
    DemoTask savedTask = demoTaskRepository.findByDemoAndKey(demo1, "demo1Task3").get(0);
    assertThat(savedDemo.getTasks(), hasItems(savedTask));
    assertThat(savedTask.getTitle(), equalTo("some title"));
    assertThat(savedTask.getDemo(), equalTo(savedDemo));
    assertThat(savedTask.getOwner(), equalTo(teamMemberRepository.getOne(1L)));
  }

}