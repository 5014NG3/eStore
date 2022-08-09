import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.*;


public class Edepot {

    static String sqlEdepotInventory =  "CREATE TABLE EDEPOTINVENTORY (stockno VARCHAR(7) not NULL,  manufacturer VARCHAR(20),  modelno INT,  quantity INT, minStock INT, maxStock INT, location VARCHAR(20),  replenishment INT, PRIMARY KEY ( stockno ))";
    static String sqlEdepotShippingNotice = "CREATE TABLE EDEPOTSHIPPINGNOTICE (shipnotid VARCHAR(20) not NULL, companyname VARCHAR(20), manufacturer VARCHAR(20), stockno VARCHAR(7), modelno INT, quantity INT, received INT, PRIMARY KEY(shipnotid))";
    //each notice includes a unique shipping notice identifier,the shipping 
    //company name, and a list of items (manufacturer, model number, quantity). 
    static Scanner ur = new Scanner(System.in);

    public static void checkTable(Statement stmt) throws SQLException{
        ResultSet a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EDEPOTINVENTORY'");
        boolean querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEdepotInventory);
            insertToInventory("AA00101", "HP",6111, 1,2,2,"A9", stmt);
            insertToInventory("AA00201", "Dell",420, 2,3,5,"A7", stmt);
            insertToInventory("AA00202", "Emachine", 3958, 2,4,5, "B52",stmt);
            insertToInventory("AA00301", "Envision", 720, 3,4,6, "C27",stmt);
            insertToInventory("AA00302", "Samsung",  712, 3,4,6, "C13", stmt);
            insertToInventory("AA00401", "Symantec", 2005, 5,7,9, "D27", stmt);
            insertToInventory("AA00402", "Mcafee",  2005, 5,7,9, "D1", stmt);
            insertToInventory("AA00501", "HP",1320, 2,3,4, "E7", stmt);
            insertToInventory("AA00601", "HP",435, 2,3,5, "F9", stmt);
            insertToInventory("AA00602", "Cannon", 738, 2,3,5, "F3", stmt);
        }
    
        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EDEPOTSHIPPINGNOTICE'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEdepotShippingNotice);

        }
    
    
    }

    public static void insertToInventory(String stockno, String manufacturer, int modelno,int minStock, int quantity,  int maxStock, String location, Statement stmt) throws SQLException {

        String sqlInsert = "INSERT INTO EDEPOTINVENTORY (stockno, manufacturer, modelno, quantity, minStock, maxStock, location, replenishment) ";
        sqlInsert +=  "Values (" + "'" + stockno + "',"+ "'" + manufacturer + "'," + "'" + modelno + "',"+ "'" + quantity + "',"+ "'" + minStock + "',"+ "'" + maxStock + "',"+ "'" + location + "','0')";	
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }

    }

    public static void insertToShippingNotice(String shipnotid , String companyname, String manufacturer , String stockno, int modelno, int quantity , int received, Statement stmt ) throws SQLException{
        String sqlInsert = "INSERT INTO EDEPOTSHIPPINGNOTICE(shipnotid,companyname,manufacturer,stockno,modelno,quantity,received)";
        sqlInsert += "Values ('" + shipnotid + "','" + companyname + "','" + manufacturer + "','" + stockno + "','" + modelno + "','" + quantity + "','" + received + "')";
        //System.out.println(sqlInsert);
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
    
    }

    public static void receiveShipment(String shipnotid, Statement stmt) throws SQLException{
        String sqlStockno = "SELECT stockno FROM EDEPOTSHIPPINGNOTICE WHERE shipnotid = '" + shipnotid + "'";
        ResultSet rs = stmt.executeQuery(sqlStockno);
        rs.next();
        String currentStockno = rs.getString(1);

        boolean received = isShipNoteReceived(shipnotid, stmt);

        if(received){
            updateReceived(shipnotid, stmt);
            int changedQuantity = getReplenishment(currentStockno, stmt);
            changedQuantity += getQuantity(currentStockno, stmt);
            updateQuantity(currentStockno,changedQuantity, stmt);
            updateReplenishment(currentStockno, 0, stmt);
            System.out.println("shipping notice received");


        }
        else{
            System.out.println("shipping notice not received");
        }


    }

    public static void setMinMaxLocation(String stockno, Statement stmt) throws SQLException{
        System.out.println("Current stock number: " + stockno + ", Enter minStock, maxStock, and location");
        System.out.print("Enter minStock: ");
        String minStock = ur.nextLine();
        System.out.print("Enter maxStock: ");
        String maxStock = ur.nextLine();
        System.out.print("Enter warhouse location: ");
        String location = ur.nextLine();

        String sqlUpdate = "UPDATE EDEPOTINVENTORY SET minStock = '" + minStock + "', maxStock = '" + maxStock + "', location = '" + location + "' WHERE stockno = '" + stockno + "'";

        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println(stockno + ": inventory values updated!");
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }

    }

    public static void receiveShippingNotice(String shipnotid , String stockno,String companyname, String manufacturer , int modelno, int quantity, Statement stmt ) throws SQLException{
        String sqlQuery = "SELECT * FROM EDEPOTINVENTORY WHERE stockno = '" + stockno + "'";
        try {

            
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if (rs.next()) {
                int currrentReplenishment = getReplenishment(stockno, stmt);
                updateReplenishment(stockno, currrentReplenishment + quantity, stmt);
                
            } 
            else {
                //add new item to inventory
                insertToInventory(stockno, manufacturer, modelno, -1, quantity, -1, "null", stmt);
                //warehouse needs to determine minStock, maxStock, and location
                setMinMaxLocation(stockno, stmt);
            }

            
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        } 

        insertToShippingNotice(shipnotid, companyname, manufacturer, stockno, modelno, quantity, 0, stmt);

    }

    public static boolean isShipNoteReceived(String shipnotid, Statement stmt) throws SQLException{
        String sqlQuery = "SELECT * FROM EDEPOTSHIPPINGNOTICE WHERE shipnotid='"+shipnotid +"'";

        try {
            ResultSet rs = stmt.executeQuery(sqlQuery);
            if(rs.next()){
                return true;
            }
            else{
                return false;
            }
            
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
            
        }




        return false;
    }
    
    public static void updateReceived(String shipnotid, Statement stmt) throws SQLException{
        String sqlUpdate = "UPDATE EDEPOTSHIPPINGNOTICE SET received = '1' WHERE shipnotid = '" + shipnotid + "'";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
    
    }

    public static int getReplenishment(String stockno, Statement stmt) throws SQLException{
		
		String sql = "Select replenishment FROM EDEPOTINVENTORY WHERE stockno='"+stockno +"'";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int replenishment = rs.getInt(1);
		return replenishment;
    }

    public static int getQuantity(String stockno, Statement stmt) throws SQLException{
		
		String sql = "Select quantity FROM EDEPOTINVENTORY WHERE stockno='"+stockno +"'";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int qty = rs.getInt(1);
		return qty;
    }

    public static void updateQuantity(String stockno, int quantity, Statement stmt) throws SQLException{
        String sqlUpdate = "UPDATE EDEPOTINVENTORY SET quantity = '" + quantity + "' WHERE stockno = '" + stockno + "'";
        
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
         
    }

    public static void updateReplenishment(String stockno, int replenishment, Statement stmt) throws SQLException{
        String sqlUpdate = "UPDATE EDEPOTINVENTORY SET replenishment = '" + replenishment + "' WHERE stockno = '" + stockno + "'";
        
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
         
    }

    public static void checkReplenish(String stockno, Statement stmt) throws SQLException{

        String sqlQuery = "SELECT manufacturer FROM EDEPOTINVENTORY WHERE stockno = '" + stockno + "'";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        rs.next();
        String currentManufacturer = rs.getString(1);

        String sqlCurrentQuantity = "SELECT stockno, quantity, maxStock FROM EDEPOTINVENTORY WHERE manufacturer = '" + currentManufacturer + "' AND minStock >= quantity";
        rs = stmt.executeQuery(sqlCurrentQuantity);
        List<String> holdStockno = new ArrayList<>();
        List<Integer> holdQuantity = new ArrayList<>();
        List<Integer> holdMaxstock = new ArrayList<>();

        while(rs.next()){
            holdStockno.add(rs.getString(1));
            holdQuantity.add(rs.getInt(2));
            holdMaxstock.add(rs.getInt(3));
        }

        if(holdStockno.size() >= 3){
            System.out.println("Email to eDedpot: Send replenishment order to " + currentManufacturer + " !");
            for (int i = 0; i < holdStockno.size(); i++) {
                System.out.println("Stock number: " + holdStockno.get(i) + ", Quantity: " + (holdMaxstock.get(i) - holdQuantity.get(i)));

                
            }


        }

   
    }

    public static void nuke(Statement stmt) throws SQLException{
        stmt.executeUpdate("DROP TABLE EDEPOTINVENTORY");
        stmt.executeUpdate("DROP TABLE EDEPOTSHIPPINGNOTICE");
        stmt.executeUpdate("DROP TABLE EMARTCOMPATIBILITY");
        stmt.executeUpdate("DROP TABLE EMARTCART");
        stmt.executeUpdate("DROP TABLE EMARTPREVIOUSORDERS");
        stmt.executeUpdate("DROP TABLE EMARTORDERNO");
        stmt.executeUpdate("DROP TABLE EMARTDESCRIPTION");
        stmt.executeUpdate("DROP TABLE EMARTORDERNOTIMESTAMP");
        stmt.executeUpdate("DROP TABLE EMARTCATALOG");
        stmt.executeUpdate("DROP TABLE EMARTCUSTOMERS");
        System.out.println("droppe");
    }
}

