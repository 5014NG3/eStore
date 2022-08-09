import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.util.*;

public class Emart {
    static String sqlEmartCatalog = "CREATE TABLE EMARTCATALOG(stockno VARCHAR(7), category VARCHAR(20), manufacturer VARCHAR(20), modelno INT, description VARCHAR(400), warranty INT, price REAL, PRIMARY KEY (stockno) )";
    static String sqlEmartCustomers = "CREATE TABLE EMARTCUSTOMERS(cid VARCHAR(20) not NULL, password VARCHAR(20),  name VARCHAR(20), email VARCHAR(20), address VARCHAR(40), status VARCHAR(20), isManager INT, PRIMARY KEY(cid) ) ";
    static String sqlEmartCompatibility = "CREATE TABLE EMARTCOMPATIBILITY (stockno VARCHAR(7) not NULL,  otherstockno VARCHAR(7), PRIMARY KEY (stockno, otherstockno ), FOREIGN KEY (stockno) REFERENCES EmartCatalog(stockno), FOREIGN KEY (otherstockno ) REFERENCES EmartCatalog(stockno) )";
    static String sqlEmartCart = "CREATE TABLE EMARTCART(stockno VARCHAR(7),  cid VARCHAR(20), quantity INTEGER,  PRIMARY KEY ( stockno, cid), FOREIGN KEY (stockno) REFERENCES EmartCatalog (stockno), FOREIGN KEY (cid) REFERENCES EmartCustomers (cid) )";
    static String sqlEmartPreviousOrders = "CREATE TABLE EMARTPREVIOUSORDERS(orderno INTEGER not NULL, cid VARCHAR(20) not NULL, stockno VARCHAR(20), quantity INT, timestamp TIMESTAMP, price REAL, month INT, PRIMARY KEY(orderno, stockno) , FOREIGN KEY(stockno) REFERENCES EmartCatalog(stockno), FOREIGN KEY(cid) REFERENCES EmartCustomers(cid) ) ";
    static String sqlEmartOrderno = "CREATE TABLE EMARTORDERNO(ordernumtracker INTEGER, PRIMARY KEY(ordernumtracker))";
    static String sqlEmartDescription = "CREATE TABLE EMARTDESCRIPTION(stockno VARCHAR(7), attribute VARCHAR(20), value VARCHAR(20), PRIMARY KEY(stockno, attribute), FOREIGN KEY(stockno) REFERENCES EMARTCATALOG(stockno))";
    static String sqlEmartOrdernoTimestamp = "CREATE TABLE EMARTORDERNOTIMESTAMP (orderno INTEGER, cid VARCHAR(20), timestamp TIMESTAMP, cartTotal REAL, month INT, PRIMARY KEY(orderno),  FOREIGN KEY(cid) REFERENCES EMARTCUSTOMERS)";



    //search for items by stock number, manufactuer, modelno, category, description attribute and value
    // or compatible items of an item, or search conditions can be combo of these

    public static void checkTable(Statement stmt) throws SQLException{
        ResultSet a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTCATALOG'");
        boolean querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartCatalog);
            //populate with sample data here
            
            insertToCatalog("AA00101", "Laptop", "HP", 6111, "Processer speed: 3.33Ghz,Ram size: 512 Mb,Hard disk size: 100Gb,Display Size: 17\"", 12, 1630, stmt);
            insertToCatalog("AA00201", "Desktop", "Dell",420, "Processer speed: 2.53Ghz, Ram size: 256 Mb, Hard disk size: 80Gb, Os: none",12,239, stmt);
            insertToCatalog("AA00202", "Desktop", "Emachine", 3958,"Processer speed: 2.9Ghz, Ram size: 512 Mb, Hard disk size: 80Gb",12,369.99, stmt);
            insertToCatalog("AA00301", "Monitor", "Envision", 720,"Size: 17\", Weight: 25 lb.",36,69.99, stmt);
            insertToCatalog("AA00302", "Monitor", "Samsung",  712, "Size: 17\", Weight: 9.6 lb.",36,279.99,stmt);
            insertToCatalog("AA00401", "Software", "Symantec", 2005, "Required disk size: 128 MB, Required RAM size: 64 MB",60,19.99, stmt);
            insertToCatalog("AA00402", "Software", "Mcafee",   2005, "Required disk size: 128 MB, Required RAM size: 64 MB",60,19.99, stmt);
            insertToCatalog("AA00501", "Printer", "HP", 1320,"Resoulution: 1200 dpi, Sheet capacity: 500, Weight: .4 lb",12,299.99, stmt);
            insertToCatalog("AA00601", "Printer", "HP", 435,"Resoulution: 3.1 Mp, Max zoom: 5 times, Weight: 24.7 lb",3,119.99, stmt);
            insertToCatalog("AA00602", "Camera", "Cannon", 	738, "Resoulution: 3.1 Mp, Max zoom: 5 times, Weight: 24.7 lb",1,329.99, stmt);


