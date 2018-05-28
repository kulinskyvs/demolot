/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.controller;

import com.kyriba.tool.demolot.domain.HasLongId;

import java.util.Objects;


/**
 * @author M-VKU
 * @version 1.0
 */
public final class ControllerConstants
{
  public static final String MODEL_OPERATION = "operation";
  public static String MODEL_OPERATION_CREATE = "Create";
  public static String MODEL_OPERATION_EDIT = "Edit";


  private ControllerConstants()
  {
  }


  public static String modelOperation(HasLongId entity)
  {
    return Objects.isNull(entity.getId()) ? MODEL_OPERATION_CREATE : MODEL_OPERATION_EDIT;
  }
}