package com.externalsort;

import com.externalsort.helper.CsvFilelineStack;
import com.externalsort.helper.IStreamWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class SortedFilesMerger {
    private static final Logger LOG = LoggerFactory.getLogger(SortedFilesMerger.class);

    // this parameter to limit the max file merging in one process
    // to avoid open too many file descriptor.
    private static final int MAX_FILE_IN_ONE_MERGING = 256;


    /**
     * Merge a list of sorted files into one sorted file to avoid out of memory error
     * @param csvFiles input sorted CSV files
     * @param outputFile output file position
     * @param csvFormat he CSV Format you want, if no idea, use CSVFormat.DEFAULT
     * @param cmp comparator passed by user, to tell which key need to sort in CSVRecord
     * @param isDinstinct is the comparator key is distinct which means no two record are compare == 0
     *      if isDistinct == true, but the data is not distinct, it will ignore the duplicate key randomly
     * @param isAppend isAppend == true will append the file, otherwise, overwrite the file
     * @param wrapper this used for Stream Wrapper, eg. some one want to use AES Stream to protect file Stream
     * @return the file lines
     * @throws IOException
     */
    public static long mergeSortedCsvFiles(List<File> csvFiles, File outputFile, CSVFormat csvFormat,
                                           final Comparator<CSVRecord> cmp, boolean isDinstinct, boolean isAppend,
                                           IStreamWrapper wrapper) throws IOException {
        LOG.debug("mergeSortedCsvFiles file size {}", csvFiles.size());
        try {
            if (csvFiles.size() <= MAX_FILE_IN_ONE_MERGING) {
                List<InputStream> inputStreams = new ArrayList<>();
                for (File f : csvFiles) {
                    inputStreams.add(wrapper.wrap(new FileInputStream(f)));
                }
                if (!outputFile.exists()) {
                    outputFile.getParentFile().mkdirs();
                    outputFile.createNewFile();
                }
                OutputStream outputStream = wrapper.wrap(new FileOutputStream(outputFile, isAppend));

                return mergeSortedCsvFiles(inputStreams, outputStream, csvFormat, cmp, isDinstinct);
            }

            List<File> intermediateMerger = new ArrayList<>();
            File tmpFileFolder = csvFiles.get(0).getParentFile();
            for (int i = 0; i < csvFiles.size(); i += MAX_FILE_IN_ONE_MERGING) {
                List<File> tmp = new ArrayList<>();
                for (int j = i; j < Math.min(csvFiles.size(), i + MAX_FILE_IN_ONE_MERGING); j++) {
                    tmp.add(csvFiles.get(j));
                }
                File tempOutputFile = File.createTempFile("intermediateMerger", null, tmpFileFolder);
                mergeSortedCsvFiles(tmp, tempOutputFile, csvFormat, cmp, isDinstinct, isAppend, wrapper);
                intermediateMerger.add(tempOutputFile);
            }

            return mergeSortedCsvFiles(intermediateMerger, outputFile, csvFormat, cmp, isDinstinct, isAppend, wrapper);
        } finally {
            for (File f : csvFiles) {
                if (f.exists() && !f.delete()) {
                    LOG.warn("The file {} was not deleted", f.getName());
                }
            }
        }

    }

    /**
     * Merge a list of sorted inputStream into outputStream to avoid out of memory error
     * @param csvFiles input sorted CSV inputStream
     * @param outputStream output stream
     * @param csvFormat he CSV Format you want, if no idea, use CSVFormat.DEFAULT
     * @param cmp comparator passed by user, to tell which key need to sort in CSVRecord
     * @param isDinstinct is the comparator key is distinct which means no two record are compare == 0
     *      if isDistinct == true, but the data is not distinct, it will ignore the duplicate key randomly
     * @return the file lines
     * @throws IOException
     */
    public static long mergeSortedCsvFiles(List<InputStream> csvFiles, OutputStream outputStream,
                                           CSVFormat csvFormat, final Comparator<CSVRecord> cmp, boolean isDinstinct) throws IOException {
        try {
            List<CsvFilelineStack> csvList = new ArrayList<>();
            for (InputStream in : csvFiles) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
                CSVParser parser = new CSVParser(bufferedReader, csvFormat);
                csvList.add(new CsvFilelineStack(parser));
            }
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            return mergeSortedFiles(csvList, bufferedWriter, cmp, isDinstinct, csvFormat);
        } finally {
            for (InputStream in : csvFiles) {
                if (in != null) {
                    in.close();
                }
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static long mergeSortedCsvFiles(List<File> csvFiles, File outputFile, CSVFormat csvFormat,
                                           final Comparator<CSVRecord> cmp,
                                           boolean isDinstinct, boolean isAppend) throws IOException {
        return mergeSortedCsvFiles(csvFiles, outputFile, csvFormat, cmp, isDinstinct, isAppend, new IStreamWrapper() {});
    }

    private static long mergeSortedFiles(List<CsvFilelineStack> csvList, BufferedWriter bufferedWriter,
                                         Comparator<CSVRecord> cmp, boolean isDinstinct, CSVFormat csvFormat)
            throws IOException {
        // TODO: ADD YOUR CODE HERE
        long fileLines = 0;
        return fileLines;
    }
}
