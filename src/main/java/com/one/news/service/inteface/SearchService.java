package com.one.news.service.inteface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.one.news.dto.NewsResultDTO;

public interface SearchService {
    NewsResultDTO getArticles(String topic, String to, String from, String sort) throws JsonProcessingException;
}
