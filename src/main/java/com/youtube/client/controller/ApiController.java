package com.youtube.client.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchListResponse;
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
	 * Returns the statistics for the channel specified by Username. If Username is not provided, statistics for the Youtube's own channel are provided.
	 * 
	 * @param channelName
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	@GetMapping("/channel")
	public YoutubeItem searchChannels(@RequestParam(value = "channelName") String channelName) throws GeneralSecurityException, IOException {
		
		if (channelName == null) {
			//TODO: Implement message: "channel name not provided"
			return null;
		}
		
		// Two approaches to retrieve Channel Name are required. 
		// First attempt to find channel object by Username.
		// If search by Username doesn't work, then we want to use the search functionality 
		// to find the Channel ID which can then be to the retrieve the channel object.
		// Second approach is more expensive as it uses more of API quota.
		
		// Approach 1
		Channel channel = service.getChannelByUsername(channelName);

		YoutubeItem youtubeItem = new YoutubeItem(channel);
		
		// Approach 2
		if (youtubeItem.channelTitle() == null) {
			System.out.println("Calling Alternate approach");
			Channel alternateResponse;
			alternateResponse = service.getChannelBySearch(channelName);
			youtubeItem = new YoutubeItem(alternateResponse);
		}
		
		// No Channel was found
		if (youtubeItem.channelTitle() == null) {
			//TODO: Implement message: "No Channel with this name was found"
			return null;
		}

		return youtubeItem;
	
	}
	
}