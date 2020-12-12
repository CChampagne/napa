package org.cch.napa.impl;

import org.cch.napa.LazyResultSetIterable;
import org.cch.napa.exceptions.PersistenceException;
import org.cch.napa.exceptions.RuntimePersistenceException;
import org.cch.napa.mapper.RecordMapper;

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
class LazyResultSetIterableImpl<T> implements LazyResultSetIterable<T> {
    private LazyResultSetIterator<T> iterator;

    public LazyResultSetIterableImpl(PreparedStatement preparedStatement, RecordMapper<T> mapper) throws org.cch.napa.exceptions.SQLException {
        iterator = new LazyResultSetIterator<T>(preparedStatement, mapper);
    }
    public Iterator<T> iterator() {
        return iterator;
    }

    public void close() throws IOException {
        iterator.close();
    }

    private class LazyResultSetIterator<T> implements Iterator<T>, Closeable {
        private ResultSet resultSet;
        private RecordMapper<T> mapper;
        private PreparedStatement preparedStatement;
        private Boolean hasNext;
        private boolean isClosed;

        public LazyResultSetIterator(PreparedStatement preparedStatement, RecordMapper<T> mapper) throws org.cch.napa.exceptions.SQLException {
            this.preparedStatement = preparedStatement;
            try {
                this.resultSet = preparedStatement.executeQuery();
            } catch (SQLException ex) {
                throw new org.cch.napa.exceptions.SQLException("Failed to execute query", ex);
            }
            this.mapper = mapper;
        }
        public boolean hasNext() {
            if (hasNext != null) return hasNext;
            try {
                hasNext = resultSet.next();
                if(!hasNext && !isClosed) {
                    isClosed = true;
                    preparedStatement.close();
                }
                return hasNext;
            } catch (SQLException ex) {
                ex.printStackTrace();
                return hasNext = false;
            }
        }
       public T next() {
            T item = null;
            if (hasNext == null) {
                hasNext();
            }
            if(!hasNext) {
                throw new NoSuchElementException();
            }
            hasNext = null;
             try {
                item = mapper.map(resultSet);
            } catch (SQLException ex) {
                throw new RuntimePersistenceException(ex);
            } catch (PersistenceException ex) {
                throw new RuntimePersistenceException(ex);
            }
             return item;
        }

        public void remove() {
           throw new UnsupportedOperationException();
        }

        public void close() throws IOException {

        }
    }
}
