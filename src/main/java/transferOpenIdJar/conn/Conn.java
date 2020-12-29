package transferOpenIdJar.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Conn {
	
	/*public static String driver = "oracle.jdbc.driver.OracleDriver";
    
    public static String url = "jdbc:oracle:thin:@192.168.102.60:1521:ora11g";
	
    public static String user = "weivote_hn";
    
    public static String password = "123456";*/

    private static Connection connection;

    public static synchronized Connection getDataSource(HashMap<String, String> map){
    	String driver =map.get("driver");
    	String url =map.get("url");
    	String username =map.get("username");
    	String password =map.get("password");
        initialization(driver,url,username,password);
        return connection;
    }
    
    public static void initialization(String driver,String url,String user,String password) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("找不到驱动程序类 ，加载驱动失败！");
            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException se) {
            System.out.println("数据库连接失败！");
            se.printStackTrace();
        }
    }
    
    public static void closeAll(Connection conn ,Statement ps,ResultSet rs){		
		if(rs!=null)
			try {
				if(rs!=null)
				rs.close();
				if(ps!=null)
				ps.close();
				if(conn!=null)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println(e.toString());
			}				
	}

   /* public static void main(String[] args) {
        try {
            Connection conn = getDataSource(url,user,password);
            System.out.println(conn);
        } catch (Exception e) {
        	e.printStackTrace();
			System.out.println(e.toString());
        }
    }*/

}
