package org.vkulinski.demolot.repository;

import org.vkulinski.demolot.domain.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long>
{

  /**
   * Finds either all the <b>active</b> or all <b>non active</b> team members
   *
   * @param active denotes whether active only team members should be returned
   * @return list of team members
   */
  List<TeamMember> findByActive(boolean active);


  /**
   * Finds all the active team members
   *
   * @return list of active team members
   */
  default List<TeamMember> findActiveOnly()
  {
    return findByActive(true);
  }


  /**
   * Finds a team member by his name and surname
   *
   * @param name member name
   * @param surName member surname
   * @return found team member (if any)
   */
  TeamMember findByNameAndSurname(String name, String surName);
}