package entity;

public class CreditAccount extends Account {
    private double creditLimit;

    public CreditAccount(String accountNumber, Customer owner, double creditLimit) {
        super(accountNumber, owner);
        this.creditLimit = creditLimit;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        double currentBalance = getBalance();
        
        if (amount > currentBalance) {
            return false;
        }

        double newBalance = currentBalance - amount;
        setBalance(Math.max(0.0, newBalance));
        return true;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    @Override
    public String toString() {
        return "CreditAccount{accountNumber='" + getAccountNumber() + "', balance=" + getBalance() + 
               ", creditLimit=" + creditLimit + ", owner=" + getOwner().getFullName() + "}";
    }
}
