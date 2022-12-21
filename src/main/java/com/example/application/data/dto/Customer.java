package com.example.application.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {


    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("first_name")
    @NotNull

    private String firstName;

    @JsonProperty("last_name")
    @NotNull

    private String lastName;
    @NotNull

    private Double balance;

    private String profile;

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEnabled() {
        return enabled;
    }
    @NotNull

    private String username;
    @NotNull

    private String password;
    @NotNull

    private RoleType role;
    private List<Account> accounts = new ArrayList<>();

    public boolean isNew() {
        return New;
    }

    public void setNew(boolean aNew) {
        New = aNew;
    }

    @JsonProperty("new")
    private boolean New;
    private boolean enabled;

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @NotNull


    private Certificates certificates = new Certificates();

    @NotNull

    private Double totalBalance;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public Certificates getCertificates() {
        return certificates;
    }

    public void setCertificates(Certificates certificates) {
        this.certificates = certificates;
    }

    public void setTotalBalance(Double totalBalance) {
        this.totalBalance = totalBalance;
    }


    public Double getTotalBalance() {
        Double b = this.accounts.stream().mapToDouble(Account::getBalance).sum();
        setBalance(b);
        return b;
    }
}
