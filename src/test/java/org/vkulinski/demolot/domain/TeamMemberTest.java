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

import static nl.jqno.equalsverifier.Warning.ALL_FIELDS_SHOULD_BE_USED;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;


/**
 * @author M-VKU
 * @version 1.0
 */
public class TeamMemberTest
{

  @Test
  public void testDefaultState()
  {
    // when
    TeamMember teamMember = new TeamMember();
    // then
    assertNull(teamMember.getId());
    assertNull(teamMember.getName());
    assertNull(teamMember.getSurname());
    assertNull(teamMember.getEmail());
    assertThat(teamMember.isActive(), is(true));
  }


  @Test
  public void testEqualsAndHashCodeContract()
  {
    EqualsVerifier
        .forClass(TeamMember.class)
        .suppress(ALL_FIELDS_SHOULD_BE_USED)
        .verify();
  }


  @Test
  public void testToString()
  {
    ToStringVerifier
        .forClass(TeamMember.class)
        .withPreset(Presets.APACHE_TO_STRING_BUILDER_MULTI_LINE_STYLE)
        .withHashCode(false)
        .verify();
  }


  @Test
  public void testJavaBeanContract()
  {
    new BeanTester().testBean(TeamMember.class);
  }

}
