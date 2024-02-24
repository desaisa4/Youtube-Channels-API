package com.youtube.client.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

@Component
public class YoutubeService {
	
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	public static final String APPLICATION_NAME= "Youtube_API_APPLICAITION";
	
	private static YouTube youtubeService;
	
	@Autowired
	private static Environment env;
	
	public static final String API_KEY = env.getProperty("apikey");;
	
	/**
	 * Initializes and returns object that will make Data requests to the YouTube API.
	 * 
	 * @return
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static YouTube getService() throws GeneralSecurityException, IOException {
		final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		
		YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				new HttpRequestInitializer() {
	          		public void initialize(HttpRequest request) throws IOException {
	          			//We don't care for this
	          		}
	          	}
			).setApplicationName(APPLICATION_NAME).build();

		return youtube;
		
	}
	
	/**
	 * Makes a call to the Youtube API to find the channel by Username. In most cases the channel name is also the Username.
	 * 
	 * @param channelName : Channel name provided by the API call.
	 * @return Channel object with some statistics about the channel
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public Channel getChannelByUsername(String channelName) throws GeneralSecurityException, IOException{
		
		youtubeService = getService();
		
		YouTube.Channels.List request = youtubeService.channels().list("snippet,contentDetails,statistics");
		
		request.setKey(API_KEY);
		
		ChannelListResponse response = request.setForUsername(channelName).setMaxResults((long) 1).execute();
		
		// We only have one result so get the first one.
		Channel channel = response.getItems().get(0);
		
		return (channel);
	}
	
	/**
	 * Makes a call to the Youtube API to search using the Q parameter. This can return various types of objects including Videos and Channels.
	 * Given we are searching by using the channel name, the first object returned by the Youtube API is always a channel. After we check that is the case
	 * We can use the unique ID associated by that channel and get the actual Channel object.
	 * 
	 * @param channelName : Channel name provided by the API call.
	 * @return Channel object with some statistics about the channel
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public Channel getChannelBySearch(String channelName) throws GeneralSecurityException, IOException {
		
		youtubeService = getService();
		
		YouTube.Search.List request = youtubeService.search().list("snippet");
		
		request.setKey(API_KEY);
		
		request.setType("channel");
		
		// Only grabs the first result. If the channel name is correct then this will be a channel.
		SearchListResponse response = request.setMaxResults(25L).setQ(channelName).setMaxResults((long) 1).execute();
		
		String channelId = null;
		
		// Store the ID of the result we found. This is not the same as the Channel ID.
		ResourceId resourceId = response.getItems().get(0).getId();
		
		// Check if the result is a Channel. If so get its Channel ID
		if(resourceId.getKind().equals("youtube#channel")) {
			channelId = resourceId.getChannelId();
		}
		
		// Make a call to get Channel object by Channel ID
		if (channelId!=null) {
			Channel channel = getChannelById(channelId);
			return channel;
		}
		
		return null;
	}
	
	/**
	 * Makes a call to the Youtube API to find the Channel object by Channel ID. This will always return a channel as long as a correct Channel ID is provided.
	 * @param channelId
	 * @return Returns a Channel Object
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	private Channel getChannelById (String channelId) throws GeneralSecurityException, IOException {
		
		youtubeService = getService();
		
		YouTube.Channels.List request = youtubeService.channels().list("snippet,contentDetails,statistics");
		
		request.setKey(API_KEY);
		
		ChannelListResponse response = request.setId(channelId).setMaxResults((long) 1).execute();
		
		Channel channel = response.getItems().get(0);
		return channel;
		
	}

}
