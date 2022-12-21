package com.example.application.data.service;

import com.example.application.data.dto.Bank;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
@Service
public class BankService {
    @Value("${api.url}")
    private String url;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
    public List<Bank> list() {
        List Banks = Objects.requireNonNull(restTemplate.getForObject(url + "/bank/getByAll", List.class));

        return List.of(objectMapper.convertValue(Banks, Bank[].class));
    }

    public Bank save(Bank Bank) {
        return restTemplate.postForObject(url + "/bank/save", Bank, Bank.class);
    }
    public void delete(Long id) {
        restTemplate.delete(url + "/bank/delete/" +id);
    }
    public Bank getById(Long id) {
        return restTemplate.getForObject(url + "/bank/getById/"+ id, Bank.class);
    }
}
