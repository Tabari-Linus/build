
import java.time.LocalDate;
import java.util.*;

// Creating an interface for banking activities
interface BankingOperations {

    void deposit(double amount);

    boolean withdraw(double amount);

    double getBalance();

    void addTransaction(String type, double amount);

    void displayTransactionHistory(int n);
}

abstract class BankAccount implements BankingOperations {

    protected String accountNumber;
    protected String accountHolderName;
    protected double accountBalance;
    protected TransactionLinkedList transactionHistory;
    protected String accountType = "Bank Account";

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.accountBalance = initialBalance;
        this.transactionHistory = new TransactionLinkedList();
        addTransaction("Initial Deposit", initialBalance);
    }

    @Override
    public abstract void deposit(double amount);

    @Override
    public abstract boolean withdraw(double amount);

    @Override
    public double getBalance() {
        return accountBalance;
    }

    @Override
    public void addTransaction(String type, double amount) {
        String transactionString = String.format(" Account holder: %s with account number %s made a %s of : %.2f", accountHolderName, accountNumber, type, amount);
        transactionHistory.add(transactionString);
    }

    @Override
    public void displayTransactionHistory(int n) {
        transactionHistory.printLastN(n, accountNumber);
    }

    protected String getAccountType() {
        return accountType;
    }
}

class SavingsAccount extends BankAccount {

    final double MINBALANCE = 50;
    final String accountType = "Savings";

    public SavingsAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Amount to withdraw must be positive");
            return false;
        }
        if (accountBalance - amount < MINBALANCE) {
            System.out.println("Withdrawal denied. Insufficient balance.");
            return false;
        }
        accountBalance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount to deposit must be positive");
            return;
        }
        accountBalance += amount;
        addTransaction("Deposite ", amount);
    }

    public void applyInterest(double rate) {
        double interest = accountBalance * rate / 100;
        accountBalance += interest;
        addTransaction("Interest", interest);
    }

    @Override
    protected String getAccountType() {
        return accountType;
    }

}

class CurrentAccount extends BankAccount {

    private double overdraftLimit;
    final String accountType = "Current";

    public CurrentAccount(String accountNumber, String accountHolderName, double initialBalance) {
        super(accountNumber, accountHolderName, initialBalance);
        this.overdraftLimit = 500;
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid withdrawal amount");
            return false;
        }
        if (accountBalance - amount < -overdraftLimit) {
            System.out.println("Withdrawal denied. Amount to withdraw exceeds Overdraft limit.");
            return false;
        }
        accountBalance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount to deposit must be positive");
            return;
        }
        accountBalance += amount;
        addTransaction("Deposite ", amount);
    }

    public void setOverdraftLimit(double newLimit) {
        this.overdraftLimit = newLimit;
    }

    @Override
    protected String getAccountType() {
        return accountType;
    }
}

class FixedDepositAccount extends BankAccount {

    private LocalDate maturityDate;
    private double interestRate;
    final String accountType = "Fixed Deposit";
    boolean interestApplied = false;

    public FixedDepositAccount(String accountNumber, String accountHolderName,
            double initialBalance, LocalDate maturityDate, double interestRate) {
        super(accountNumber, accountHolderName, initialBalance);
        this.maturityDate = maturityDate;
        this.interestRate = interestRate;
    }

    @Override
    public boolean withdraw(double amount) {
        if (LocalDate.now().isBefore(maturityDate)) {
            System.out.println("Withdrawal denied. Account not matured for withdrawal. Locked until " + maturityDate);
            return false;
        } else if (amount <= 0) {
            System.out.println("Amount should not be less than zero.");
            return false;
        } else if (amount > accountBalance) {
            System.out.println("Insufficient funds in the account.");
            return false;
        }

        // checking if interest has been applied
        if (interestApplied) {
            accountBalance -= amount;
            addTransaction("Withdrawal", amount);
            return true;
        } else {
            applyInterest();
            accountBalance -= amount;
            addTransaction("Withdrawal", amount);
            interestApplied = true;
            return true;
        }
    }

