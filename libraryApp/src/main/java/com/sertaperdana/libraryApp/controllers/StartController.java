package com.sertaperdana.libraryApp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StartController {

    @GetMapping
    public String start() {
        return "start";
    }  //Calls templates/start.html

}