package transferOpenIdJar.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import transferOpenIdJar.conn.Conn;
import transferOpenIdJar.entity.HtUser;
import transferOpenIdJar.jdbc.Jdbc;
import transferOpenIdJar.utils.ConfigUtils;

public class Dao {
    
	public Jdbc jdbc = new Jdbc();

	public HtUser getById(String id){
		Connection conn=Conn.getDataSource(ConfigUtils.getPropertiesMap());
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			pst=jdbc.prepareStatement(conn, "select id,weixin_no from ht_user where id = '"+id+"'", null);
			rs=pst.executeQuery();
			while(rs.next()){
				HtUser htUser = new HtUser();
				htUser.setId(rs.getString(1));
				htUser.setOldWeixinNo(rs.getString(2));
				return htUser;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			Conn.closeAll(conn, pst, rs);
		}
		return null;
	}

	public List<HtUser> getOldOpenId() {
		List<HtUser> list =new ArrayList<HtUser>();
		Connection conn=Conn.getDataSource(ConfigUtils.getPropertiesMap());
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			pst=jdbc.prepareStatement(conn, "select id,weixin_no from ht_user where weixin_no is not null", null);
			rs=pst.executeQuery();
			while(rs.next()){
				HtUser htUser = new HtUser();
				htUser.setId(rs.getString(1));
				htUser.setOldWeixinNo(rs.getString(2));
				list.add(htUser);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			Conn.closeAll(conn, pst, rs);
		}
		return list;
	}

	public int batchUpdate(List<HtUser> resResultList) {
		Connection conn=Conn.getDataSource(ConfigUtils.getPropertiesMap());
		StringBuilder sql = new StringBuilder("begin ");
		for (HtUser htUser : resResultList) {
			String id = htUser.getId();
			String newWeixinNo = htUser.getNewWeixinNo();
			if (newWeixinNo!=null&&newWeixinNo!=""){
				sql.append("update ht_user SET weixin_no = '"+newWeixinNo+"' WHERE id = '"+id+"' ;");
			}
		}
		sql.append("end;");
		if ("begin end;".equals(sql.toString())){
			System.out.println("批量更新0条");
			return 0;
		}
		System.out.println("批量更新SQL："+sql.toString());
		return jdbc.updateList(conn, sql.toString());
	}

}
