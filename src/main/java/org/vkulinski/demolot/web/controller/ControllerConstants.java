package org.vkulinski.demolot.web.controller;

import org.vkulinski.demolot.domain.HasLongId;
import reactor.core.publisher.Mono;

import java.util.Objects;


/**
 * @author M-VKU
 * @version 1.0
 */
public final class ControllerConstants
{
  public static final String MODEL_OPERATION = "operation";
  public static final String MODEL_DEMO = "demo";
  public static final String MODEL_DEMO_TASKS = "demoTasks";
  public static final String MODEL_MEMBERS = "members";
  public static final String MODEL_DEMO_TASK = "demoTask";

  public static final String MODEL_OPERATION_CREATE = "Create";
  public static final String MODEL_OPERATION_EDIT = "Edit";

  public static final String URL_DEMOS_ROOT = "/demos";


  private ControllerConstants()
  {
  }


  public static String modelOperation(HasLongId entity)
  {
    return Objects.isNull(entity.getId()) ? MODEL_OPERATION_CREATE : MODEL_OPERATION_EDIT;
  }


  public static Mono<String> withDemoRelatedPath(String relativePath)
  {
    return Mono.just("demo/" + relativePath);
  }
}