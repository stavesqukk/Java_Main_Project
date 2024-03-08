package org.example.service;

public class EventManager {

    private int managerId;
    private String managerName;
    private String username;
    private String password;

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EventManager(int managerId, String managerName, String username, String password) {
        this.managerId = managerId;
        this.managerName = managerName;
        this.username = username;
        this.password = password;
    }
}
