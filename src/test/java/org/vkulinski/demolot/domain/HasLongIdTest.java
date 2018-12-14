/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 12/14/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package org.vkulinski.demolot.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author M-VKU
 * @version 1.0
 */
@RunWith(MockitoJUnitRunner.class)
public class HasLongIdTest
{
  private HasLongId objNullId = new FakeObject(null);
  private HasLongId objId1 = new FakeObject(1L);
  private HasLongId objId2 = new FakeObject(2L);


  @Test
  public void equalsByIdForObjectWithIdNotDefined()
  {
    // given
    HasLongId obj = new FakeObject(null);
    // then
    assertThat(obj.equalsById(objNullId), is(false));
    assertThat(obj.equalsById(objId1), is(false));
    assertThat(obj.equalsById(objId2), is(false));
  }


  @Test
  public void equalsByIdForObjectWithIdDefined()
  {
    // given
    HasLongId obj = new FakeObject(1L);
    // then
    assertThat(obj.equalsById(objNullId), is(false));
    assertThat(obj.equalsById(objId1), is(true));
    assertThat(obj.equalsById(objId2), is(false));
  }


  private class FakeObject implements HasLongId
  {
    public FakeObject(Long id)
    {
      this.id = id;
    }


    public Long id;


    @Override
    public Long getId()
    {
      return id;
    }
  }
}
