package com.example.application.data.service;

import com.example.application.data.dto.Customer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
public class CustomerService {
    @Value("${api.url}")
    private String url;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RestTemplate restTemplate;
   public List<Customer> list() {
        List customers = Objects.requireNonNull(restTemplate.getForObject(url + "/customer/getByAll", List.class));

        return List.of(objectMapper.convertValue(customers, Customer[].class));
    }

    public Customer save(Customer customer) {
       return restTemplate.postForObject(url + "/customer/save", customer, Customer.class);
    }
    public void delete(Long id) {
        restTemplate.delete(url + "/customer/delete/" +id);
    }
    public Customer getById(Long id) {
        return restTemplate.getForObject(url + "/customer/getById/"+ id, Customer.class);
    }
    public Customer getByUsername(String username) {
        return restTemplate.postForObject(url + "/customer/load", username, Customer.class);
    }

}
