package com.example.chef_in_home;

public class Request {
    private String requestId;
    private String chefId;
    private String userId;
    private String userName;
    private String userEmail;

    public Request() {
        // Default constructor required for calls to DataSnapshot.getValue(Request.class)
    }

    public Request(String requestId, String chefId, String userId, String userName, String userEmail) {
        this.requestId = requestId;
        this.chefId = chefId;
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getChefId() {
        return chefId;
    }

    public void setChefId(String chefId) {
        this.chefId = chefId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
