package org.vkulinski.demolot.repository;

import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
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