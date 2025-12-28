package entity;

import enums.OperationType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Transaction {
    private OperationType type;
    private double amount;
    private String fromAccountNumber;
    private String toAccountNumber;
    private LocalDateTime timestamp;
    private boolean success;
    private String message;

    public Transaction(OperationType type, double amount, String fromAccountNumber, 
                      String toAccountNumber, boolean success, String message) {
        this.type = type;
        this.amount = amount;
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.timestamp = LocalDateTime.now();
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    @Override
    public String toString() {
        if (type == OperationType.DEPOSIT) {
            return String.format("Transaction{type=%s, Deposit amount=%.2f, to=Account number'%s', " +
                            "timestamp=%s, success=%s, message='%s'}",
                    type, amount, toAccountNumber,
                    timestamp, success, message);
        }
        if (type == OperationType.WITHDRAW) {
            return String.format("Transaction{type=%s, Withdraw amount=%.2f, from=Account number'%s', " +
                            "timestamp=%s, success=%s, message='%s'}",
                    type, amount, fromAccountNumber,
                    timestamp, success, message);
        } else
            return String.format("Transaction{type=%s, amount=%.2f, from=Account number'%s', to=Account number'%s', " +
                           "timestamp=%s, success=%s, message='%s'}",
                           type, amount, fromAccountNumber, toAccountNumber,
                           timestamp, success, message);
    }
}

