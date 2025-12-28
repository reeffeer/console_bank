package entity;

public abstract class Account {
    private String accountNumber;
    private double balance;
    private Customer owner;

    public Account(String accountNumber, Customer owner) {
        this.accountNumber = accountNumber;
        this.owner = owner;
        this.balance = 0.0;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            return false;
        }
        balance += amount;
        return true;
    }

    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        if (balance < amount) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public boolean transfer(Account to, double amount) {
        if (amount <= 0) {
            return false;
        }
        if (this.withdraw(amount)) {
            if (to.deposit(amount)) {
                return true;
            } else {
                this.deposit(amount);
                return false;
            }
        }
        return false;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public Customer getOwner() {
        return owner;
    }

    protected void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{accountNumber='" + accountNumber + "', balance=" + balance + 
               ", owner=" + owner.getFullName() + "}";
    }
}
