package com.pure.cash.mk;

public class UserModel {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private long balance;
    private long totalInvested;
    private long totalEarned;
    private long createdAt;

    public UserModel() {}

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public long getBalance() { return balance; }
    public void setBalance(long balance) { this.balance = balance; }

    public long getTotalInvested() { return totalInvested; }
    public void setTotalInvested(long totalInvested) { this.totalInvested = totalInvested; }

    public long getTotalEarned() { return totalEarned; }
    public void setTotalEarned(long totalEarned) { this.totalEarned = totalEarned; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
