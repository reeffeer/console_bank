package database;

import entity.Account;
import entity.Customer;
import entity.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryDatabase {
    private static InMemoryDatabase instance;
    
    private List<Customer> customers;
    private List<Account> accounts;
    private List<Transaction> transactions;
    private Map<String, Customer> customersByFullName;
    private int nextCustomerId;
    private int nextAccountNumber;

    private InMemoryDatabase() {
        this.customers = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.transactions = new ArrayList<>();
        this.customersByFullName = new HashMap<>();
        this.nextCustomerId = 1;
        this.nextAccountNumber = 1;
    }

    public static synchronized InMemoryDatabase getInstance() {
        if (instance == null) {
            instance = new InMemoryDatabase();
        }
        return instance;
    }

    public Customer addCustomer(Customer customer) {
        customers.add(customer);
        customersByFullName.put(customer.getFullName(), customer);
        return customer;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public List<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public Customer findCustomerByFullName(String fullName) {
        return customersByFullName.get(fullName);
    }

    public Customer findCustomerById(int id) {
        return customers.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Account findAccountByNumber(String accountNumber) {
        return accounts.stream()
                .filter(a -> a.getAccountNumber().equals(accountNumber))
                .findFirst()
                .orElse(null);
    }

    public List<Account> findAccountsByCustomerId(int customerId) {
        List<Account> customerAccounts = new ArrayList<>();
        for (Account account : accounts) {
            if (account.getOwner().getId() == customerId) {
                customerAccounts.add(account);
            }
        }
        return customerAccounts;
    }

    public int getNextCustomerId() {
        return nextCustomerId++;
    }

    public String getNextAccountNumber() {
        String accountNumber;
        do {
            accountNumber = String.format("%010d", nextAccountNumber++);
        } while (findAccountByNumber(accountNumber) != null);
        
        return accountNumber;
    }

    public Transaction getLastTransaction() {
        if (transactions.isEmpty()) {
            return null;
        }
        return transactions.get(transactions.size() - 1);
    }
}

