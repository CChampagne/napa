/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl.db2e;

import com.ibm.next.mam.persistence.SQLTypeMapper;
import com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class DB2eEntityDaoFactory extends EntityDaoFactoryImpl {

	/**
	 * @see com.ibm.next.mam.persistence.impl.EntityDaoFactoryImpl#getSqlTypeMapper()
	 */
	@Override
	public SQLTypeMapper getSqlTypeMapper() {
		
		return new DB2eTypeMapper();
	}

}