            /*
            long now = System.currentTimeMillis();
            Timestamp sqlTimestamp = new Timestamp(now);
            System.out.println("init emart catalog " + sqlTimestamp);

            //SELECT timestamp FROM randomTable ORDER BY timestamp ASC
            */

            



        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTCOMPATIBILITY'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartCompatibility);
            //populate with sample data here
            insertToCompatibility("AA00301","AA00201",stmt);
            insertToCompatibility("AA00301","AA00202",stmt);
            insertToCompatibility("AA00302","AA00201",stmt);
            insertToCompatibility("AA00302","AA00202",stmt);
            insertToCompatibility("AA00401","AA00202",stmt);
            insertToCompatibility("AA00401","AA00201",stmt);
            insertToCompatibility("AA00401","AA00101",stmt);
            insertToCompatibility("AA00402","AA00101",stmt);
            insertToCompatibility("AA00402","AA00201",stmt);
            insertToCompatibility("AA00402","AA00202",stmt);
            insertToCompatibility("AA00501","AA00201",stmt);
            insertToCompatibility("AA00501","AA00202",stmt);
            insertToCompatibility("AA00601","AA00201",stmt);
            insertToCompatibility("AA00601","AA00202",stmt);
            insertToCompatibility("AA00602","AA00201",stmt);
            insertToCompatibility("AA00602","AA00202",stmt);
            System.out.println("init compatibility");
        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name ='EMARTCUSTOMERS'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartCustomers);
            //populate with sample data here
            insertToCustomers("Rhagrid","Rhagrid","Rubeus Hagrid","rhagrid@cs","123 MyStreet, Goleta apt A, Ca","Gold",0,stmt);
            insertToCustomers("Mhooch","Mhooch","Madam Hooch","mhooch@cs","123 MyStreet, Goleta apt B, Ca","Silver",0,stmt);
            insertToCustomers("Amoody","Amoody","Alastor Moody","amoody@cs","123 MyStreet, Goleta apt C, Ca","New",0,stmt);
            insertToCustomers("Pquirrell","Pquirrell","Professor Quirrell","pquirrell@cs","123 MyStreet, Goleta apt D, Ca","New",0,stmt);
            insertToCustomers("Sblack","Sblack","Sirius Black","sblack@cs","123 MyStreet, Goleta apt E, Ca","Green",1,stmt);
            insertToCustomers("Ddiggle","Ddiggle","Dedalus Diggle","ddiggle@cs","123 MyStreet, Goleta apt F, Ca","Green",0,stmt);

            System.out.println("init customers");
            
            
        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTCART'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartCart);
            
        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTPREVIOUSORDERS'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartPreviousOrders);

        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTORDERNO'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartOrderno);
            String initialOrderno = "INSERT INTO EMARTORDERNO (ordernumtracker) Values ('0')";
            stmt.executeUpdate(initialOrderno);


        }

        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTDESCRIPTION'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartDescription);
            //populate
            insertToDescription("AA00101", "Processer speed", "3.33Ghz",stmt );
            insertToDescription("AA00101", "Ram size", "512 Mb",stmt );
            insertToDescription("AA00101", "Hard disk size", "100Gb",stmt );
            insertToDescription("AA00101", "Display Size", "17\"",stmt );

            insertToDescription("AA00201", "Processer speed", "2.53Ghz",stmt );
            insertToDescription("AA00201", "Ram size", "256 Mb",stmt );
            insertToDescription("AA00201", "Hard disk size", "80Gb",stmt );
            insertToDescription("AA00201", "Os", "none",stmt );

            insertToDescription("AA00202", "Processer speed", "2.9Ghz",stmt );
            insertToDescription("AA00202", "Ram size", "512 Mb",stmt );
            insertToDescription("AA00202", "Hard disk size", "80Gb",stmt );

            insertToDescription("AA00301", "Size", "17\"",stmt );
            insertToDescription("AA00301", "Weight", "25 lb.",stmt );

            insertToDescription("AA00302", "Size", "17\"",stmt );
            insertToDescription("AA00302", "Weight", "9.6 lb.",stmt );

            insertToDescription("AA00401", "Required disk size", "128 MB",stmt );
            insertToDescription("AA00401", "Required RAM size", "64 MB",stmt );

            insertToDescription("AA00402", "Required disk size", "128 MB",stmt );
            insertToDescription("AA00402", "Required RAM size", "64 MB",stmt );

            insertToDescription("AA00501", "Resolution", "1200 dpi",stmt );
            insertToDescription("AA00501", "Sheet capacity", "500",stmt );
            insertToDescription("AA00501", "Weight", ".4 lb",stmt );

            insertToDescription("AA00601", "Resolution", "3.1 Mp",stmt );
            insertToDescription("AA00601", "Max zoom", "5 times",stmt );
            insertToDescription("AA00601", "Weight", "24.7 lb",stmt );

            insertToDescription("AA00602", "Resolution", "3.1 Mp",stmt );
            insertToDescription("AA00602", "Max zoom", "5 times",stmt );
            insertToDescription("AA00602", "Weight", "24.7 lb",stmt );
            


            



            



        }

        
        a = stmt.executeQuery("SELECT name FROM sqlite_master WHERE name = 'EMARTORDERNOTIMESTAMP'");
        querySuccess = a.next();
        if(!querySuccess){
            stmt.execute(sqlEmartOrdernoTimestamp);
            System.out.println("init ordernotimestamp");
        }
        


  
    }
    
    public static void  searchEmartCatalog(String stockno, String manufacturer,String modelno, String category, String attribute, String value, Statement stmt) throws SQLException{
        String sqlQuery = queryHelper(stockno, manufacturer, modelno, category);
        ResultSet rs = stmt.executeQuery (sqlQuery);
       
        List<String> holdStockno = new ArrayList<>();
        System.out.println("Your search returned:");

        //no attribute and value provided
        if(attribute.isEmpty() && value.isEmpty()){
            while(rs.next()){
                System.out.println(rs.getString(1)+" "+rs.getString(2)+" "+rs.getString(3) + " " +  rs.getInt(4)+" "+rs.getString(5)+" "+rs.getInt(6)+" "+rs.getFloat(7)   );
            }

        }


        //when attribute and value is provided
        if(!attribute.isEmpty() && !value.isEmpty()){

            while(rs.next()){
            // Get the value from column "columnName" with integer type
                String currentStockno = rs.getString(1);
                holdStockno.add(currentStockno);
                //System.out.println(currentStockno+" "+rs.getString(2)+" "+rs.getString(3) + " " +  rs.getInt(4)+" "+rs.getString(5)+" "+rs.getInt(6)+" "+rs.getFloat(7)   ); 

            }
            rs.close();
            List<Boolean> descriptionValid = new ArrayList<>();

            for(int i = 0; i < holdStockno.size(); i++){
                descriptionValid.add(searchDescription(holdStockno.get(i), attribute, value, stmt) ); 
            }

            for(int i = 0; i < descriptionValid.size(); i++){
                System.out.println(descriptionValid.get(i));
            }

            int index = 0;

            ResultSet copy_rs = stmt.executeQuery(sqlQuery);

            while(copy_rs.next()){
                if(descriptionValid.get(index)){
                    System.out.println(copy_rs.getString(1)+" "+copy_rs.getString(2)+" "+copy_rs.getString(3) + " " +  copy_rs.getInt(4)+" "+copy_rs.getString(5)+" "+copy_rs.getInt(6)+" "+copy_rs.getFloat(7)   ); 
                }
                    index += 1;
            }

            copy_rs.close();
        }  

    }

    public static Boolean searchDescription(String stockno, String attribute, String value, Statement stmt) throws SQLException{

        String sqlQuery = "SELECT * FROM EMARTDESCRIPTION WHERE stockno = '" + stockno + "' AND ATTRIBUTE = '" + attribute + "' AND VALUE = '" + value +  "'";

        ResultSet ts = stmt.executeQuery(sqlQuery);
        boolean success = ts.next();
        boolean initialVal = success;
        while(success){
            success = ts.next();
        }



        ts.close();

        return initialVal;

    }
    
    public static String queryHelper(String stockno, String manufacturer,String modelno, String category) throws SQLException{
        boolean where = false;

    
        String sqlQuery = "SELECT * FROM EMARTCATALOG ";


        if(!stockno.isEmpty()){
            if(!where){
                sqlQuery += "WHERE stockno = '"+stockno+"'";
                where = true;
            }
            else{
                sqlQuery += " AND stockno = '"+stockno+"'";

            }

        }


        if(!manufacturer.isEmpty()){
            if(!where){
                sqlQuery+="WHERE manufacturer = '"+manufacturer+"'";
                where = true;

            }
            else{
                sqlQuery+=" AND manufacturer = '"+manufacturer+"'";
            }

        }

        if(!modelno.isEmpty()){
            if(!where){
                sqlQuery+="WHERE modelno = '"+modelno+"'";
                where = true;

            }
            else{
                sqlQuery+=" AND modelno = '"+modelno+"'";
            }

        }

        if(!category.isEmpty()){

            if(!where){
                sqlQuery+="WHERE category = '"+category+"'";
                where = true;

            }
            else{
                sqlQuery+=" AND category = '"+category+"'";
            }

        }
        
        /*
        if(!description.isEmpty()){

            if(!where){
                sqlQuery+="WHERE description = '"+description+"'";
                where = true;

            }
            else{
                sqlQuery+=" AND description = '"+description+"'";
            }

        }
        */
    




        return sqlQuery;

    }

    public static void insertToCatalog(String stockno , String category, String manufacturer , int modelno, String description, int warranty, double price, Statement stmt) throws SQLException{
        String sqlInsert = "INSERT INTO EMARTCATALOG (stockno, category, manufacturer, modelno, description, warranty, price) ";
        sqlInsert +=  "Values (" + "'" + stockno + "',"+ "'" + category + "'," + "'" + manufacturer + "',"+ "'" + modelno + "',"+ "'" + description + "',"+ "'" + warranty + "',"+ "'" + price + "')";	
        //System.out.println(sqlInsert);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        



    }
 
    public static void insertToCustomers(String cid, String password, String name, String email, String address, String status, int isManager, Statement stmt) throws SQLException{
        String sqlInsert = "INSERT INTO EMARTCUSTOMERS (cid, password, name, email, address, status, isManager) ";
        sqlInsert +=  "Values (" + "'" + cid + "',"+ "'" + password + "'," + "'" + name + "',"+ "'" + email + "',"+ "'" + address + "',"+ "'" + status + "',"+ "'" + isManager + "')";	
        //System.out.println(sqlInsert);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
        se.printStackTrace();
        }
        

    }

    public static void insertToCompatibility(String stockno, String otherstockno, Statement stmt) throws SQLException{
        String sqlInsert = "INSERT INTO EMARTCOMPATIBILITY (stockno, otherstockno) ";
        sqlInsert +=  "Values (" + "'" + stockno + "','" + otherstockno + "')";	
        //System.out.println(sqlInsert);
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        



    }

    public static void insertToCart(String stockno, String cid, int quantity, Statement stmt ) throws SQLException {

        //check quantity here to see if there is enough in stock in eDepot

        String sqlInsert = "INSERT INTO EMARTCART (stockno, cid, quantity) ";
        sqlInsert +=  "Values (" + "'" + stockno + "',"+ "'" + cid + "',"+ "'" + quantity + "')";	
        System.out.println(sqlInsert);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        



        
    }

    public static void insertToDescription(String stockno, String attribute, String value, Statement stmt) throws SQLException{
        String sqlInsert = "INSERT INTO EMARTDESCRIPTION (stockno, attribute, value) ";
        sqlInsert +=  "Values (" + "'" + stockno + "',"+ "'" + attribute + "',"+ "'" + value + "')";	
        System.out.println(sqlInsert);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        


    }

    public static void removeFromCart(String stockno, String cid, Statement stmt) throws SQLException {

        String sqlInsert = "DELETE FROM EMARTCART WHERE STOCKNO = '" + stockno + "' AND CID = '" + cid +"'";
        System.out.println(sqlInsert);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        


    }

    public static void viewCart(String cid, Statement stmt) throws SQLException{

        String sqlQuery = "SELECT * FROM EMARTCART WHERE CID = '" + cid + "'";

        ResultSet rs = stmt.executeQuery (sqlQuery);
        List<String> holdStockno = new ArrayList<>();
        List<String> holdQuantity = new ArrayList<>();
        
        while(rs.next()){

            String currentStockno = rs.getString(1);
            rs.getString(2);
            String currentQuantity = rs.getString(3);

            holdStockno.add(currentStockno);
            holdQuantity.add(currentQuantity);
        }
        rs.close();

        for(int i = 0; i < holdStockno.size(); i++){
            searchEmartCatalog(holdStockno.get(i),"","","","","",stmt); 
            System.out.println("Quantity: " + holdQuantity.get(i) );


        }


    }

    public static void insertToPreviousOrders(int orderno, String cid, String stockno, int quantity, Timestamp timestamp, double price,int month ,Statement stmt) throws SQLException{

        String sqlInsert = "INSERT INTO EMARTPREVIOUSORDERS (orderno, cid, stockno, quantity, timestamp,price , month) ";
        sqlInsert +=  "Values (" + "'" + orderno + "',"+ "'" + cid + "'," + "'" + stockno + "',"+ "'" + quantity + "',"+ "TIMESTAMP '" + timestamp + "',"+ "'" + price + "','" + month  +"')";	
        //System.out.println(sqlInsert);
        //update quantity in eDepot
        int currentQuantity = Edepot.getQuantity(stockno, stmt);
        Edepot.updateQuantity(stockno, currentQuantity - quantity, stmt);
        //check replenishment here
        Edepot.checkReplenish(stockno, stmt);
        
        
        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }
        

    }

    public static void searchCompatibility(String stockno, Statement stmt) throws SQLException{

        String sqlQuery = "SELECT otherstockno FROM EMARTCOMPATIBILITY WHERE stockno = '" + stockno + "'";
        ResultSet rs = stmt.executeQuery (sqlQuery);
        System.out.println("Compatible items with " + stockno + ": ");

        List<String> compStocknos = new ArrayList<>();
        
        while(rs.next()){
        // Get the value from column "columnName" with integer type
            String compatibleStockno = rs.getString(1); 
            compStocknos.add(compatibleStockno);
            //searchEmartCatalog(compatibleStockno, "", "", "", "", stmt);
        }
        rs.close();


        for(int i = 0; i < compStocknos.size(); i++ ){

            searchEmartCatalog(compStocknos.get(i), "", "", "", "","" , stmt);

        }




    }
    
    public static float checkoutHelper(String cid, Statement stmt) throws SQLException{

        String sqlQuery = "SELECT * FROM EMARTCART WHERE CID = '" + cid + "'";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        boolean qs = rs.next();



        if(!qs){
            return -1;
        }

        List<String> getStocknos = new ArrayList<>();
        List<String> getQuantity = new ArrayList<>();

        


        while(qs){
            getStocknos.add(rs.getString(1));
            getQuantity.add(rs.getString(3));
            qs = rs.next();
        }


        float cartTotal = 0;
        String priceQuery;
        int currentOrderno = getcurrentOrderno(stmt);
        Timestamp currentTimestamp = getTimestamp();
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        
        System.out.println("Order summary for order number: " + currentOrderno);
        for(int i = 0; i < getStocknos.size(); i++){
            String currentStockno =  getStocknos.get(i);
            String currentQuantity = getQuantity.get(i);
            priceQuery = "SELECT price FROM EMARTCATALOG WHERE STOCKNO = '" + currentStockno + "'";
            rs = stmt.executeQuery(priceQuery);
            rs.next();
            String currentPrice = rs.getString(1);
            System.out.println("current price: " + currentPrice);
            cartTotal += Float.parseFloat(currentPrice) * Float.parseFloat(currentQuantity);
            System.out.println("Stockno: " + currentStockno + ", Quantity: " + currentQuantity + ", Price: " + currentPrice );

            insertToPreviousOrders(currentOrderno, cid, currentStockno, Integer.parseInt(currentQuantity), currentTimestamp, Float.parseFloat(currentPrice), month, stmt);
            //System.out.println("wtf?");
        }

        String currentUserStatus = getUserStatus(cid, stmt);
        float discount = getDiscount(currentUserStatus);
        float floatDiscount = 1 - discount/100;
        float shippingFee = 0;

        if(currentUserStatus.equals("New")|| cartTotal > 100){
            System.out.println(cid + ",Status: " + currentUserStatus);
            System.out.println("Shipping fee waived! ");
            shippingFee = 0;
        }
        else{
            System.out.println(cid + ",Status: " + currentUserStatus);
            System.out.println("Shipping fee $100");
            shippingFee = 100;
        }

        float actualTotal = cartTotal*floatDiscount + shippingFee;

        insertToEmartOrderTimestamp(currentOrderno, cid, currentTimestamp, actualTotal,month ,stmt);


        emptyCart(cid,stmt);
        updatecurrentOrderno(stmt);
        autoUpdateStatus(cid, stmt);

        System.out.println("Cart total before discount applied: $" + cartTotal);
        System.out.println("Cart total after shipping fee and discount: $" + actualTotal );

        return cartTotal;

        
    }

    public static Timestamp getTimestamp(){
        long now = System.currentTimeMillis();
        Timestamp sqlTimestamp = new Timestamp(now);
        //int mth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        return sqlTimestamp;

    }

    public static int getDiscount(String status) {
        int discount = -1;

        if(status.equals("Green") ){
            discount = 0;
        }
        if(status.equals("New") || status.equals("Gold")){
            discount = 10;
        }

        if(status.equals("Silver")){
            discount = 5;
        }

        

        return discount;

    }

    public static int getcurrentOrderno(Statement stmt) throws SQLException{
        String sqlQuery = "SELECT ordernumtracker FROM EMARTORDERNO";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        int currentOrderno = -1;

        while(rs.next()){
            currentOrderno = rs.getInt(1);

        }

        return currentOrderno;

    }

    public static void updatecurrentOrderno(Statement stmt) throws SQLException{

        int currentOrderno = getcurrentOrderno(stmt);

        currentOrderno += 1;

        String sqlUpdate = "UPDATE EMARTORDERNO SET ordernumtracker = '" + currentOrderno + "'";

        

        try {
            stmt.executeUpdate(sqlUpdate);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }


    }

    public static void emptyCart(String cid, Statement stmt) throws SQLException{
        String sqlUpdate = "DELETE FROM EMARTCART WHERE cid = '" + cid + "'";
        
        try{
            stmt.executeUpdate(sqlUpdate);
        }
        catch(SQLException se){
            System.out.println(se);
            se.printStackTrace();
        }



    }

    public static void managerMonthlySummary(Statement stmt) throws SQLException{
        String sqlQuery = "SELECT stockno FROM EMARTCATALOG";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        List<String> holdStockno = new ArrayList<>();
        while(rs.next()){
            holdStockno.add(rs.getString(1));
            
        }

        sqlQuery = "SELECT DISTINCT category FROM EMARTCATALOG";
        rs = stmt.executeQuery(sqlQuery);
        List<String> holdCagetory = new ArrayList<>();
        while(rs.next()){
            holdCagetory.add(rs.getString(1));
        }
        
        int mth = Calendar.getInstance().get(Calendar.MONTH) + 1;
        System.out.println("Summary for month " + mth + ": ");

        System.out.println("Sales by stock number: ");
        String query;
        for (int i = 0; i < holdStockno.size(); i++) {
            query = "SELECT quantity, price FROM EMARTPREVIOUSORDERS WHERE stockno = '" + holdStockno.get(i) + "'";

            rs = stmt.executeQuery(query);


            float totalPrice = 0;
            int totalQuantity = 0;


            while(rs.next()){
                totalQuantity += rs.getInt(1);
                totalPrice += Float.parseFloat(rs.getString(2));
            }

            System.out.println("Stock number: " + holdStockno.get(i) + ", Quantity: " + totalQuantity + ", Price: " + totalPrice);
        }
        

        System.out.println("Sales by category: ");
        for (int i = 0; i < holdCagetory.size(); i++) {
            query = "SELECT stockno FROM EMARTCATALOG WHERE category = '" + holdCagetory.get(i) + "'";

            rs = stmt.executeQuery(query);

            
            List<String> categoryStockno = new ArrayList<>();
            while(rs.next()){
                categoryStockno.add(rs.getString(1));
            }

            float totalPrice = 0;
            int totalQuantity = 0;

            for (int j = 0; j < categoryStockno.size(); j++) {
                query = "SELECT quantity, price FROM EMARTPREVIOUSORDERS WHERE stockno = '" + categoryStockno.get(j) + "'";
                rs = stmt.executeQuery(query);
                while(rs.next()){
                    totalQuantity += rs.getInt(1);
                    totalPrice += Float.parseFloat(rs.getString(2));
                }
                
            }
            




            System.out.println("Category: " + holdCagetory.get(i) + ", Quantity: " + totalQuantity + ", Price: " + totalPrice);
        }

        

        query = "SELECT * FROM (SELECT * FROM EMARTORDERNOTIMESTAMP ORDER BY cartTotal DESC) WHERE month = '" + mth + "' AND ROWNUM <= 1";

        System.out.println("Customer who made largest purchase: ");
        
        ResultSet rsa = stmt.executeQuery(query);
        //System.out.println(rsa.next());
        
        while(rsa.next()){
            System.out.println("Customer: " + rsa.getString(2) + ", Cart Total: " + rsa.getString(4));
        }
        


    }

    public static void managerChangeStatus(String cid, String newStatus ,Statement stmt) throws SQLException{

        String sqlUpdate = "UPDATE EMARTCUSTOMERS SET status = '" + newStatus + "' WHERE cid = '" + cid + "'";
        try {
            stmt.executeUpdate(sqlUpdate);
            System.out.println(cid + "'s status has been changed to: " + newStatus);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }





    }

    public static void managerSendOrder(String manufacturer, String stockno, int quantity){
        //just print the order here
        System.out.println("Order sent to: " + manufacturer + ", Stock number: " + stockno + ", Quantity: " + quantity);




    }

    public static void managerChangePrice(String stockno, float newPrice, Statement stmt) throws SQLException{
        String sqlUpdate = "UPDATE EMARTCATALOG SET price = '" + newPrice + "' WHERE stockno = '" + stockno + "'";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }


    }

    public static void deletePreviousOrder(int orderno, Statement stmt ) throws SQLException{
        //this is the explicit deletion
        String sqlUpdate = "DELETE FROM EMARTPREVIOUSORDERS WHERE orderno = '" + orderno + "'";
        try {
            stmt.executeUpdate(sqlUpdate);
        } catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }


    }

    public static String chooseStatus(float pastThree){
        if(pastThree > 500){
            return "Gold";
        }
        if(pastThree > 100 && pastThree <= 500){
            return "Silver";
        }
        if(pastThree > 0 && pastThree <= 100){
            return "Green";
        }

        return "null";


    }

    public static void autoUpdateStatus(String cid, Statement stmt) throws SQLException{
        String sqlQuery = "SELECT cartTotal FROM EMARTORDERNOTIMESTAMP WHERE cid = '" + cid + "' ORDER BY timestamp desc OFFSET 0 ROWS FETCH NEXT 3 ROWS ONLY";
        ResultSet rs = stmt.executeQuery(sqlQuery);

        float pastThree = 0;

        while(rs.next()){
            pastThree += Float.parseFloat(rs.getString(1));

        }

        String newStatus = chooseStatus(pastThree);
        if(newStatus == "null"){
            System.out.println("No previous orders, status remains...");
        }
        else{
            managerChangeStatus(cid, newStatus, stmt);
        }



    }

    public static void insertToEmartOrderTimestamp(int orderno , String cid , Timestamp timestamp, float cartTotal, int month, Statement stmt ) throws SQLException{
        String sqlInsert = "INSERT INTO EMARTORDERNOTIMESTAMP (orderno, cid, timestamp, cartTotal, month) ";
        sqlInsert +=  "Values (" + "'" + orderno + "',"+ "'" + cid + "'," + "TIMESTAMP '" + timestamp + "',"+ "'" + cartTotal  + "','" + month + "')";		
        //System.out.println(sqlInsert);

        try {
            stmt.executeUpdate(sqlInsert);
            
        } 
        catch (SQLException se) {
            System.out.println(se);
            se.printStackTrace();
        }




    }

    public static void viewPreviousOrder(int orderno,String cid ,Statement stmt) throws SQLException{
        String sqlQuery = "SELECT * FROM EMARTPREVIOUSORDERS WHERE orderno = '" + orderno + "' AND cid = '" + cid + "'";
        ResultSet rs = stmt.executeQuery(sqlQuery);

        System.out.println("Order number: " + orderno + " details");

        while(rs.next()){
            System.out.println("Stock number: " + rs.getString(3) + ", Quantity: " + rs.getString(4) + ", Timestamp: " + rs.getString(5) + ", Price: " + rs.getString(6));
        
        }
        rs.close();

        sqlQuery = "SELECT cartTotal FROM EMARTORDERNOTIMESTAMP WHERE orderno = '" + orderno + "'";
        rs = stmt.executeQuery(sqlQuery);
        rs.next();
        float cartTotal = Float.parseFloat(rs.getString(1));

        System.out.println("Order total without discount or shipping fee: " + cartTotal);



    }

    public static void rerunPreviousOrder(int orderno, String cid, Statement stmt) throws SQLException{
        String sqlQuery = "SELECT * FROM EMARTPREVIOUSORDERS WHERE orderno = '" + orderno + "' AND cid = '" + cid + "'";
        ResultSet rs = stmt.executeQuery(sqlQuery);
        List<String> holdStockno = new ArrayList<>();
        List<Integer> holdQuantity = new ArrayList<>();
        

        while(rs.next()){
            holdStockno.add(rs.getString(3));
            holdQuantity.add(rs.getInt(4));
            //insertToCart(rs.getString(3), cid, rs.getString(4), stmt);
        }
        rs.close();
        

        for (int i = 0; i < holdStockno.size(); i++) {

            if(holdQuantity.get(i) <= Edepot.getQuantity(holdStockno.get(i), stmt)){
                //System.out.println("ADDEd to cart ! re run order");
                insertToCart(holdStockno.get(i), cid, holdQuantity.get(i), stmt);
            }
            else{
                System.out.println("Not enough stock for: " + holdStockno.get(i));
            }

            
        }

        checkoutHelper(cid, stmt);


    }

    public static String getUserStatus(String cid, Statement stmt) throws SQLException{
        String sqlQuery = "SELECT status FROM EMARTCUSTOMERS WHERE CID = '" + cid + "'";
        ResultSet rs = stmt.executeQuery (sqlQuery);
        if(rs.next()){
            return rs.getString(1);
        }

        return "error_status";






    }




}



