package com.example.application.data.service;

import com.example.application.data.dto.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Objects;
@Service

public class AccountService {
    @Value("${api.url}")
    private String url;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
    public List<Account> list() {
        List Accounts = Objects.requireNonNull(restTemplate.getForObject(url + "/account/getByAll", List.class));

        return List.of(objectMapper.convertValue(Accounts, Account[].class));
    }

    public Account save(Account Account) {
        return restTemplate.postForObject(url + "/account/save", Account, Account.class);
    }
    public void delete(Long id) {
        restTemplate.delete(url + "/account/delete/" +id);
    }
    public Account getById(Long id) {
        return restTemplate.getForObject(url + "/account/getById/"+ id, Account.class);
    }
    public List<Account> save(List<Account> Account) {
        return List.of(Objects.requireNonNull(restTemplate.postForObject(url + "/account/saveAll", Account, Account[].class)));
    }

    public List<Account> filter(Map<String,String> filterParams) {
        return List.of(Objects.requireNonNull(restTemplate.postForObject(url + "/account/filter", filterParams, Account[].class)));
    }

}
