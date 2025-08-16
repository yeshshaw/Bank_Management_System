package org.example.BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountsManager {
    private Connection connection ;
    private Scanner scanner ;

    public AccountsManager(Connection connection , Scanner scanner) {
        this.connection = connection ;
        this.scanner = scanner ;

    }

    public void debit_money(long accountNumber) throws SQLException {

        scanner.nextLine();
        System.out.println("Enter the Amount to deposit ");
        double amount = scanner.nextDouble() ;
        System.out.println("Enter the security pin ");
        String securityPin = scanner.nextLine() ;
        try {
            connection.setAutoCommit(false);
            if (validAccount(accountNumber)) {
                String q = "select * from accounts where account_number = ? and security_pin = ? " ;
                PreparedStatement preparedStatement = connection.prepareStatement(q) ;
                preparedStatement.setLong(1 , accountNumber);
                preparedStatement.setString(2 , securityPin);
                ResultSet resultSet = preparedStatement.executeQuery() ;

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance") ;
                    if ( amount<= current_balance) {
                        String debit_query = "update accounts  set balance = balance - ? where account_number = ? " ;
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query) ;
                        preparedStatement1.setDouble(1 , amount);
                        preparedStatement1.setLong(2, accountNumber);
                        int rowsaffected = preparedStatement1.executeUpdate() ;
                        if (rowsaffected>0) {
                            System.out.println(" Amount Debited Successfully!");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else{
                            System.out.println("Transaction failed ! ");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficirnt balance!");

                    }
                }
                else {
                    System.out.println("Invalid Information");
                }
            }
            else {
                System.out.println("Invalid account number!");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine() ;
        System.out.println( "Enter the amount :-");
        double amount = scanner.nextDouble() ;
        scanner.nextLine() ;
        System.out.println("Enter the security Pin :-");
        String securityPin = scanner.nextLine() ;
        try{
            connection.setAutoCommit(false);
            if (validAccount(account_number)) {
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, securityPin);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    PreparedStatement preparedStatement1 = connection.prepareStatement("update accounts set balance = balance + ? where account_number = ?");
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsaffected = preparedStatement1.executeUpdate();

                    if (rowsaffected > 0) {

                        System.out.println("Rs" + amount + "credited Sucessfully!");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transition failed !");
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return;
                    }
                } else {
                    System.out.println("Invalid security pin ");
                }
            }
            else{
                System.out.println("Invalid Account Number !");
            }

        }catch (SQLException e) {
            e.printStackTrace();
        }
        connection.setAutoCommit(true);
    }

    public void getBalance(long accountNumber) {
        scanner.nextLine();
        System.out.println("Enter the security pin :-");
        String security_pin = scanner.nextLine() ;
        try{

            if (validAccount(accountNumber)) {

                PreparedStatement preparedStatement = connection.prepareStatement("select balance from accounts where account_number = ? and security_pin = ? ");
                preparedStatement.setLong(1, accountNumber);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double balance = resultSet.getDouble("balance");
                    System.out.println("Balance :-  " + balance);
                } else {
                    System.out.println("Invalid pin");
                }
            }
            else{
                System.out.println("Invalid Account Number !");
            }
        }

        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void tranfer_money (long senderAccountNumber) throws SQLException {
        scanner.nextLine();
        System.out.println("Enter the RECIEVER Account Number :-");
        long recieverAccount = scanner.nextLong();
        System.out.println("Enter the amount :-");
        double amount = scanner.nextDouble() ;
        System.out.println("Enter your security pin :-");
        String securityPin = scanner.nextLine() ;
        try {

            if ( validAccount(senderAccountNumber) && validAccount(recieverAccount)) {
                connection.setAutoCommit(false);
                PreparedStatement preparedStatement = connection.prepareStatement("select * from accounts where account_number = ? and security_pin = ?");
                preparedStatement.setLong(1, senderAccountNumber);
                preparedStatement.setString(2, securityPin);

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    double currentBalance = resultSet.getDouble("balance");
                    if( amount <= currentBalance) {
                        String debitQuery = " update accounts set balance - ? where account_number = ?" ;
                        String creditQuery = " update accounts set balance + ? where account_number = ?" ;
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debitQuery) ;
                        PreparedStatement preparedStatement2 = connection.prepareStatement(creditQuery) ;
                        preparedStatement2.setDouble(1 , amount);
                        preparedStatement1.setLong(2 , recieverAccount);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2,senderAccountNumber);
                        int affectedrow1 = preparedStatement1.executeUpdate() ;
                        int affectedrow2 = preparedStatement2.executeUpdate() ;
                        if (affectedrow1 >0 && affectedrow2 > 0) {
                            System.out.println("Transaction Successfull");
                            System.out.println("RS " + amount + " Transfered Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        }
                        else {
                            System.out.println("Transaction failed");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    }
                    else {
                        System.out.println("Insufficiant Balance :-");
                    }


                }
                else {
                    System.out.println("Inavlid security pin");
                }
            }
            else {
                System.out.println("Invalid Account!");
            }

        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public boolean validAccount (long account_number) {
        String query = "select  account_number from accounts where account_number =  ? " ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query) ;
            preparedStatement.setLong(1 , account_number);
            ResultSet resultSet = preparedStatement.executeQuery() ;
            if (resultSet.next()) {
                return true ;
            }
            else return false ;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return false ;

    }


}

