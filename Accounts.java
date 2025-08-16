package org.example.BankingManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class Accounts {
    private Scanner scanner ;
    private Connection connection ;
    public Accounts(Connection connection , Scanner scanner) {
        this.connection = connection ;
        this.scanner = scanner ;

    }

    public long openAccount( String email) {
        if (!account_exist(email)  ) {
            String query = "insert into accounts(account_number , full_name , email , balance , security_pin) values(?,?,?,?,?)" ;
            System.out.println();
            scanner.nextLine();
            System.out.println("Enter the full name :-");
            String name =  scanner.nextLine() ;
            System.out.println();
            System.out.println("Enter the balance :- ");
            double balance = scanner.nextInt() ;
            scanner.nextLine() ;
            System.out.println("Enter the security pin :-");
            String pin = scanner.nextLine();
            long new_account_number = generate_account_number() ;
            try{
                PreparedStatement preparedStatement = connection.prepareStatement(query) ;
                preparedStatement.setLong(1, new_account_number);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, email);
                preparedStatement.setDouble(4, balance);
                preparedStatement.setString(5 , pin);

                int row = preparedStatement.executeUpdate() ;
                if (row >0) {
                    return new_account_number ;
                }
                else {
                    throw new RuntimeException("Account creation is failed ! ") ;

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        throw new RuntimeException("Account already eist!") ;
    }
    public long getAccountNumber(String email) {

        String query = "select account_number from accounts where email = ? " ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query) ;
            preparedStatement.setString(1 , email);
            ResultSet resultSet = preparedStatement.executeQuery() ;
            if ( resultSet.next()) {
                return resultSet.getLong("account_number") ;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        throw new RuntimeException("Acount not found!") ;

    }

    private long generate_account_number() {

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select account_number from accounts order by account_number desc limit 1 ");

            if (resultSet.next()) {
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number + 1 ;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Accounts can't be generated!");
    }

    public boolean account_exist (String email) {
        String query = "select  account_number from accounts where email =  ? " ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query) ;
            preparedStatement.setString(1 , email);
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

