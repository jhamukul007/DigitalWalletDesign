package com.dwp.models;

public class User extends BaseEntity{
    private Long id;
    private String name;
    private String emailId;
    private DigitalWallet digitalWallet;
    public User(Long id, String name, String emailId) {
        this.id = id;
        this.name = name;
        this.emailId = emailId;
        this.digitalWallet = new DigitalWallet();
    }
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DigitalWallet getDigitalWallet() {
        return digitalWallet;
    }

    public void setDigitalWallet(DigitalWallet digitalWallet) {
        this.digitalWallet = digitalWallet;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", emailId='" + emailId + '\'' +
                ", digitalWallet=" + digitalWallet +
                '}';
    }
}
