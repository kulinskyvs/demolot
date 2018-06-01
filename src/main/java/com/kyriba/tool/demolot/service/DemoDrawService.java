/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/28/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.service;

import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;

import javax.transaction.Transactional;
import java.util.List;


/**
 * @author M-VKU
 * @version 1.0
 */
public interface DemoDrawService
{

  boolean existsById(long id);

  Demo getOne(long id);

  List<Demo> findAll();


  @Transactional
  Demo submit(Demo toBeSubmitted);


  @Transactional
  Demo submitTask(Demo demo, DemoTask toBeSubmitted);


  @Transactional
  Demo deleteTask(Demo demo, long taskId);


  @Transactional
  void deleteById(long id);


  @Transactional
  Demo startDraw(long id);
}
