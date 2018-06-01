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
import com.kyriba.tool.demolot.domain.DrawStatus;
import com.kyriba.tool.demolot.repository.DemoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

import static org.springframework.data.domain.Sort.Order.desc;


/**
 * @author M-VKU
 * @version 1.0
 */
@Service
class DemoDrawServiceImpl implements DemoDrawService
{

  private final DemoRepository repository;


  public DemoDrawServiceImpl(DemoRepository repository)
  {
    this.repository = repository;
  }


  @Override
  public boolean existsById(long id)
  {
    return repository.existsById(id);
  }


  @Override
  public Demo getOne(long id)
  {
    return repository.getOne(id);
  }


  @Override
  public List<Demo> findAll()
  {
    return repository.findAll(Sort.by(desc("id")));
  }


  @Override
  public Demo submit(Demo toBeSubmitted)
  {
    Demo toBeSaved = Objects.nonNull(toBeSubmitted.getId()) ?
        validateSubmission(toBeSubmitted, foundDemo -> {
          foundDemo.setTitle(toBeSubmitted.getTitle());
          foundDemo.setDrawStatus(toBeSubmitted.getDrawStatus());
          foundDemo.setPlannedDate(toBeSubmitted.getPlannedDate());
          foundDemo.setSummary(toBeSubmitted.getSummary());
          foundDemo.setLink(toBeSubmitted.getLink());
        }) :
        toBeSubmitted;

    return repository.save(toBeSaved);
  }


  @Override
  public Demo submitTask(Demo demo, DemoTask toBeSubmitted)
  {
    Demo toBeSaved = validateSubmission(demo,
        validDemo -> {
          if (Objects.nonNull(toBeSubmitted.getId())) {
            //update the task
            DemoTask taskFromDemo = validDemo.getTaskById(toBeSubmitted.getId());
            taskFromDemo.setKey(toBeSubmitted.getKey());
            taskFromDemo.setTitle(toBeSubmitted.getTitle());
            taskFromDemo.setLink(toBeSubmitted.getLink());
            taskFromDemo.setOwner(toBeSubmitted.getOwner());
          }
          else {
            //add new task
            validDemo.getTasks().add(toBeSubmitted);
            toBeSubmitted.setDemo(validDemo);
          }
        });

    return repository.save(toBeSaved);
  }


  @Override
  public Demo deleteTask(Demo demo, long taskId)
  {
    Demo toBeSaved = validateSubmission(demo,
        validDemo -> {
          DemoTask demoTask = validDemo.getTaskById(taskId);
          validDemo.getTasks().remove(demoTask);
        });

    return repository.save(toBeSaved);
  }


  @Override
  public void deleteById(long id)
  {
    Demo loadedDemo = repository.getOne(id);
    if (DrawStatus.PREPARATION == loadedDemo.getDrawStatus()) {
      repository.deleteById(id);
    }
    else {
      throw new IllegalStateException(
          "Unable to delete demo in status '" + loadedDemo.getDrawStatus().getDescription() + "'");
    }
  }


  @Override
  public Demo startDraw(long id)
  {
    Demo demo = getOne(id);
    if (demo.getDrawStatus().equals(DrawStatus.PREPARATION)) {
      demo.setDrawStatus(DrawStatus.IN_PROGRESS);
      demo = repository.save(demo);
    }
    return demo;
  }


  private Demo validateSubmission(Demo submissionCandidate, Consumer<Demo> submitter)
  {

    Optional<Demo> loadedDemo = repository.findById(submissionCandidate.getId());
    if (loadedDemo.isPresent()) {
      Demo foundDemo = loadedDemo.get();
      if (DrawStatus.PREPARATION != foundDemo.getDrawStatus()) {
        throw new IllegalStateException(
            "Unable to submut demo in status '" + foundDemo.getDrawStatus().getDescription() + "'");
      }
      submitter.accept(foundDemo);
      return foundDemo;
    }
    else {
      throw new IllegalStateException("Unable to find demo with id = " + submissionCandidate.getId());
    }
  }
}
