/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.controller;

import com.kyriba.tool.demolot.domain.DrawStatus;

import java.beans.PropertyEditorSupport;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


/**
 * @author M-VKU
 * @version 1.0
 */
public class DrawStatusPropertyEditor extends PropertyEditorSupport
{

  @Override
  public String getAsText()
  {
    return Optional
        .ofNullable(getValue())
        .map(status -> ((DrawStatus) status).getDescription())
        .orElse(null);
  }


  @Override
  public void setAsText(String text) throws IllegalArgumentException
  {
    setValue(Stream
        .of(DrawStatus.values())
        .filter(status -> Objects.equals(text, status.getDescription()))
        .findFirst()
        .orElse(null));
  }

}