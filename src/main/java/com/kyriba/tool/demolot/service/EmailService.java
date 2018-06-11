/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 6/11/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.service;

import com.kyriba.tool.demolot.domain.Demo;


/**
 * @author M-VKU
 * @version 1.0
 */
public interface EmailService
{

  /**
   * Send email notifications about the results of the given demo
   *
   * @param demo demo which results should be notified
   */
  void notifyDemoResults(Demo demo);

}