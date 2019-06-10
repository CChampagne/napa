/**
 * by Christophe Champagne (GII561)
 */
package com.ibm.next.mam.persistence.impl.db2e;

import java.sql.Types;

import com.ibm.next.mam.persistence.impl.DefaultSQLTypeMapper;

/**
 * @author Christophe Champagne (GII561)
 *
 */
public class DB2eTypeMapper extends DefaultSQLTypeMapper {

	/**
	 * @see com.ibm.next.mam.persistence.impl.DefaultSQLTypeMapper#getSqlTypeFromClass(java.lang.Class)
	 */
	@Override
	public int getSqlTypeFromClass(Class<?> cls) {
		if(cls.equals(Long.class) || cls.equals(Long.TYPE)){
			//There is no specific type for longs here
			//-> use numeric with size == 12 to implement it
			return Types.NUMERIC;
		}
		return super.getSqlTypeFromClass(cls);
	}
	//TODO Find a way to determine also to determine size from class

	/**
	 * @see com.ibm.next.mam.persistence.impl.DefaultSQLTypeMapper#getSizeFromType(int)
	 */
	@Override
	public int getSizeFromType(int type) {
		if(type == Types.NUMERIC){
			return 15;
		}
		return super.getSizeFromType(type);
	}
	
}
