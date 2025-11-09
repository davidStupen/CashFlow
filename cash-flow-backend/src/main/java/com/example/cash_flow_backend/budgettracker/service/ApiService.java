package com.example.cash_flow_backend.budgettracker.service;

import com.example.cash_flow_backend.budgettracker.model.dto.api.ExchangeRateDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Service
public class ApiService {
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public ApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    public ResponseEntity<?> apiEurCzk() throws JsonProcessingException {
        LocalDateTime dateTime = LocalDateTime.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formatted = dateTime.format(formatter);
        String url = "https://data.kurzy.cz/json/meny/b[6]den[" + formatted + "]cb[volat].js";
        String allJson = Objects.requireNonNull(this.restTemplate.getForObject(url, String.class));
        int indStart = allJson.indexOf("(") + 1;
        int indEnd = allJson.lastIndexOf(")");
        String json = allJson.substring(indStart, indEnd);
        ExchangeRateDTO exchangeRateDTO = this.objectMapper.readValue(json, ExchangeRateDTO.class);
        return new ResponseEntity<>(exchangeRateDTO.kurzy().EUR().dev_stred(), HttpStatus.OK);
    }
}
