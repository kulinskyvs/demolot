package org.vkulinski.demolot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.FixedLocaleResolver;


@SpringBootApplication
public class DemolotApplication
{

  public static void main(String[] args)
  {
    SpringApplication.run(DemolotApplication.class, args);
  }


//  TODO: investigate how to use i18n with spwing web flux
//  @Bean
//  public LocaleResolver localeResolver()
//  {
//    return new FixedLocaleResolver(Locale.ENGLISH);
//  }
}