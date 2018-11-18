/********************************************************************************
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 * The content of this file is copyrighted by Kyriba Corporation and can not be *
 * reproduced, distributed, altered or used in any form, in whole or in part.   *
 * Date        Author  Changes                                                  *
 * 5/24/2018     M-VKU   Initial                                                  *
 * Copyright 2000 - 2018 Kyriba Corp. All Rights Reserved.                   *
 ********************************************************************************/
package com.kyriba.tool.demolot.web.controller;

import com.kyriba.tool.demolot.domain.TeamMember;
import com.kyriba.tool.demolot.repository.TeamMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Optional;

import static com.kyriba.tool.demolot.web.controller.ControllerConstants.*;
import static org.springframework.data.domain.Sort.Order.asc;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class TeamMemberController {
    private static final String ROOT_URL = "/teamembers";
    private static final String MODEL_MEMBER = "teamMember";
    private static final String VIEW_MEMBER = "teammember";

    @Autowired
    private TeamMemberRepository teamMemberRepository;


    @GetMapping(value = ROOT_URL)
    public Mono<String> showAllTeamMembers(Model model) {
        model.addAttribute(
                "members",
                Flux.fromIterable(teamMemberRepository.findAll(Sort.by(asc("id"))))
        );
        return withTeamRelatedPath("team");
    }


    @GetMapping(value = ROOT_URL + "/form")
    public Mono<String> showCreateForm(Model model) {
        model.addAttribute(MODEL_MEMBER, new TeamMember());
        model.addAttribute(MODEL_OPERATION, MODEL_OPERATION_CREATE);
        return withTeamRelatedPath(VIEW_MEMBER);
    }


    @GetMapping(value = ROOT_URL + "/{memberId}/form")
    public Mono<String> showEditForm(@PathVariable("memberId") final long memberId,
                                     Model model) {
        model.addAttribute(MODEL_MEMBER, teamMemberRepository.findById(memberId));
        model.addAttribute(MODEL_OPERATION, MODEL_OPERATION_EDIT);
        return withTeamRelatedPath(VIEW_MEMBER);
    }


    @PostMapping(value = ROOT_URL)
    public Mono<String> submitMember(@Valid TeamMember teamMember,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors() || isUniquenessViolated(teamMember, model)) {
            model.addAttribute(MODEL_OPERATION, ControllerConstants.modelOperation(teamMember));
            return withTeamRelatedPath(VIEW_MEMBER);
        } else {
            //save and go to the list of members
            teamMemberRepository.save(teamMember);
            return Mono.just("redirect:" + ROOT_URL);
        }
    }


    @DeleteMapping(value = ROOT_URL + "/{memberId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Mono<Void> deleteMember(@PathVariable("memberId") final long memberId) {
        //TODO: handle EmptyResultDataAccessException??
        teamMemberRepository.deleteById(memberId);
        return Mono.empty();
    }


    private boolean isUniquenessViolated(TeamMember member, Model model) {
//        TODO: refactor to reactive
        boolean isViolated = Optional
                .ofNullable(teamMemberRepository.findByNameAndSurname(member.getName(), member.getSurname()))
                .map(foundMember -> !foundMember.equalsById(member))
                .orElse(false);

        if (isViolated) {
            model.addAttribute("validationError", "A member with the same name and surname already exists");
        }
        return isViolated;
    }


    private static Mono<String> withTeamRelatedPath(String relativePath) {
        return Mono.just("team/" + relativePath);
    }
}