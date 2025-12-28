package entity;

public class DebitAccount extends Account {
    public DebitAccount(String accountNumber, Customer owner) {
        super(accountNumber, owner);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            return false;
        }
        double currentBalance = getBalance();
        if (currentBalance < amount) {
            return false;
        }
        setBalance(currentBalance - amount);
        return true;
    }
}