    public void applyInterest() {
        if (LocalDate.now().isAfter(maturityDate)) {
            double interest = accountBalance * interestRate / 100;
            accountBalance += interest;
            addTransaction("Interest", interest);
        }
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Amount to deposit must be positive");
            return;
        }
        accountBalance += amount;
        addTransaction("Deposite ", amount);
    }

    @Override
    protected String getAccountType() {
        return accountType;
    }

    public LocalDate getMaturityDate() {
        return maturityDate;
    }
}

class TransactionLinkedList {

    private class Node {

        String transaction;
        Node next;

        Node(String transaction) {
            this.transaction = transaction;
        }
    }

    private Node head;

    public void add(String transaction) {
        Node newNode = new Node(transaction);
        newNode.next = head;
        head = newNode;
    }

    public void printLastN(int n, String accountNumber) {
        Node current = head;
        int count = 0;
        while (current != null && count < n) {
            if (current.transaction.contains(accountNumber)) {
                System.out.println(current.transaction);
                count++;
            }
            current = current.next;

        }
    }
}

public class BankAccountManagementSystem {

    static List<BankAccount> accounts = new ArrayList<>();

    public static void createAccount(String accountHolderName, double initialBalance, String accountType) {
        String accountNumber = null;
        BankAccount account;

        if (accounts.isEmpty()) {
            accountNumber = "1010020001";
        } else {
            int lastAccountNumber = Integer.parseInt(accounts.get(accounts.size() - 1).accountNumber);
            accountNumber = String.valueOf(lastAccountNumber + 1);
        }

        try {
            if (accountType.equalsIgnoreCase("savings")) {
                account = new SavingsAccount(accountNumber, accountHolderName, initialBalance);
                System.out.println("Savings account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else if (accountType.equalsIgnoreCase("current")) {
                account = new CurrentAccount(accountNumber, accountHolderName, initialBalance);
                System.out.println("Current account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else if (accountType.equalsIgnoreCase("fixed")) {
                account = new FixedDepositAccount(accountNumber, accountHolderName, initialBalance,
                        LocalDate.now().plusYears(1), 5.0);
                System.out.println("Fixed deposit account created for : " + accountHolderName + " with account number: " + accountNumber);
            } else {
                System.out.println("Invalid account type specified.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
            return;
        }
        System.out.println("Account created successfully!");

        // Add the account to the list of accounts
        accounts.add(account);

    }

    public static BankAccount getAccount(String accountNumber) {
        try {

            if (accountNumber.isEmpty()) {
                System.out.println("Account number cannot be empty.");
                return null;
            }
            if (accountNumber.length() != 10) {
                System.out.println("Account number must be 10 digits long.");
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        // Search for the account in the list of accounts
        for (BankAccount account : accounts) {
            if (account.accountNumber.equals(accountNumber)) {
                return account;

            }
        }
        System.out.println("Account not found.");
        return null;
    }

    // public static void displayAllAccountDetials() {
    //     if (accounts.isEmpty()) {
    //         System.out.println("No accounts found.");
    //         return;
    //     }
    //     System.out.println("List of all accounts:");
    //     for (BankAccount account : accounts) {
    //         System.out.println("Account number: " + account.accountNumber
    //                 + "\nAccount holder: " + account.accountHolderName
    //                 + "\nAccount balance: " + account.getBalance()
    //                 + "\nAccount type: " + account.getAccountType());
    //     }
    // }
    // Method to view account details
    public static void viewAccountDetails(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.accountNumber.equals(accountNumber)) {
                System.out.println("\n\nAccount details:");
                System.out.println("Account number: " + account.accountNumber
                        + "\nAccount holder: " + account.accountHolderName
                        + "\nAccount balance: " + account.getBalance()
                        + "\nAccount type: " + account.getAccountType());
                return;

            }
        }
        System.out.println("Account not found.");
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Banking Application!");

        // createAccount("Dennis Owusu", 500, "Savings");
        // createAccount("Thomas Owusu", 1000, "Current");
        // createAccount("Emmanuel", 5000, "fixed");
        // createAccount("Kwame", 2000, "fixed");
        // createAccount("Kwesi", 1000, "savings");
        System.out.println("What will you like to do?");
        int choice = 0;
        String accountNumber;
        while (choice != 7) {
            System.out.println("1. Create a new account\n2. View account details\n3. Deposit money\n4. Withdraw money\n5. Transaction History \n6. Interest \n7. Exit");
            System.out.println("\nPlease enter your choice: ");
            try {
                choice = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                scanner.next();
                continue;
            }

            switch (choice) {
                case 1:
                    try {
                        System.out.println("Please enter your name: ");
                        scanner.nextLine();
                        String name = scanner.nextLine();
                        if (name.isEmpty()) {
                            System.out.println("Name cannot be empty.");
                            return;
                        }
                        System.out.println("Please enter your initial deposit amount: ");
                        double initialDeposit = scanner.nextDouble();
                        if (initialDeposit <= 0) {
                            System.out.println("Initial deposit must be positive.");
                            return;
                        }

                        System.out.println("Please enter the account type (savings/current/fixed): ");
                        scanner.nextLine();
                        String acType = scanner.nextLine();
                        if (acType.isEmpty()) {
                            System.out.println("Account type cannot be empty.");
                            return;
                        }
                        createAccount(name, initialDeposit, acType);

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        return;
                    }
                    break;
                case 2:
                    System.out.println("Enter account number to view details: ");
                    scanner.nextLine();
                    accountNumber = scanner.nextLine();
                    viewAccountDetails(accountNumber);
                    break;
                case 3:
                    System.out.println("Enter account number to deposit money to: ");
                    scanner.nextLine();
                    accountNumber = scanner.nextLine();
                    System.out.println("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    if (getAccount(accountNumber) != null) {
                        getAccount(accountNumber).deposit(depositAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    System.out.println("Enter account number to withdraw money: ");
                    scanner.nextLine();
                    accountNumber = scanner.nextLine();
                    System.out.println("Enter amount to deposit: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (getAccount(accountNumber) != null) {
                        getAccount(accountNumber).withdraw(withdrawAmount);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 5:
                    System.out.println("Enter account number to display account transactions: ");
                    scanner.nextLine();
                    accountNumber = scanner.nextLine();
                    System.out.println("Enter number of transactions to display: ");
                    int n = scanner.nextInt();
                    if (getAccount(accountNumber) != null) {
                        getAccount(accountNumber).displayTransactionHistory(n);
                    } else {
                        System.out.println("Account not found.");
                    }

                    break;
                case 6:
                    System.out.println("Enter account number to apply interest: ");
                    scanner.nextLine();
                    accountNumber = scanner.nextLine();
                    BankAccount myAccount = getAccount(accountNumber);
                    if (myAccount instanceof SavingsAccount) {
                        System.out.println("Enter interest rate: ");
                        double rate = scanner.nextDouble();
                        ((SavingsAccount) myAccount).applyInterest(rate);
                    } else if (myAccount instanceof FixedDepositAccount) {
                        ((FixedDepositAccount) myAccount).applyInterest();
                    } else {
                        System.out.println("Interest can only be applied to savings and fixed deposit accounts.");
                    }
                    break;

                case 7:
                    System.out.println("Exiting the application. Thank you!");
                    break;

                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
                    System.out.println("1. Create a new account\n2. View account details\n3. Deposit money\n4. Withdraw money\n5. Transaction History \n6. Interest \n7. Exit");
                    System.out.println("\nPlease enter your choice: ");
            }

        }

    }
}
