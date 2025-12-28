import database.InMemoryDatabase;
import entity.Account;
import entity.CreditAccount;
import entity.Customer;
import entity.DebitAccount;
import entity.Transaction;
import enums.OperationType;

import java.util.List;

public class Bank {
    private InMemoryDatabase database;

    public Bank() {
        this.database = InMemoryDatabase.getInstance();
    }

    public Customer createCustomer(String fullName, String passwordHash) {
        int id = database.getNextCustomerId();
        Customer customer = new Customer(id, fullName, passwordHash);
        database.addCustomer(customer);
        return customer;
    }

    public Account openDebitAccount(Customer owner) {
        String accountNumber = database.getNextAccountNumber();
        Account account = new DebitAccount(accountNumber, owner);
        database.addAccount(account);
        return account;
    }

    public Account openCreditAccount(Customer owner, double creditLimit) {
        String accountNumber = database.getNextAccountNumber();
        Account account = new CreditAccount(accountNumber, owner, creditLimit);
        database.addAccount(account);
        return account;
    }

    public Account findAccount(String accountNumber) {
        return database.findAccountByNumber(accountNumber);
    }

    public boolean deposit(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            database.addTransaction(new Transaction(OperationType.DEPOSIT, amount, null, 
                    accountNumber, false, "Счёт не найден"));
            return false;
        }

        boolean success = account.deposit(amount);
        String message = success ? "OK" : "Неверная сумма";
        database.addTransaction(new Transaction(OperationType.DEPOSIT, amount, null, 
                accountNumber, success, message));
        return success;
    }

    public boolean withdraw(String accountNumber, double amount) {
        Account account = findAccount(accountNumber);
        if (account == null) {
            database.addTransaction(new Transaction(OperationType.WITHDRAW, amount, 
                    accountNumber, null, false, "Счёт не найден"));
            return false;
        }

        boolean success = account.withdraw(amount);
        String message = success ? "OK" : "Недостаточно средств или неверная сумма";
        database.addTransaction(new Transaction(OperationType.WITHDRAW, amount, 
                accountNumber, null, success, message));
        return success;
    }

    public boolean transfer(String from, String to, double amount) {
        if (from.equals(to)) {
            database.addTransaction(new Transaction(OperationType.TRANSFER, amount, from, to, 
                    false, "Счет отправителя и счет получателя не могут быть одинаковыми"));
            return false;
        }

        Account fromAccount = findAccount(from);
        Account toAccount = findAccount(to);

        if (fromAccount == null) {
            database.addTransaction(new Transaction(OperationType.TRANSFER, amount, from, to, 
                    false, "Счёт отправителя не найден"));
            return false;
        }

        if (toAccount == null) {
            database.addTransaction(new Transaction(OperationType.TRANSFER, amount, from, to, 
                    false, "Счёт получателя не найден"));
            return false;
        }

        boolean success = fromAccount.transfer(toAccount, amount);
        String message = success ? "OK" : "Недостаточно средств или неверная сумма";
        database.addTransaction(new Transaction(OperationType.TRANSFER, amount, from, to, 
                success, message));
        return success;
    }

    public void printCustomerAccounts(int customerId) {
        List<Account> accounts = database.findAccountsByCustomerId(customerId);
        if (accounts.isEmpty()) {
            System.out.println("У клиента нет счетов.");
            return;
        }

        System.out.println("\n=== Счета клиента (ID: " + customerId + ") ===");
        for (Account account : accounts) {
            System.out.println(account);
        }
        System.out.println();
    }

    public void printTransactions() {
        List<Transaction> transactions = database.getTransactions();
        if (transactions.isEmpty()) {
            System.out.println("Нет транзакций.");
            return;
        }

        System.out.println("\n=== История транзакций ===");
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
        System.out.println();
    }

    public String getLastTransactionMessage() {
        Transaction lastTransaction = database.getLastTransaction();
        if (lastTransaction == null) {
            return null;
        }
        return lastTransaction.getMessage();
    }

    public Transaction getLastTransaction() {
        return database.getLastTransaction();
    }

    public void printReport() {
        List<Account> accounts = database.getAccounts();
        List<Transaction> transactions = database.getTransactions();

        int debitCount = 0;
        int creditCount = 0;
        double debitBalance = 0.0;
        double creditBalance = 0.0;
        int successCount = 0;
        int failCount = 0;

        for (Account account : accounts) {
            if (account instanceof DebitAccount) {
                debitCount++;
                debitBalance += account.getBalance();
            } else if (account instanceof CreditAccount) {
                creditCount++;
                creditBalance += account.getBalance();
            }
        }

        for (Transaction transaction : transactions) {
            if (transaction.isSuccess()) {
                successCount++;
            } else {
                failCount++;
            }
        }

        System.out.println("\n=== Отчёт банка ===");
        System.out.println("Дебетовые счета:");
        System.out.println("  Количество: " + debitCount);
        System.out.println("  Суммарный баланс: " + String.format("%.2f", debitBalance));
        System.out.println("Кредитные счета:");
        System.out.println("  Количество: " + creditCount);
        System.out.println("  Суммарный баланс: " + String.format("%.2f", creditBalance));
        System.out.println("Транзакции:");
        System.out.println("  Успешных: " + successCount);
        System.out.println("  Неуспешных: " + failCount);
        System.out.println();
    }
}
