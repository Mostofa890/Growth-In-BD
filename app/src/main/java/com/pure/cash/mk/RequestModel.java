package com.pure.cash.mk;

public class RequestModel {
    private String requestId;
    private String userId;
    private long amount;
    private String method;
    private String txnId;
    private String number;
    private String status;
    private long time;
    private String type;

    public RequestModel() {}

    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public long getAmount() { return amount; }
    public void setAmount(long amount) { this.amount = amount; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getTxnId() { return txnId; }
    public void setTxnId(String txnId) { this.txnId = txnId; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public long getTime() { return time; }
    public void setTime(long time) { this.time = time; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
