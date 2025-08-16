package org.example.BankingManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system" ;

    private static final String userName = "root" ;

    private static final String password = "yash54321" ;




    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver") ;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        try{
            Connection connection = DriverManager.getConnection(url , userName , password);

            Scanner scanner = new Scanner(System.in);
            User user = new User(connection , scanner) ;
            Accounts accounts = new Accounts(connection , scanner) ;
            AccountsManager accountsManager = new AccountsManager(connection , scanner) ;
            String email ;
            long account_number ;
            while (true) {
                System.out.println("*** Welcome To Banking System ***");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exist");
                System.out.println("Enter your choice");
                int choice = scanner.nextInt();

                switch ( choice) {
                    case 1 :
                        user.register();
                        break;
                    case 2 :
                        email = user.Login();
                        if(email!= null ){
                            System.out.println();
                            System.out.println("User Logged in ");
                            if (!accounts.account_exist(email)) {
                                System.out.println("1. Open a new Bank account ");
                                System.out.println("2. Exist ");
                                if ( scanner.nextInt() == 1) {
                                    account_number = accounts.openAccount(email) ;
                                    System.out.println("Account created Successfully ");
                                    System.out.println("your account number is" +  account_number);
                                }
                                else {
                                    break;
                                }
                            }
                            account_number = accounts.getAccountNumber(email) ;
                            int choice1 = 0 ;
                            while (choice1 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Tranfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice");
                                choice1 = scanner.nextInt() ;

                                switch (choice1){
                                    case 1 :
                                        accountsManager.debit_money(account_number);
                                        break;
                                    case 2 :
                                        accountsManager.credit_money(account_number);
                                        break;
                                    case 3 :
                                        accountsManager.tranfer_money(account_number);
                                        break;
                                    case 4 :
                                        accountsManager.getBalance(account_number);
                                        break;
                                    case 5 :
                                        break;
                                    default:
                                        System.out.println("Invalid choice");

                                }
                            }

                        }
                        else {
                            System.out.println("Invalid email or Password");

                        }
                    case 3 :
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!!!");
                        System.out.println("EXITING SYSTEM !!!!");
                        return;
                    default:
                        System.out.println("Invalid choice ");

                }

            }




        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }
}
