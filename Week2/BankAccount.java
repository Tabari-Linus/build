public interface BankAccount {
    void deposit(double amount);
    boolean withdraw(double amount);
    double getBalance();
    void addTransaction(String type, double amount);
    void displayTransactionHistory(int n);
}


abstract class Account implements BankAccount {
    protected String accountNumber;
    protected String accountHolder;
    protected double balance;
    protected TransactionLinkedList transactionHistory;
    
    public Account(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new TransactionLinkedList();
        addTransaction("Initial Deposit", initialBalance);
    }
    
    @Override
    public abstract void deposit(double amount) ;
    
    @Override
    public abstract boolean withdraw(double amount);
    
    @Override
    public double getBalance() {
        return balance;
    }
    
    @Override
    public void addTransaction(String type, double amount) {
        String transactionString = String.format(" Account holder: %s with a count number %s made a %s of : %.2f", accountHolder,accountNumber, type, amount);
        transactionHistory.add(transactionString);
    }

    
    @Override
    public void displayTransactionHistory(int n) {
        transactionHistory.printLastN(n, accountNumber);
    }
}


public class SavingsAccount extends Account {

    final double MINBALANCE = 50;

    public SavingsAccount(String accountNumber, String accountHolder, double initialBalance) {
        super(accountNumber, accountHolder, initialBalance);
    }

    @Override
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("AAmount to withdraw must be positive");
            return false;
        }
        if (balance - amount < MINBALANCE) {
            System.out.println("Withdrawal denied. Insufficient balance.");
            return false;
        }
        balance -= amount;
        addTransaction("Withdrawal", amount);
        return true;
    }


    public void applyInterest(double rate) {
        double interest = balance * rate / 100;
        balance += interest;
        addTransaction("Interest", interest);
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
        while (current.next != null && current !=null && count < n) {
            if (current.transaction.contains(accountNumber)){
                System.out.println(current.transaction);
                count++;
            }
            current = current.next;
            
        }
    }
}
