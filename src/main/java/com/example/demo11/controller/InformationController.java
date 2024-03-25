package com.example.demo11.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InformationController {

    @GetMapping("introduce")
    public String introduce(
    ) {
       return "/information/introduce";
    }

    @GetMapping("/information")
    public String information(
    ) {
        return "/information/information";
    }

    @GetMapping("/curriculum")
    public String curriculum(
    ) {
        return "/information/curriculum";
    }

    @GetMapping("/recruitment")
    public String recruitment(
    ) {
        return "/information/recruitment";
    }



}
