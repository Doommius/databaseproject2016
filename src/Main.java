import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        {
//            String url = "jdbc:postgresql://localhost:5432/Computerproject";
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String user = "postgres";
            String password = "12";
            Connection con = null;

            //CONNECTING
            try {
                System.out.println("Connecting to database");
                con = DriverManager.getConnection(url, user, password);

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(DBcalls.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
                System.out.println("Connection failed");
            }
            System.out.println("welcome to the computer store system");
            System.out.println("You have the following options");
            System.out.println("note you have to use the whole part name, and the correct capitallation");
            System.out.println("Listall or LA -> list all parts in stock"); //works
            System.out.println("Listallprice or LAP -> list all prices of all parts in stock."); //works
            System.out.println("Listallsystems or LAS -> list all different systems, and how many can be built from the current stock"); //prints systems and their price,
            System.out.println("Priceoffer or PO -> enter part of system name to get price offer. price + 30 % around up to nearest 99 dkk"); //works and has a multiplier for systems.d
            System.out.println("Sell -> sell an item by listing the name of the item. this can be CPU-2999v3"); //works for parts and predesigned systems,
            System.out.println("Customsystem or CS -> price offer for custom system");
            System.out.println("Restockinglist -> this will manually restock the system.(all parts)"); //Works
            System.out.println("RESTOCK, Restocks the database"); //works.
            boolean keeprun = true;

            while (keeprun == true) {
                System.out.println("Enter what you choose to Select.");
                Scanner menu = new Scanner(System.in);
                String menupick = menu.next();
                // compares input from system in to the cases that repesent a case in use.
                switch (menupick) {
                    case "Listall":case "listall":case "la":case "LA":
                        DBcalls.Printallparts(con);
                        break;
//                    case "Listallprint":case "listallprice":case "lap":case "LAP":
//                        DBcalls.Printallpartswithprice(con);
//                        break;
                    case "Listallsystems":case "LAS":case "las":case "listallsystems":
                        DBcalls.listsystems(con);
                        break;
                    case "PriceOffer":case "PO":case "po":case "priceoffer":
                        System.out.println("price offer test");
                        DBcalls.Priceoffer(con);
                        break;
                    case "sell":case "Sell":
                        System.out.println("sell test");
                        DBcalls.Sellitem(con);
                        break;
                    case "Restockinglist":case "restockinglist":case "rsl":case "RSL":
                        DBcalls.Restockinglist(con);
                        break;
                    case "RESTOCK":case "restock":
                        DBcalls.Restock(con);
                        break;
                    case "CS":case "cs":case "CustomSystem":case "customsystem":
                        DBcalls.customsystem(con);
                        break;
                    case "Exit":case "exit":
                        keeprun = false;
                        break;
                    default:
                        System.out.println("Invalid choice, please pick again.");
                        menupick = "blank";
                        break;


                }
                //}
            }
        }
    }
}