package org.cch.napa;

import java.io.Closeable;
import java.util.Iterator;

/**
 *
 * @param <T> The Object type
 */
public interface LazyResultSetIterable<T> extends Iterable<T>, Closeable {
    interface LazyResultSetIterator<T> extends Iterator<T>, Closeable {}
}
