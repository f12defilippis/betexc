package com.bonde.betbot.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BetbotController {

	@RequestMapping("/")
    private String test(HttpServletRequest req) throws Exception{
		return "OK!";
    }

	
	
	
}
