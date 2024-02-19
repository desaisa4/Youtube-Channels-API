package com.youtube.client.model;

import java.util.List;

import com.google.api.services.youtube.model.Channel;

public record YoutubeItem(List<Channel> channelTitle){}