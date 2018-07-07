package com.tiza.datest.database;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lingtu.crypt.Base64;
import com.tiza.datest.DaApplication;
import com.tiza.datest.entity.MobileEntity;
import com.tiza.datest.entity.Taxi;
import com.tiza.datest.entity.Taxi2;
import com.tiza.datest.util.DaConfig;

public class DataBaseFunction {
	
	private Connection dbCon;
	
	private PreparedStatement psSimserial_commaddr;
	
	private DaConfig conf;

	private Logger cat;
	
	
	public DataBaseFunction(DaApplication App){
		this.conf = App.cfg;
		this.cat = App.log;
		
	}
	
	/**
	 * 初始化数据库连接
	 * 
	 * @return 成功返回true，否则返回false
	 */
	public boolean initDB1() {
		boolean retValue = false;
		try {
			String strDBDriver = conf.theproperties.getProperty("jdbcdriver");
			String strDrl = conf.theproperties.getProperty("jdbcdrl");
			String strDBUserName = conf.theproperties.getProperty("dbuser");
			String strDBPassword = conf.theproperties.getProperty("dbpass");

			cat.info("DBDriver:" + strDBDriver + " DBUrl:" + strDrl
					+ " DBUser:" + strDBUserName);
			Class.forName(strDBDriver);
			// 创建数据库连接
			dbCon = DriverManager.getConnection(strDrl, strDBUserName,
					strDBPassword);

			// mac_id to sim_no
			//select commaddr,suid,vname,sgname,SGID from serviceview
			//select * from serviceview where sgid in(105,84)
			//select commaddr,suid,vname,sgname,sgid from serviceview where sgid in(84,105)
			//where sgid in(84,105,124)
			String sql = conf.theproperties.getProperty("simserial_commaddr", "select commaddr,suid,vname,sgname,sgid from serviceview");
			psSimserial_commaddr = dbCon.prepareStatement(sql);
			
			retValue = true;
		} catch (ClassNotFoundException e) {
			cat.error("Db Driver Not Found!",e);
		} catch (SQLException e) {
			cat.error("Db init field:" , e);
		}
		return retValue;

	}
	
	
	public Vector<MobileEntity> getSimSerial2CommAddr() {
		Vector<MobileEntity> vMac = new Vector<MobileEntity>();
		ResultSet rs = null;
		MobileEntity me = null;
		try {
			rs = psSimserial_commaddr.executeQuery();
			while (rs.next()) {

				me = new MobileEntity();
				me.commaddr = rs.getString("commaddr").trim();
				me.suid = rs.getInt("suid");
				me.vname = rs.getString("vname");
				me.sgname = rs.getString("sgname");
				me.sgid = rs.getInt("sgid");
				vMac.add(me);
			}
			cat.info("vno：" + vMac.size());
		} catch (Exception err) {
			cat.error("getSimSerial2CommAddr error:" ,err);
		} finally {
			try {
				if (null != rs)
					rs.close();
			} catch (SQLException e) {
				cat.error("getSimSerial2CommAddr error:" ,e);
			}
		}
		return vMac;
	}
	
	public List<Taxi> ReadDB(String sql) {
		   List<Taxi> list = new ArrayList<Taxi>();
		   Statement stmt = null;
		   ResultSet rs = null;
			
		   try{
			   stmt = dbCon.createStatement();
			   rs = stmt.executeQuery(sql);
		    while(rs.next()){
		    	Taxi test = new Taxi();
		    	test.suid = rs.getInt("suid");
		    	test.utc = rs.getLong("utc");
		    	test.onlat = rs.getLong("onlat");
		    	test.onlon = rs.getLong("onlon");
		    	test.onutc = rs.getLong("onutc");
		    	test.offlat = rs.getLong("offlat");
		    	test.offlon = rs.getLong("offlon"); 
		    	test.offutc = rs.getLong("offutc");
		    	test.unitprice = rs.getInt("unitprice");
		    	test.waittime = rs.getInt("waittime");
		    	test.distance = rs.getInt("distance");
		    	test.empty_distance = rs.getInt("empty_distance");
		    	test.price = rs.getInt("price");
		    	test.bpos = rs.getInt("bpos");
		    	test.managementid = rs.getString("managementid");
		    	list.add(test);
		    }   
		   } catch (Exception e) {
			} finally {
				try {
					rs.close();
				} catch (Exception e) {
				}
				try {
					stmt.close();
				} catch (Exception e) {
				}
			}
			return list;
		}
	
	/**
	 * 更新commaddr的isupload
	 * @return
	 */
	
	public void updateCommaddr(String commaddr){
		String sql="update TAXI_DEVICESIM  set isupload =1 where commaddr=?";
		PreparedStatement ps=null;	
		try {
			dbCon.setAutoCommit(false);
			ps = dbCon.prepareStatement(sql);
			ps.setString(1,commaddr);
			ps.executeUpdate();
			
			dbCon.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 检查数据库连接
	 * 
	 * @return
	 */
	public boolean getAlive1() {
		try {
			Statement statement = dbCon.createStatement();
			statement.close();
		} catch (Exception ex) {
			return false;
		}
		return true;
	}
	
	/**
	 * 关闭数据库连接
	 */
	public void closeDB1() {
		try {
			// 关闭所有的预处理器
			Field[] fields = DataBaseFunction.class.getDeclaredFields();
			for (Field f : fields) {
				if (f.getName().startsWith("ps")) {
					try {
						PreparedStatement pstmt = (PreparedStatement) f
								.get(this);
						pstmt.close();
					} catch (Exception e) {
					}
				}
			}
			// 关闭数据库连接
			if (null != dbCon)
				dbCon.close();
		} catch (SQLException e) {
			cat.error("Close DB1 error:" , e);
		}
	}

}
