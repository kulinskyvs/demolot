/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 12/14/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package org.vkulinski.demolot.domain;

import com.jparams.verifier.tostring.ToStringVerifier;
import com.jparams.verifier.tostring.preset.Presets;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.meanbean.test.BeanTester;
import org.meanbean.test.ConfigurationBuilder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.NoSuchElementException;

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;


/**
 * @author M-VKU
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class DemoTest
{
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private Demo demo;

  @Mock
  private DemoTask task1;
  @Mock
  private DemoTask task2;


  @Before
  public void setUp() throws Exception
  {
    given(task1.getId()).willReturn(1L);
    given(task2.getId()).willReturn(2L);
    demo = new Demo();
    demo.setTasks(Arrays.asList(task1, task2));
  }


  @Test
  public void testDefaultState()
  {
    // when
    Demo demo = new Demo();
    // then
    assertNull(demo.getId());
    assertNull(demo.getTitle());
    assertNull(demo.getPlannedDate());
    assertNull(demo.getSummary());
    assertNull(demo.getLink());
    assertNull(demo.getTasks());
    assertThat(demo.getDrawStatus(), equalTo(DrawStatus.PREPARATION));
  }


  @Test
  public void testEqualsAndHashCodeContract()
  {
    EqualsVerifier
        .forClass(Demo.class)
        .suppress(ALL_FIELDS_SHOULD_BE_USED)
        .withPrefabValues(Demo.class, mock(Demo.class), mock(Demo.class))
        .verify();
  }


  @Test
  public void testToString()
  {
    ToStringVerifier
        .forClass(Demo.class)
        .withPreset(Presets.APACHE_TO_STRING_BUILDER_MULTI_LINE_STYLE)
        .withOnlyTheseFields("id", "title", "plannedDate")
        .withHashCode(false)
        .verify();
  }


  @Test
  public void testJavaBeanContract()
  {
    new BeanTester().testBean(
        Demo.class,
        new ConfigurationBuilder()
            .overrideFactory("plannedDate", LocalDate::now)
            .build());
  }


  @Test
  public void getTaskByIdForExistingIdMustReturnTheTask()
  {
    assertThat(demo.getTaskById(1L), sameInstance(task1));
    assertThat(demo.getTaskById(2L), sameInstance(task2));
  }


  @Test
  public void getTaskByIdForNonExistingIdMustFail()
  {
    // given
    demo.setId(1L);
    expectedException.expect(NoSuchElementException.class);
    expectedException.expectMessage("Unable to find a task with id 3 into a demo with id [1]");
    // when
    demo.getTaskById(3L);
  }
}