package application;

import java.sql.DriverManager;
import java.sql.SQLException;

//import com.mysql.jdbc.Connection;

/*This method creates the SQL Handshake that needs to be conducted before executing queries
 * 
 * */

public class openSQL 
{
	public static java.sql.Connection con;
	public static java.sql.Statement stmt;

	//public static void main(String[] args) throws SQLException
	//{
		//java.sql.Connection con=createConnection();
		//createTables(con);
	//}
	public static java.sql.Connection createConnection()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/RISystem","root","admin123");  
			//CHANGME Variable changes depending on your assigned password for MySQLWorkBench
		}
		catch (Exception e) 
		{
		e.printStackTrace();
		}
		return con;
	}
	public static void createTables(java.sql.Connection con) throws SQLException
	{
		stmt=con.createStatement();
		String sql = "CREATE TABLE IF NOT EXISTS login (\n"
                + "	username varchar(500) PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	level int,\n"
                + " password varChar(500)\n"
                + ");";
		stmt.executeUpdate(sql);
		sql="CREATE TABLE IF NOT EXISTS patient (\n"
				+" patientID int(10) NOT NULL,\n"
				+ "fName varchar(255),\n"
				+ "lName varchar(255),\n"
				+ "dateOfBirth date,\n"
				+ "gender varchar(255),\n"
				+ "phoneNumber varchar(255),\n"
				+ "addressOne varchar(255),\n"
				+ "addressTwo varchar(255),\n"
				+ "addressCity varchar(255),\n"
				+ "addressState varchar(2),\n"
				+ "addressZip int(5)\n"
				+ ")ENGINE=InnoDB DEFAULT CHARSET=latin1;";
		stmt.executeUpdate(sql);
	}
}