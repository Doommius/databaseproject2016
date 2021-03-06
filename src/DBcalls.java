import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * Created by Mark jervelund          <Mark@jervelund.com>  <Mjerv15>
 * Start with java -cp postgresql-9.4-1201.jdbc4.jar:. DBtest
 */
public class DBcalls {


    // converted to new format
    //prints all the parts in the parts table with prices and stock
    public static void Printallparts(Connection con) {
        String query = "SELECT model,stock from parts Order by model;";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("Model                         | Stock");
            while (rs.next()) {
                System.out.print(rs.getString("model"));
                System.out.println("| " + rs.getString("stock"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void Printallpartsprice(Connection con) {
        String query = "SELECT model,price from parts Order by model;";
        try {
            Statement st = con.createStatement();


            ResultSet rs = st.executeQuery(query);
            System.out.println("Model                         | Price");
            while (rs.next()) {

                System.out.print(rs.getString("model"));

                System.out.println("    | " + ((rs.getInt("price"))*130/100));


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // converted to new format only works with single parts atm
    //updates the stock of a item in the parts table.
    public static void Sellitem(Connection con) {
        //sells an item.
        String Part_ID = null;

        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the model ID for the part you wish to sell.");
        Part_ID = keyboard.next();
        if (Part_ID.contains("SYS-")) {
            sellsystem(con, Part_ID);
            return;
        }
        try {
            Statement st = con.createStatement();
            ResultSet exsist = (st.executeQuery("SELECT COUNT(*) model From parts where model SIMILAR  TO '%" + Part_ID + "%';"));
            exsist.next();
            System.out.println(exsist.getInt(1));
            if (exsist.getInt(1) == 1) {

                String query = "UPDATE parts SET Stock = Stock-1 WHERE model SIMILAR  TO '%" + Part_ID + "%';";
//                System.out.println("sold " + Part_ID);
                st.executeUpdate(query);
                System.out.println("Sold 1 x " + Part_ID);
            } else if (exsist.getInt(1) > 1) {
                System.out.println("Multiple containing that string found in database");
            } else {
                System.out.println("Part does not exsist in Database");
            }

        } catch (SQLException e) {
            System.out.println("Part "+Part_ID+" is sold out!");
            //e.printStackTrace();
        }
    }

    //converted to new format
    //Sells a system, and updates that part number in parts
    private static void sellsystem(Connection con, String system) {
        //calculates the price of a system.
        ArrayList<String> syspartlist = new ArrayList<String>();
        int price = 0;
        try {
            Statement st = con.createStatement();
            String query = "Select * FROM  computer  WHERE model SIMILAR  TO '%" + system + "%';";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            syspartlist.add(rs.getString("cpu"));
            syspartlist.add(rs.getString("ram"));
            syspartlist.add(rs.getString("Storage"));
            syspartlist.add(rs.getString("Motherboard"));
            syspartlist.add(rs.getString("computercase"));
            syspartlist.add(rs.getString("graphics"));
//            System.out.println(syspartlist.toString());
            for (String Part_ID : syspartlist) {
                if (Part_ID != null) {
                    try {
                        query = "UPDATE parts SET Stock = Stock-1 WHERE model SIMILAR  TO '%" + Part_ID + "%';";
                        System.out.println("sold " + Part_ID);
                        st.executeUpdate(query);

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("there was a problem with a Part");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there was a problem with a system");
        }
    }

    //converted to new system
    //prints a price offer for parts, and systems, systems are handled by the systemprice function.
    public static void Priceoffer(Connection con) {
        //returns price for parts and systems.
        String Part_ID = null;
        int multiplier = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the model ID for the part you wish to get a price offer..");
        Part_ID = keyboard.next();
        if (Part_ID.contains("SYS-")) {
            System.out.println("Enter the multiplier for how many systems you would like to buy..");
            Scanner multiplierinput = new Scanner(System.in);
            multiplier = multiplierinput.nextInt();
            multiplier -= 1;
            double pricemultiplier = multiplier * 2;
            if (pricemultiplier > 20) {
                pricemultiplier = 20;
            }
            Double systemprice = (((((systemprice(con, Part_ID)) * 13 / 10) / 100) * 100 + 99) * (1 + multiplier) * (1 - (pricemultiplier / 100)));
            int finalprice = systemprice.intValue();
            System.out.println("Price offer for " + Part_ID + " is " + finalprice);
            return;
        }
        try {
            Statement st = con.createStatement();
            String query = "Select price FROM parts WHERE model SIMILAR  TO '%" + Part_ID + "%';";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            int price = ((rs.getInt("price") * 13 / 10));
            System.out.println("Price offer " +
                    "for " + Part_ID + " is " + price);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //converted to new system
    //calculates the price for a system and returns it as an int.
    private static int systemprice(Connection con, String system) {
        //calculates the price of a system.
        ArrayList<String> syspartlist = new ArrayList<String>();
        int price = 0;
        try {
            Statement st = con.createStatement();
            String query = "Select * FROM  computer  WHERE model SIMILAR  TO '%" + system + "%';";
            ResultSet rs = st.executeQuery(query);
            rs.next();
            syspartlist.add(rs.getString("cpu"));
            syspartlist.add(rs.getString("ram"));
            syspartlist.add(rs.getString("Storage"));
            syspartlist.add(rs.getString("Motherboard"));
            syspartlist.add(rs.getString("computercase"));
            syspartlist.add(rs.getString("graphics"));
            for (String Part_ID : syspartlist) {
                // System.out.println(Part_ID);
                if (Part_ID != null) {
                    try {
                        query = "Select price FROM  parts WHERE model SIMILAR  TO '%" + Part_ID + "%';";
                        rs = st.executeQuery(query);
                        rs.next();
                        int tempvalue = rs.getInt("price");

                        price += tempvalue;

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("there was a problem with a Part");
                    }
                }
            }
            return price;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("there was a problem with a system");
        }

        System.out.print(price);
        return price;
    }

    //converted to new system
    //prints a list of all parts not fully stocked.
    public static void Restockinglist(Connection con) {
        //Prints a list of things to restock.
        String query = "SELECT model,stock,refillstock from parts Order by model";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            System.out.println("Model                          | In Stock | preferred level | to restock");
            while (rs.next()) {
                String model = rs.getString("model");
                int stock = rs.getInt("stock");
                int restock = rs.getInt("refillstock");
                if (stock < restock) {
                    System.out.println(model + " | " + stock + "       | " + restock + "               | " + (restock - stock));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Converted to new system
    //restocks all parts in the database
    public static void Restock(Connection con) {
        //restocks the Stock.
        ArrayList<String> Restocklist = new ArrayList<String>();

        System.out.println("Model                          | In Stock | preferred level | restocking");
        String query = "SELECT model,stock,refillstock from parts Order by model";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String model = rs.getString("model");
                int stock = rs.getInt("stock");
                int restock = rs.getInt("refillstock");
                if (stock < restock) {
                    System.out.println(model + " | " + stock + " | " + restock + " | " + (restock - stock));
                    Restocklist.add("UPDATE parts SET stock = refillstock WHERE model SIMILAR  TO '%" + model + "%';");
                }
            }
            for (String updatequery : Restocklist) {
                st.executeUpdate(updatequery);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Converted to new system
    //list all prebuilt systems in the database.
    public static void listsystems(Connection con) {
        //list all systems and their prices.
        String query = "Select model, name from Computer";
        System.out.println("Model                         name                          build cost     price offer stock");
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String model = rs.getString("model");
                System.out.println(model + rs.getString("name") + systemprice(con, model) + "      |    " + ((((systemprice(con, model)) * 13 / 10) / 100) * 100 + 99)+"    |   "+systemstock(con, model));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int systemstock(Connection con,String Part_iD) {
        String query = ("Select min(parts.stock) from Parts where model in " +
                "((select computer.cpu from computer where model similar to '%" + Part_iD + "%'), " +
                "(select computer.ram from computer where model similar to '%" + Part_iD + "%') , " +
                "(select computer.storage from computer where model similar to '%" + Part_iD + "%'), " +
                "(select computer.motherboard from computer where model similar to '%" + Part_iD + "%'), " +
                "(select computer.computercase from computer where model similar to '%" + Part_iD + "%'), " +
                "(select computer.graphics from computer where model similar to '%" + Part_iD + "%'))");
        int stock = 0;
        try {
           // System.out.println(query);
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();
            stock = rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;

    }

    // need to be used in custom system
    // checks if a entry is in the database.
    public static boolean isindb(Connection con, String Part_ID) {
        try {
            Statement st = con.createStatement();
            ResultSet exsist = (st.executeQuery("SELECT COUNT(*) model From parts where model SIMILAR  TO '%" + Part_ID + "%';"));
            exsist.next();
            System.out.println(exsist.getInt(1));
            if (exsist.getInt(1) >= 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // works, but no safeguards.
    //get a price offer on a custom system.
    public static void customsystem(Connection con) {
        try {
            ArrayList<String> syspartlist = new ArrayList<String>();
            String Part_MB, Part_CPU, Part_RAM, Part_GPU, Part_Storage, Part_case, Part_graphics;
            String query;
            Statement st = con.createStatement();

            //choose the cpu
            System.out.println("You can use the following CPU for this sysyem");
            query = "Select model from cpu;";
            ResultSet rscpu = st.executeQuery(query);
            int columns = rscpu.getMetaData().getColumnCount();
            while (rscpu.next()) {
                System.out.print(rscpu.getString(columns));
            }
            System.out.println();
            Scanner keyboard = new Scanner(System.in);
            System.out.println("Enter the CPU that you wish to use.");
            Part_CPU = keyboard.next();
            syspartlist.add(Part_CPU);

            System.out.println("You can use the following motherboards for this CPU");
            query = "Select model from motherboard where motherboard.socket = (Select socket from cpu where model SIMILAR TO '%" + Part_CPU + "%');";
            ResultSet rs = st.executeQuery(query);
            columns = rs.getMetaData().getColumnCount();
            ArrayList<String> motherboards = new ArrayList<String>();
            while (rs.next()) {
                System.out.print(rs.getString(columns));
            }
            System.out.println();
            System.out.println("Enter the Model for the motherboard that you wish to use.");
            Part_MB = keyboard.next();
            syspartlist.add(Part_MB);


            System.out.println("You can use the following Ram for this system");
            query = "Select model from ram where (ram.ramtype = (Select ramtype from motherboard where model SIMILAR TO '%" + Part_MB + "%')AND ram.fsb = (Select fsb from cpu where model Similar to '%" + Part_CPU + "%'));";
            ResultSet rsram = st.executeQuery(query);
            columns = rsram.getMetaData().getColumnCount();
            ArrayList<String> ram = new ArrayList<String>();
            while (rsram.next()) {
                System.out.print(rsram.getString(columns));
            }
            System.out.println();
            System.out.println("Enter the Model for the Ram that you wish to use.");
            Part_RAM = keyboard.next();
            syspartlist.add(Part_RAM);


            System.out.println("You can use the following Storage for this sysyem");
            query = "Select model from storage;";
            ResultSet rsstorage = st.executeQuery(query);
            columns = rsstorage.getMetaData().getColumnCount();
            while (rsstorage.next()) {
                System.out.print(rsstorage.getString(columns));
            }
            System.out.println();
            System.out.println("Enter the Model for the Storage drive that you wish to use.");
            Part_Storage = keyboard.next();
            syspartlist.add(Part_Storage);


            System.out.println("You can use the following Case(s) for this sysyem");
            query = "Select model from computercase where formfactor =(SELECT formfactor FROM motherboard where model similar to '%" + Part_MB + "%') ;";
            ResultSet rscase = st.executeQuery(query);
            columns = rscase.getMetaData().getColumnCount();
            while (rscase.next()) {
                System.out.print(rscase.getString(columns));
            }
            System.out.println();
            System.out.println("Enter the Model for the case that you wish to use.");
            Part_case = keyboard.next();
            syspartlist.add(Part_case);


            query = "Select hasgrafics From cpu where model similar to '%" + Part_CPU + "%';";
            ResultSet rshasgrafics = st.executeQuery(query);
            rshasgrafics.next();
            boolean hasgrafics = false;
            boolean keeprun = true;
            if (rshasgrafics.getBoolean("hasgrafics")) {
                System.out.println("The system you are designing have onboard grafics, do you want to install a one anyway? y/n");
                while (keeprun == true) {
                    Scanner menu = new Scanner(System.in);
                    String menupick = menu.next();
                    // compares input from system in to the cases that repesent a case in use.
                    switch (menupick) {
                        case "y":
                        case "yes":
                            hasgrafics = true;
                            keeprun = false;
                            break;
                        case "n":
                        case "no":
                            hasgrafics = false;
                            keeprun = false;
                            break;
                        default:
                            System.out.println("Invalid choice, please pick again.");
                            menupick = "blank";
                            break;
                    }
                }

            } else {
                hasgrafics = true;
            }

            if (hasgrafics) {
                System.out.println("You can use the following Graphics cards for this system");
                query = "Select model from graphics;";
                ResultSet rsgraphics = st.executeQuery(query);
                columns = rsgraphics.getMetaData().getColumnCount();
                while (rsgraphics.next()) {
                    System.out.print(rsgraphics.getString(columns));
                }
                System.out.println();
                System.out.println("Enter the Model for the Graphics card that you wish to use.");
                Part_graphics = keyboard.next();
                syspartlist.add(Part_graphics);
            }
            int price = 0;
            for (String Part_ID : syspartlist) {
                ;
                if (Part_ID != null) {
                    try {
                        query = "Select price FROM  parts WHERE model SIMILAR  TO '%" + Part_ID + "%';";
                        System.out.println("getting price for " + Part_ID);
                        rs = st.executeQuery(query);
                        rs.next();
                        int tempvalue = rs.getInt("price");

                        price += tempvalue;

                    } catch (SQLException e) {
                        e.printStackTrace();
                        System.out.println("there was a problem with a Part");
                    }
                }
            }
            System.out.println(((price * 13 / 10) / 100) * 100 + 99);

            //syspartlist.forEach(s -> System.out.println(s));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

}

