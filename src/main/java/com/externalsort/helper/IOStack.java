package com.externalsort.helper;

import java.io.IOException;

/**
 * General interface to abstract away CsvFilelineStack
 * so that users of the library can roll their own.
 */
public interface IOStack<T> {
    void close() throws IOException;
    boolean empty();
    T peek();
    T pop() throws IOException;
}
