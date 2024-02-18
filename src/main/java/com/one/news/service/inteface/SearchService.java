package com.one.news.service.inteface;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.one.news.dto.NewsResultDTO;
import com.one.news.dto.SourceDTO;
import com.one.news.dto.SourceResultDTO;

import java.util.List;

public interface SearchService {
    NewsResultDTO getArticles(String topic, String to, String from, String sort, String source) throws JsonProcessingException;

    SourceResultDTO getSources() throws JsonProcessingException;
}
