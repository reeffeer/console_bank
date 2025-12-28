package entity;

public class Customer {
    private int id;
    private String fullName;
    private String passwordHash;

    public Customer(int id, String fullName, String passwordHash) {
        this.id = id;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public String toString() {
        return "Customer{id=" + id + ", fullName='" + fullName + "'}";
    }
}
