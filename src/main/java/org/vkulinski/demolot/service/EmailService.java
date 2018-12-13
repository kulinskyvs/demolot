package org.vkulinski.demolot.service;

import org.vkulinski.demolot.domain.Demo;


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
   * @param emailTemplate email template path
   */
  void notifyDemoResults(Demo demo, String emailTemplate);

}