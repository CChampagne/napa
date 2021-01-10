package org.cch.napa.entity;

import java.io.Closeable;
import java.util.Iterator;

/**
 * {@link Iterable} wrapping a {@link java.sql.ResultSet} to map each record lazily, on each iteration.
 * It is aimed to be used as an alternative to creating a whole list at once
 * which eventually becomes problematic as the number of records increases.
 * Note the {@link LazyResultSetIterable#close()}
 *
 *
 * @param <T> The Object type to which each record will be mapped
 *
 */
public interface LazyResultSetIterable<T> extends Iterable<T>, Closeable {
    /**
     * Closes the underlying recordset.
     * This method is called automatically when the iteration has completed.
     * It should be called manually in case of interruption of iteration.
     */
    void close();
    /**
     *
     * @param <T>
     */
    interface LazyResultSetIterator<T> extends Iterator<T>, Closeable {
        /**
         * Closes the underlying recordset.
         * This method is called automatically when the iteration has completed.
         * It should be called manually in case of interruption of iteration.
         */
        void close();
    }
}
