package com.externalsort.helper;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * General interface to abstract away Wrapper for the FileStream
 * so that users of the library can roll their own.
 */
public interface IStreamWrapper {
    default OutputStream wrap(OutputStream outputStream) {
        return outputStream;
    }
    default InputStream wrap(InputStream inputStream) {
        return inputStream;
    }
}
