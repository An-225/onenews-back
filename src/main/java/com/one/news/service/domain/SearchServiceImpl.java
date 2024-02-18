package com.one.news.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.news.dto.ArticleDTO;
import com.one.news.dto.NewsResultDTO;
import com.one.news.dto.SourceDTO;
import com.one.news.dto.SourceResultDTO;
import com.one.news.service.inteface.SearchService;
import com.one.news.util.RestUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    String apiKey = "8c94394235894df9be4e56065ce65d75";
    String newsHost = "https://newsapi.org/v2/everything";
    String sourceHost = "https://newsapi.org/v2/top-headlines/sources";


    @Override
    public NewsResultDTO getArticles(String topic, String to, String from, String sort, String source) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var query = URLEncoder.encode(topic, StandardCharsets.UTF_8);
        var queryString = new java.util.HashMap<>(Map.of(
                "q", query,
                "from", from,
                "to", to,
                "sortBy", sort,
                "language", "pt",
                "apiKey", apiKey
        ));

        if (source != null)
            queryString.put("sources", source);

        var restResponse = RestUtil.doRequest(newsHost, queryString, null, null, HttpMethod.GET, 20000);
        var newsResult = mapper.readValue(restResponse.getBody(), new TypeReference<NewsResultDTO>() {
        });
        List<ArticleDTO> filteredArticles = newsResult.getArticles().stream()
                .filter(article -> article.getTitle() != null && !article.getTitle().trim().isEmpty() && !"[Removed]".equals(article.getTitle()))
                .toList();
        newsResult.setArticles(filteredArticles);
        return newsResult;
    }

    public SourceResultDTO getSources() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var queryString = Map.of(
                "language", "pt",
                "apiKey", apiKey
        );

        var restResponse = RestUtil.doRequest(sourceHost, queryString, null, null, HttpMethod.GET, 20000);
        return mapper.readValue(restResponse.getBody(), new TypeReference<>() {
        });
    }
}
