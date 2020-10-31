/**
 * by Christophe Champagne
 */
package org.cch.nanodb.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.sap.ip.me.api.logging.Trace;
import com.sap.ip.me.api.services.MeIterator;
import com.sap.ip.me.persist.jdbc.IBMDB2eManager;

/**
 * @author Christophe Champagne
 *
 */
public class ORESDB2eManager extends IBMDB2eManager {
	private static Connection connection;

	/**
	 * @see com.sap.ip.me.persist.jdbc.IBMDB2eManager#getConnection()
	 */
	@Override
	public Connection getConnection() throws SQLException {
		if(connection == null){
			trace();
			connection = super.getConnection();
		}
		return connection;
	}
	   private void trace() throws SQLException {
		      String url = super.getDBUrl().replace("%d", super.getRootDir().getAbsolutePath() + File.separator);
		      String user = super.getUserName();
		      String password = super.getPassword();
		     Properties p = new Properties();
		     p.put("DB2e_ENCODING", "UTF8");
		 
		     p.put("ENABLE_DELETE_PHYSICAL_REMOVE", String.valueOf(super.getBooleanProperty("MobileEngine.Persist.Jdbc.Db2e.DeletePhysical", true)));
		     p.put("user", user);
		     p.put("password", password);
		     p.put("ENABLE_TABLE_CHECKSUM", String.valueOf(super.getBooleanProperty("MobileEngine.Persist.Jdbc.Db2e.EnableChecksum", false)));
		     p.put("#url_DoNotConsiderThis", url);
		     for (MeIterator itr = super.getDBConnectionPropertyKeys(); itr.hasNext(); ) {
		       String key = (String)itr.next();
		       String value = super.getDBConnectionPropertyValue(key);
		       p.put(key, value);
		       //"ENABLE_IO_WRITETHROUGH" set to value " false"
		       Trace.getInstance("MI/Persistence").log(90, "DB connection property \"{0}\" set to value \" {1}\"", key, value);
		     }
		     FileOutputStream fo = null;
		     try{
		    	 fo = new FileOutputStream(new File("c:\\mobile\\connection.properties"));
		    	 p.store(fo, "all properties necessary to create db2e connection");
		     } catch (IOException e){
		    	 if(fo != null){
		    		 try {
						fo.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		    	 }
		     }
	   }
}
