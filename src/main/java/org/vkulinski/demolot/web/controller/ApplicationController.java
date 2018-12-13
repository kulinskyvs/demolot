package org.vkulinski.demolot.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import reactor.core.publisher.Mono;


/**
 * @author M-VKU
 * @version 1.0
 */
@Controller
public class ApplicationController {

    @RequestMapping("/")
    Mono<String> home() {
        return Mono.just("index");
    }
}