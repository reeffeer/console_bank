import database.InMemoryDatabase;
import entity.Account;
import entity.Customer;
import util.PasswordUtil;
import java.util.Scanner;

public class Menu {
    private Bank bank;
    private Scanner scanner;
    private Customer currentCustomer;

    public Menu() {
        this.bank = new Bank();
        this.scanner = new Scanner(System.in);
        this.currentCustomer = null;
    }

    public void run() {
        boolean running = true;
        while (running) {
            if (currentCustomer == null) {
                running = showAuthMenu();
            } else {
                running = showMainMenu();
            }
        }
        System.out.println("До свидания!");
    }

    private boolean showAuthMenu() {
        System.out.println("\n=== Банковская система ===");
        System.out.println("1. Регистрация");
        System.out.println("2. Авторизация");
        System.out.println("3. Выход");
        System.out.print("Выберите действие: ");

        int choice = readInt();
        switch (choice) {
            case 1:
                register();
                break;
            case 2:
                login();
                break;
            case 3:
                return false;
            default:
                System.out.println("Неверный выбор!");
        }
        return true;
    }

    private boolean showMainMenu() {
        System.out.println("\n=== Главное меню ===");
        System.out.println("Пользователь: " + currentCustomer.getFullName() + " (ID: " + currentCustomer.getId() + ")");
        System.out.println("1. Открыть дебетовый счёт");
        System.out.println("2. Открыть кредитный счёт");
        System.out.println("3. Пополнить счёт");
        System.out.println("4. Снять со счёта");
        System.out.println("5. Перевести средства");
        System.out.println("6. Показать мои счета");
        System.out.println("7. Показать транзакции");
        System.out.println("8. Отчёт банка");
        System.out.println("9. Выйти");
        System.out.print("Выберите действие: ");

        int choice = readInt();
        switch (choice) {
            case 1:
                openDebitAccount();
                break;
            case 2:
                openCreditAccount();
                break;
            case 3:
                deposit();
                break;
            case 4:
                withdraw();
                break;
            case 5:
                transfer();
                break;
            case 6:
                bank.printCustomerAccounts(currentCustomer.getId());
                break;
            case 7:
                bank.printTransactions();
                break;
            case 8:
                bank.printReport();
                break;
            case 9:
                currentCustomer = null;
                break;
            default:
                System.out.println("Неверный выбор!");
        }
        return true;
    }

    private void register() {
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine().trim();
        
        if (fullName.isEmpty()) {
            System.out.println("Имя не может быть пустым!");
            return;
        }

        InMemoryDatabase db = InMemoryDatabase.getInstance();
        if (db.findCustomerByFullName(fullName) != null) {
            System.out.println("Пользователь с таким именем уже существует!");
            return;
        }

        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();
        
        if (password.isEmpty()) {
            System.out.println("Пароль не может быть пустым!");
            return;
        }

        String passwordHash = PasswordUtil.hashPassword(password);
        Customer customer = bank.createCustomer(fullName, passwordHash);
        System.out.println("Регистрация успешна! Ваш ID: " + customer.getId());
    }

    private void login() {
        System.out.print("Введите полное имя: ");
        String fullName = scanner.nextLine().trim();
        
        System.out.print("Введите пароль: ");
        String password = scanner.nextLine();

        InMemoryDatabase db = InMemoryDatabase.getInstance();
        Customer customer = db.findCustomerByFullName(fullName);
        
        if (customer == null) {
            System.out.println("Пользователь не найден!");
            return;
        }

        if (PasswordUtil.checkPassword(password, customer.getPasswordHash())) {
            currentCustomer = customer;
            System.out.println("Авторизация успешна!");
        } else {
            System.out.println("Неверный пароль!");
        }
    }

    private void openDebitAccount() {
        Account account = bank.openDebitAccount(currentCustomer);
        System.out.println("Дебетовый счёт открыт! Номер счёта: " + account.getAccountNumber());
    }

    private void openCreditAccount() {
        System.out.print("Введите кредитный лимит: ");
        double creditLimit = readDouble();
        
        if (creditLimit <= 0) {
            System.out.println("Кредитный лимит должен быть больше 0!");
            return;
        }

        Account account = bank.openCreditAccount(currentCustomer, creditLimit);
        System.out.println("Кредитный счёт открыт! Номер счёта: " + account.getAccountNumber() + 
                          ", лимит: " + creditLimit);
    }

    private void deposit() {
        System.out.println("(Для отмены введите '0' на любом этапе)");
        System.out.print("Введите номер счёта: ");
        String accountNumber = readStringWithCancel();
        if (accountNumber == null) {
            System.out.println("Операция отменена.");
            return;
        }
        
        System.out.print("Введите сумму: ");
        Double amount = readDoubleWithCancel();
        if (amount == null) {
            System.out.println("Операция отменена.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Ошибка: неверная сумма!");
            return;
        }
        
        if (bank.deposit(accountNumber, amount)) {
            System.out.println("Средства успешно внесены!");
        } else {
            String errorMessage = bank.getLastTransactionMessage();
            System.out.println("Ошибка при внесении средств! Причина: " + errorMessage);
        }
    }

    private void withdraw() {
        System.out.println("(Для отмены введите '0' на любом этапе)");
        System.out.print("Введите номер счёта: ");
        String accountNumber = readStringWithCancel();
        if (accountNumber == null) {
            System.out.println("Операция отменена.");
            return;
        }
        
        System.out.print("Введите сумму: ");
        Double amount = readDoubleWithCancel();
        if (amount == null) {
            System.out.println("Операция отменена.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Ошибка: неверная сумма!");
            return;
        }
        
        if (bank.withdraw(accountNumber, amount)) {
            System.out.println("Средства успешно сняты!");
        } else {
            String errorMessage = bank.getLastTransactionMessage();
            System.out.println("Ошибка при снятии средств! Причина: " + errorMessage);
        }
    }

    private void transfer() {
        System.out.println("(Для отмены введите '0' на любом этапе)");
        System.out.print("Введите номер счёта отправителя: ");
        String from = readStringWithCancel();
        if (from == null) {
            System.out.println("Операция отменена.");
            return;
        }
        
        System.out.print("Введите номер счёта получателя: ");
        String to = readStringWithCancel();
        if (to == null) {
            System.out.println("Операция отменена.");
            return;
        }
        
        System.out.print("Введите сумму: ");
        Double amount = readDoubleWithCancel();
        if (amount == null) {
            System.out.println("Операция отменена.");
            return;
        }
        if (amount <= 0) {
            System.out.println("Ошибка: неверная сумма!");
            return;
        }
        
        if (bank.transfer(from, to, amount)) {
            System.out.println("Перевод выполнен успешно!");
        } else {
            String errorMessage = bank.getLastTransactionMessage();
            System.out.println("Ошибка при переводе! Причина: " + errorMessage);
        }
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private double readDouble() {
        try {
            return Double.parseDouble(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private String readStringWithCancel() {
        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            return null;
        }
        return input;
    }

    private Double readDoubleWithCancel() {
        String input = scanner.nextLine().trim();
        if (input.equals("0")) {
            return null;
        }
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }
}
