
import java.sql.*;
import java.util.Properties;

public class DBCon {
	protected Connection conn;
	
	public DBCon() {
	}
	
	@SuppressWarnings("deprecation")
	public void connect() {
		 try {
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 Properties p = new Properties ();
			 p.put("user","root");
			 p.put("password","root");
			 conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433", p);
		 } catch (Exception e) {
			 throw new RuntimeException("Unable to connect", e);
		}
	} 
}