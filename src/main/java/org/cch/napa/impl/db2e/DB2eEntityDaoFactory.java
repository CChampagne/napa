package org.cch.napa.impl.db2e;

import org.cch.napa.ConnectionProvider;
import org.cch.napa.SQLTypeMapper;
import org.cch.napa.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class DB2eEntityDaoFactory extends EntityDaoFactoryImpl {

	public DB2eEntityDaoFactory(ConnectionProvider provider) {
		super(provider);
	}

	/**
	 * @see EntityDaoFactoryImpl#getSqlTypeMapper()
	 */
	@Override
	public SQLTypeMapper getSqlTypeMapper() {
		return new DB2eTypeMapper();
	}

}
