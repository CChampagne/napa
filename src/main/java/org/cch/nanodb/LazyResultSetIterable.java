package org.cch.nanodb;

import org.cch.nanodb.exceptions.PersistenceException;
import org.cch.nanodb.exceptions.RuntimePersistenceException;
import org.cch.nanodb.mapper.RecordMapper;

import java.io.Closeable;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @param <T> The Object type
 */
public interface LazyResultSetIterable<T> extends Iterable<T>, Closeable {
    interface LazyResultSetIterator<T> extends Iterator<T>, Closeable {}
}
