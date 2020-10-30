/**
 * by Christophe Champagne
 */
package nanodb.impl.db2e;

import nanodb.SQLTypeMapper;
import nanodb.impl.EntityDaoFactoryImpl;

/**
 * @author Christophe Champagne
 *
 */
public class DB2eEntityDaoFactory extends EntityDaoFactoryImpl {

	/**
	 * @see nanodb.impl.EntityDaoFactoryImpl#getSqlTypeMapper()
	 */
	@Override
	public SQLTypeMapper getSqlTypeMapper() {
		
		return new DB2eTypeMapper();
	}

}
