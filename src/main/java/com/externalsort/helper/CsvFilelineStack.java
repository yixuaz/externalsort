package com.externalsort.helper;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Iterator;

public class CsvFilelineStack implements IOStack<CSVRecord> {

    private final Iterator<CSVRecord> iterator;
    private final CSVParser csvParser;
    private CSVRecord cache;

    public CsvFilelineStack(CSVParser csvParser) {
        this.csvParser = csvParser;
        iterator = csvParser.iterator();
        reload();
    }

    @Override
    public void close() throws IOException {
        csvParser.close();
    }

    @Override
    public boolean empty() {
        return cache == null;
    }

    @Override
    public CSVRecord peek() {
        return cache;
    }

    @Override
    public CSVRecord pop() throws IOException {
        CSVRecord ret = peek();
        reload();
        return ret;
    }

    private void reload() {
        cache = iterator.hasNext() ? iterator.next() : null;
    }
}
