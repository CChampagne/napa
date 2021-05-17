package org.cch.napa.entity;

import java.io.Closeable;
import java.util.Iterator;
import java.util.List;

/**
 * {@link Iterable} wrapping a {@link java.sql.ResultSet} to map each record lazily, on each iteration.
 * It is aimed to be used as an alternative to creating a whole list at once
 * which eventually becomes problematic as the number of records increases.
 * Note the {@link LazyResultSetIterable#close()} is called automatically as soon as all the iterations have been completed.
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
     * Convert the lazy iterable into a {@link List}.
     * The underlying recordset should be closed as soon as the list
     * @return a classical list.
     */
    List<T> asList();

    /**
     * Returns the first item in the sequence.
     * @return An item of type T or null if the sequence is empty.
     */
    T first();

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
