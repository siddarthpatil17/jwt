package com.random.siddhu.model;

public class ActiveUserDTO {
    private int userId;
    private String email;
    private int companyId;

    public ActiveUserDTO(int userId, String email, int companyId) {
        this.userId = userId;
        this.email = email;
        this.companyId = companyId;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }
}
