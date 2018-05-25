/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/25/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.domain;

import java.util.Objects;


/**
 * Marker interface for all the entities that has surrogate identifier of the type {@link Long}
 *
 * @author M-VKU
 * @version 1.0
 */
public interface HasLongId
{
  Long getId();


  /**
   * Checks whether this entity equals by id of the given one
   *
   * @param entity entity to be compared
   * @return true in case they are "equal by id", false - otherwise
   */
  default <T extends HasLongId> boolean equalsById(T entity)
  {
    return Objects.nonNull(getId()) && Objects.equals(getId(), entity.getId());
  }
}