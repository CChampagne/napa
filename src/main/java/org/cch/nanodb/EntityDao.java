package org.cch.nanodb;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.exceptions.SQLException;

import java.util.List;


/**
 * @author Christophe Champagne
 *  
 */
/*
 * NTH use also generic for key type so we can represent a multiple primary key by one class
 * and use it for the select method
 */
public interface EntityDao<E> {
	/**
	 * Select an org.cch.nanodb.entity by giving another instance of the org.cch.nanodb.entity having the the fields
	 * annotated by @code@Id correctly filled.
	 * @param entityParameter
	 * @return the populated org.cch.nanodb.entity
	 * @throws PersistenceException thrown when there is an issue related to the persistence but not to org.cch.nanodb.jdbc
	 * @throws SQLException thrown when a java.sql.SQLException is thrown in executing the select query
	 * 
	 */
	public E select(E entityParameter) throws PersistenceException, SQLException;
	
	/**
	 * Select all entities available in the corresponding table.
	 * @return the populated org.cch.nanodb.entity
	 */
	public List<E> selectAll() throws PersistenceException, SQLException;
	
	/**
	 * Select an org.cch.nanodb.entity by giving another instance of the org.cch.nanodb.entity having the the fields
	 * annotated by @code@Id correctly filled.
	 * @return the populated org.cch.nanodb.entity
	 */
	public List<E> select(String query, Object...parameters) throws PersistenceException, SQLException;
	
	/**
	 * Inserts an org.cch.nanodb.entity
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void insert(E entity) throws PersistenceException, SQLException;
	
	/**
	 * Updates an org.cch.nanodb.entity
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void update(E entity) throws PersistenceException, SQLException;	
	
	/**
	 * Inserts or Updates an org.cch.nanodb.entity
	 * It can have so performance cost since there could be a check against the DB
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void persist(E entity) throws PersistenceException, SQLException;

	/**
	 * Deletes the record corresponding to the org.cch.nanodb.entity.
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void delete(E entity) throws PersistenceException, SQLException;

	/**
	 * Checks if the record is present in DB
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public boolean recordExists(E entity) throws PersistenceException, SQLException;
	/**
	 * Returns the number of records present in the corresponding table
	 * @return a positive long (>=0)
	 */
	public long count() throws PersistenceException, SQLException;
	/**
	 * 
	 * @return
	 */
	public long count(String query, Object...parameters) throws PersistenceException, SQLException;
	/**
	 * 
	 * @return the class of the org.cch.nanodb.entity implementation
	 */
	public Class<? extends E> getEntityClass();
}
