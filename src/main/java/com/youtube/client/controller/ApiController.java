package com.youtube.client.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.youtube.client.model.YoutubeItem;
import com.youtube.client.service.YoutubeService;

@RestController
public class ApiController {

	YoutubeService service;
	
	@Autowired
	public ApiController(YoutubeService service) {
		this.service = service;
	}

	public String getChannel() throws GeneralSecurityException, IOException {
		return (service.getChannels());
	}
	
	@GetMapping("/channels")
	public YoutubeItem getYoutubeItem() throws GeneralSecurityException, IOException {
		
		String channelTitle = service.getChannels();
		
		YoutubeItem youtubeItem = new YoutubeItem(channelTitle);
		
		return youtubeItem;
	
	}

}
