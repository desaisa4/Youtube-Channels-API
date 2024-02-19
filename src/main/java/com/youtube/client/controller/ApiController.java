package com.youtube.client.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.services.youtube.model.Channel;
import com.youtube.client.model.YoutubeItem;
import com.youtube.client.service.YoutubeService;

@RestController
public class ApiController {
	
	/**
	 * Service instance for the controller
	 */
	YoutubeService service;
	
	@Autowired
	public ApiController(YoutubeService service) {
		this.service = service;
	}

	/**
	 * Returns the statics for the channel specified by Username. If Username is not provided, statistics for the Youtube's own channel are provided.
	 * 
	 * @param channelName
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	@GetMapping("/channel")
	public YoutubeItem searchChannels(@RequestParam(value = "channelName", defaultValue = "Youtube") String channelName) throws GeneralSecurityException, IOException {
		
		List<Channel> channels = service.getChannels(channelName);
		
		YoutubeItem youtubeItem = new YoutubeItem(channels);
		
		return youtubeItem;
	
	}
	
}