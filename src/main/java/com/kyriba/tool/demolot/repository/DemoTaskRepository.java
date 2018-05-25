/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/25/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.repository;

import com.kyriba.tool.demolot.domain.Demo;
import com.kyriba.tool.demolot.domain.DemoTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author M-VKU
 * @version 1.0
 */
@Repository
public interface DemoTaskRepository extends JpaRepository<DemoTask, Long>
{

  /**
   * Finds all the tasks of the given demo with the given key
   *
   * @param demo demo which the tasks is linked to
   * @param key task key
   * @return found team member (if any)
   */
  List<DemoTask> findByDemoAndKey(Demo demo, String key);

}