package org.cch.napa.entity.impl.db2e;

import org.cch.napa.ConnectionProvider;
import org.cch.napa.entity.SQLTypeMapper;
import org.cch.napa.entity.impl.EntityDaoFactoryImpl;

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
