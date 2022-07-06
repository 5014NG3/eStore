import java.sql.*;
class OracleCon{
public static void main(String args[]){
try{
//step1 load the driver class
Class.forName("oracle.jdbc.driver.OracleDriver");

//step2 create the connection object
Connection con=DriverManager.getConnection("jdbc:oracle:thin:@cs174a.cs.ucsb.edu:1521/xepdb1","eduardogalvez","694201337");


//step3 create the statement object
Statement stmt=con.createStatement();

Emart.checkTable(stmt);
Edepot.checkTable(stmt);

UserInterface.chooseInterface(stmt);
//step4 close the connection object

//Edepot.nuke(stmt);


con.close();
}catch(Exception e){ System.out.println(e);}
}


}
