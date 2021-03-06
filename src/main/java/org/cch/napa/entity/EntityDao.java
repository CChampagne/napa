package org.cch.napa.entity;

import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.exceptions.SQLException;

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
	 * Select an entity by giving another instance of the entity having the the fields
	 * annotated by @code@Id correctly filled.
	 * @param entityParameter the entity with the fields corresponding to the primary key set
	 * @return the populated entity
	 * @throws PersistenceException thrown when there is an issue related to the persistence but not to org.cch.napa.jdbc
	 * @throws SQLException thrown when a java.sql.SQLException is thrown in executing the select query
	 *
	 */
	public E select(E entityParameter) throws PersistenceException, SQLException;

	/**
	 * Select all entities available in the corresponding table and returns an {@link LazyResultSetIterable} that will populate the data object on the fly.
	 * It thus preferable if the query returns an important number of records.
	 * Note that the iterable wraps a PreparedStatement that needs to be closed so it also implements Closeable.
	 * It will be automatically closed if all the the items have been iterated.
	 * @return the {@link LazyResultSetIterable}
	 */
	public LazyResultSetIterable<E> lazilySelectAll() throws PersistenceException, SQLException;

	/**
	 * Select an entity by giving another instance of the entity having the the fields
	 * annotated by @code@Id correctly filled.
	 * @return the populated entity
	 */
	public LazyResultSetIterable<E> lazilySelect(String query, Object...parameters) throws PersistenceException, SQLException;

	/**
	 * Select all entities available in the corresponding table.
	 * @return the populated entity
	 */
	public List<E> selectAll() throws PersistenceException, SQLException;

	/**
	 * Select an entity by giving another instance of the entity having the the fields
	 * annotated by @code@Id correctly filled.
	 * @return the populated entity
	 */
	public List<E> select(String query, Object...parameters) throws PersistenceException, SQLException;

	/**
	 * Inserts an entity
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void insert(E entity) throws PersistenceException, SQLException;
	
	/**
	 * Updates an entity
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void update(E entity) throws PersistenceException, SQLException;	
	
	/**
	 * Inserts or Updates an entity
	 * It can have so performance cost since there could be a check against the DB
	 * @param entity
	 * @throws PersistenceException
	 * @throws SQLException
	 */
	public void persist(E entity) throws PersistenceException, SQLException;

	/**
	 * Deletes the record corresponding to the entity.
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
	 * @return the class of the entity implementation
	 */
	public Class<? extends E> getEntityClass();
}
