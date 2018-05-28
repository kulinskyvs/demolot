/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.domain;

/**
 * Represents the draw status of some particular {@link Demo}
 */
public enum DrawStatus
{
  PREPARATION("Preparation"),
  IN_PROGRESS("In_progress"),
  FINISHED("Finished");

  private String description;


  DrawStatus(String description)
  {
    this.description = description;
  }


  public String getDescription()
  {
    return description;
  }
}
