import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;


public class Connect {
     /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {

            //connecting to the database
            // db parameters
            String url = "jdbc:sqlite:/home/dod/Documents/db_proj/eStore/sqlite/db/chinook.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);


            //step3 create the statement object
            Statement stmt=conn.createStatement();


            Emart.checkTable(stmt);
            Edepot.checkTable(stmt);

            UserInterface.chooseInterface(stmt);
            //step4 close the connection object

            conn.close();
            
            System.out.println("eStore by 5014NG3!!!!");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }
}