package com.security.contests.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ContestController {
	
	@GetMapping("/hello")
	public String test() {
		return "test";
	}

}
