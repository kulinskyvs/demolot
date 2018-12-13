package org.vkulinski.demolot.service;

import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;


/**
 * @author M-VKU
 * @version 1.0
 */
public interface DemoDrawService
{

  boolean existsById(long id);

  Mono<Demo> getOne(long id);

  Flux<Demo> findAll();


  @Transactional
  Mono<Demo> submit(Demo toBeSubmitted);


  @Transactional
  Mono<Demo> submitTask(Demo demo, DemoTask toBeSubmitted);


  @Transactional
  Mono<Demo> deleteTask(Demo demo, long taskId);


  @Transactional
  Mono<Void> deleteById(long id);


  @Transactional
  Mono<Demo> startDraw(long id);

  @Transactional
  Mono<Demo> drawTasks(long id);


  @Transactional
  Mono<Demo> drawTask(long demoId, long taskId);

  @Transactional
  Mono<Demo> resetDraw(long demoId);
}