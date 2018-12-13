package org.vkulinski.demolot.repository;

import org.vkulinski.demolot.domain.Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author M-VKU
 * @version 1.0
 */
@Repository
public interface DemoRepository extends JpaRepository<Demo, Long>
{
}