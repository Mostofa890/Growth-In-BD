package com.pure.cash.mk;

public class InvestmentModel {
    private String id;
    private String userId;
    private long amount;
    private double rate;
    private int durationMonths;
    private long startDate;
    private long endDate;
    private String status;
    private long totalEarned;
    private double monthlyEarning;

    public InvestmentModel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public int getDurationMonths() { return durationMonths; }
    public void setDurationMonths(int durationMonths) { this.durationMonths = durationMonths; }

    public long getStartDate() { return startDate; }
    public void setStartDate(long startDate) { this.startDate = startDate; }

    public long getEndDate() { return endDate; }
    public void setEndDate(long endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTotalEarned() { return totalEarned; }
    public void setTotalEarned(long totalEarned) { this.totalEarned = totalEarned; }

    public double getMonthlyEarning() { return monthlyEarning; }
    public void setMonthlyEarning(double monthlyEarning) { this.monthlyEarning = monthlyEarning; }
}
