package com.one.news.service.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.one.news.dto.ArticleDTO;
import com.one.news.dto.NewsResultDTO;
import com.one.news.service.inteface.SearchService;
import com.one.news.util.RestUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {
    String apiKey = "8c94394235894df9be4e56065ce65d75";
    String newsHost = "https://newsapi.org/v2/everything";

    @Override
    public NewsResultDTO getArticles(String topic, String to, String from) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String apiNewsUrl = newsHost + "?" +
                            "q=" + topic +
                            "&from=" + from +
                            "&to=" + to +
                            "&language=pt" +
                            "&sortBy=popularity" +
                            "&apiKey=" + apiKey;
        var restResponse = RestUtil.doRequest(apiNewsUrl, null, null, null, HttpMethod.GET, 20000);
        var newsResult = mapper.readValue(restResponse.getBody(), new TypeReference<NewsResultDTO>() {});
        List<ArticleDTO> filteredArticles = newsResult.getArticles().stream()
                .filter(article -> article.getTitle() != null && !article.getTitle().trim().isEmpty() && !"[Removed]".equals(article.getTitle()))
                .collect(Collectors.toList());
        newsResult.setArticles(filteredArticles);
        return newsResult;
    }
}
