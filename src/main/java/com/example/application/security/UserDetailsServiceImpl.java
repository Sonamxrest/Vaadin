package com.example.application.security;

import com.example.application.data.dto.Customer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${api.url}")
    private String url;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        Object user =
                new
                        RestTemplate().
                        postForEntity
                                (url + "/customer/load",username, Object.class).getBody();
        if (user == null) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            Customer customer = objectMapper.convertValue(user, Customer.class);
            return new org.springframework.security.core.userdetails.User( customer.getUsername(), customer.getPassword(),
                    getAuthorities(customer));
        }
    }

    private static List<GrantedAuthority> getAuthorities(Customer user) {
        return new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().label)));

    }

}
