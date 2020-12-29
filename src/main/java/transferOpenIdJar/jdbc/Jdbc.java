package transferOpenIdJar.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import transferOpenIdJar.conn.Conn;

public class Jdbc {
	
	public int updateList(Connection conn,String sql) {
		Statement stat=null;
		try {
			stat=conn.createStatement();
			stat.execute(sql);
			return 1;	
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return 0;
		}finally{			
			Conn.closeAll(conn, stat, null);
		}
	}
	
	/***
	 * 
	 * 增删改
	 */
	public int executeUpdate(Connection conn,String sql ,Object []ob){
		PreparedStatement ps=null;
		try {
			ps=prepareStatement(conn,sql,ob);
			int i=ps.executeUpdate();
			return i;	
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
			return 0;
		}finally{			
			Conn.closeAll(conn, ps, null);
		}
	
	}	
	
	/***
	 *查询
	 */
	public PreparedStatement prepareStatement(Connection conn,String sql,Object []ob){		
		PreparedStatement ps=null;
				try {
					int index=1;
					ps = conn.prepareStatement(sql);
						if(ps!=null&&ob!=null){
							for (int i = 0; i < ob.length; i++) {			
									ps.setObject(index, ob[i]);	
									index++; 
							}
						}
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println(e.toString());
				}
		 return ps;
	}

}
