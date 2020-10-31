/**
 * by Christophe Champagne
 */
package org.cch.nanodb.impl.db2e;

import org.cch.nanodb.SQLTypeMapper;
import org.cch.nanodb.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class DB2eEntityDaoFactory extends EntityDaoFactoryImpl {

	/**
	 * @see EntityDaoFactoryImpl#getSqlTypeMapper()
	 */
	@Override
	public SQLTypeMapper getSqlTypeMapper() {
		
		return new DB2eTypeMapper();
	}

}
