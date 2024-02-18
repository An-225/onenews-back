package com.one.news.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.one.news.dto.NewsResultDTO;
import com.one.news.dto.SourceResultDTO;
import com.one.news.enumeration.ConstantsAPI;
import com.one.news.service.inteface.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(ConstantsAPI.API)
public class SearchController {
    private static final Logger log = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<NewsResultDTO> getArticlesByTopic(
            @RequestParam String topic,
            @RequestParam String to,
            @RequestParam String from,
            @RequestParam(required = false, defaultValue = "popularity") String sort,
            @RequestParam(required = false) String source
    ) throws JsonProcessingException {
        log.info("get articles by topic: {}", topic);
        var articles = searchService.getArticles(topic, to, from, sort, source);
        return new ResponseEntity<>(articles, HttpStatus.OK);
    }

    @GetMapping("sources")
    public ResponseEntity<SourceResultDTO> getSources() throws JsonProcessingException {
        return new ResponseEntity<>(searchService.getSources(), HttpStatus.OK);
    }
}
