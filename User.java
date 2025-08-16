package org.example.BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Scanner scanner;

    private Connection connection ;

    public User ( Connection connection , Scanner scanner) {
        this.connection = connection ;
        this.scanner = scanner ;
    }

    public void register(){
        scanner.nextLine();
        System.out.println("Full Name : ");
        String fullName = scanner.nextLine() ;
        System.out.println("Email : ");
        String email = scanner.nextLine() ;
        System.out.println("password : ");
        String password = scanner.nextLine() ;
        if ( user_exist(email) ) {
            System.out.println("User already exist for this email address ");
            return;
        }
        else {
            String register_query = " insert into Users (full_name , email , password ) values(? ,? , ? )" ;
            try {
                PreparedStatement pstmt = connection.prepareStatement(register_query) ;
                pstmt.setString(1,fullName);
                pstmt.setString(2 , email);
                pstmt.setString(3 , password);
                int affected_row = pstmt.executeUpdate();
                if (affected_row > 0 ) {
                    System.out.println("Register succesfully ! ");
                }
                else System.out.println("Registration failed ! ");


            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String Login() {
        scanner.nextLine() ;
        System.out.println("Enter the email");
        String email = scanner.nextLine() ;

        System.out.println("Enter the Password");

        String password = scanner.nextLine() ;
        if (email.isEmpty() || password.isEmpty()) {
            System.out.println("Email / Password blank नहीं हो सकता!");
            return null;
        }

        String login_query = "select * from Users where email = ? and password = ? " ;
        try{

                PreparedStatement preparedStatement = connection.prepareStatement(login_query);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    return email;
                } else {
                    return null;
                }

        }
        catch (SQLException e ) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist (String email) {
        String query = "select * from users where email = ?" ;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query) ;
            preparedStatement.setString(1,email);
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

