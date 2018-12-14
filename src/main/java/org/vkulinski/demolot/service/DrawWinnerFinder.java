package org.vkulinski.demolot.service;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.vkulinski.demolot.domain.Demo;
import org.vkulinski.demolot.domain.DemoTask;
import org.vkulinski.demolot.domain.TeamMember;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author M-VKU
 * @version 1.0
 */
public class DrawWinnerFinder
{
  private final RandomIntGenerator generator;
  private final List<TeamMember> fromMembers;
  private final Multiset<Long> foundWinners = HashMultiset.create();


  public DrawWinnerFinder(Demo demo,
                          List<TeamMember> fromMembers)
  {
    this(
        demo,
        fromMembers,
        new SecureRandom()::nextInt
    );
  }


  DrawWinnerFinder(Demo demo,
                   List<TeamMember> fromMembers,
                   RandomIntGenerator generator)
  {
    this.fromMembers = fromMembers;
    this.generator = generator;
    demo.getTasks().stream().filter(DemoTask::hasWinner).forEach(task -> foundWinners.add(task.getWinner().getId()));
  }


  public TeamMember find(DemoTask task)
  {
    List<TeamMember> members = fromMembers
        .stream()
        .filter(member -> !member.equalsById(task.getOwner()))
        .collect(Collectors.toList());

    return members.isEmpty() ? task.getOwner() : find(members);
  }


  private TeamMember find(List<TeamMember> members)
  {
    if (1 == members.size()) {
      return members.get(0);
    }

    TeamMember winnerCandidate = members.get(generator.nextInt(members.size()));

    if (members
        .stream()
        .anyMatch(member -> foundWinners.count(member.getId()) < foundWinners.count(winnerCandidate.getId()))) {
      //there are winners that won less tasks than the given candidate
      //eliminate this candidate from members and repeat the process
      return find(members
          .stream()
          .filter(member -> !member.equalsById(winnerCandidate))
          .collect(Collectors.toList())
      );
    }
    else {
      //the given winner candidate is the good one
      foundWinners.add(winnerCandidate.getId());
      return winnerCandidate;
    }
  }


  static interface RandomIntGenerator
  {
    int nextInt(int max);
  }
}