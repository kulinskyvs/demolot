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
import org.junit.Test;
import org.meanbean.test.BeanTester;
import org.meanbean.test.ConfigurationBuilder;

import java.time.LocalDateTime;

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;


/**
 * @author M-VKU
 * @version 1.0
 */
public class DemoTaskTest
{

  @Test
  public void testDefaultState()
  {
    // when
    DemoTask demoTask = new DemoTask();
    // then
    assertNull(demoTask.getId());
    assertNull(demoTask.getTitle());
    assertNull(demoTask.getDemo());
    assertNull(demoTask.getKey());
    assertNull(demoTask.getLink());
    assertNull(demoTask.getOwner());
    assertNull(demoTask.getDrawDateTime());
    assertNull(demoTask.getWinner());
  }


  @Test
  public void testEqualsAndHashCodeContract()
  {
    EqualsVerifier
        .forClass(DemoTask.class)
        .suppress(ALL_FIELDS_SHOULD_BE_USED)
        .withPrefabValues(DemoTask.class, mock(DemoTask.class), mock(DemoTask.class))
        .verify();
  }


  @Test
  public void testToString()
  {
    ToStringVerifier
        .forClass(DemoTask.class)
        .withPreset(Presets.APACHE_TO_STRING_BUILDER_MULTI_LINE_STYLE)
        .withOnlyTheseFields("id", "key", "title")
        .withHashCode(false)
        .verify();
  }


  @Test
  public void testJavaBeanContract()
  {
    new BeanTester().testBean(
        DemoTask.class,
        new ConfigurationBuilder()
            .overrideFactory("drawDateTime", LocalDateTime::now)
            .overrideFactory("demo", Demo::new)
            .overrideFactory("owner", TeamMember::new)
            .overrideFactory("winner", TeamMember::new)
            .build());
  }


  @Test
  public void hasWinnerReturnsTrueIfWinnerIsDefined()
  {
    // given
    TeamMember teamMember = new TeamMember();
    DemoTask demoTask = new DemoTask();
    demoTask.setWinner(teamMember);
    // then
    assertThat(demoTask.hasWinner(), is(true));
  }


  @Test
  public void hasWinnerReturnsFalseIfWinnerIsNotDefined()
  {
    // given
    DemoTask demoTask = new DemoTask();
    demoTask.setWinner(null);
    // then
    assertThat(demoTask.hasWinner(), is(false));
  }
}
