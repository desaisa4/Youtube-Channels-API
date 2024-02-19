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

@Component
public class YoutubeService {
	
	public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	
	public static final String APPLICATION_NAME= "Youtube_API_APPLICAITION";
	
	public static final String RANDOM_CHANNEL_ID = "UC-lHJZR3Gqxm24_Vd_AJ5Yw";
	
	private static YouTube youtubeService;
	
	@Autowired
	private Environment env;
	
	public static String API_KEY;
	
	/**
	 * 
	 * Initializes and returns object that will mate Data requests to the YouTube API.
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
	
	public List<Channel> getChannels(String channelName) throws GeneralSecurityException, IOException{
		
		youtubeService = getService();
		API_KEY = env.getProperty("apikey");
		
		YouTube.Channels.List request = youtubeService.channels().list("snippet,contentDetails,statistics").setForUsername(channelName);
		
		request.setKey(API_KEY);
		
		ChannelListResponse response = request.execute();
		
		List<Channel> channels = response.getItems();
		
		return (channels);
	}

}
