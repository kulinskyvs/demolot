package com.kyriba.tool.demolot.service;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseOperation;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DatabaseTearDown;
import com.kyriba.tool.demolot.DemolotApplication;
import com.kyriba.tool.demolot.domain.TeamMember;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static com.kyriba.tool.demolot.service.TeamMemberRepositoryIT.DATASET;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


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
public class TeamMemberRepositoryIT
{

  static final String DATASET = "classpath:datasets/domain.xml";

  @Autowired
  private TeamMemberRepository teamMemberRepository;


  @Test
  public void findActiveOnlyShouldReturnTwoUsers()
  {
    //when
    List<TeamMember> activeMembers = teamMemberRepository.findActiveOnly();
    //then
    assertThat(activeMembers, hasSize(2));
    assertThat(activeMembers, hasItems(
        new TeamMember("Vasil", "Pupkin"),
        new TeamMember("Sidor", "Sidorov")
    ));
  }


  @Test
  public void findByActiveForActiveMustReturnTwoUsers()
  {
    //when
    List<TeamMember> activeMembers = teamMemberRepository.findByActive(true);
    //then
    assertThat(activeMembers, hasSize(2));
    assertThat(activeMembers, hasItems(
        new TeamMember("Vasil", "Pupkin"),
        new TeamMember("Sidor", "Sidorov")
    ));
  }


  @Test
  public void findByActiveForNonActiveMustReturnSingleUsers()
  {
    //when
    List<TeamMember> activeMembers = teamMemberRepository.findByActive(false);
    //then
    assertThat(activeMembers, hasSize(1));
    assertThat(activeMembers, hasItems(new TeamMember("Ivan", "Ivanov")));
  }


  @Test
  public void findByNameAndSurnameMustReturnForExisting()
  {
    assertThat(teamMemberRepository.findByNameAndSurname("Vasil", "Pupkin").getId(), equalTo(1L));
    assertThat(teamMemberRepository.findByNameAndSurname("Vasil", "Pupkin_"), nullValue());
  }


  @Test(expected = DataIntegrityViolationException.class)
  public void saveMustFailForNonUniqueNameAndSurName()
  {
    //given
    TeamMember newMember = new TeamMember("Vasil", "Pupkin");
    newMember.setEmail("somewho@somewhere.com");
    //when
    teamMemberRepository.save(newMember);
  }

}