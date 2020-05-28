package com.externalsort;

import com.externalsort.helper.IStreamWrapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Comparator;
import java.util.List;

public class ExternalSort {

    private static final File TMP_DIR = new File(System.getProperty("java.io.tmpdir"));

    private static final IStreamWrapper DEFAULT_WRAPPER = new IStreamWrapper() {};

    /**
     * sort a csv file, if file is too large, will use external sorting
     * @param inCsvFile Input CSV File
     * @param outCsvFile Sorted Output CSV File
     * @param comparator comparator passed by user about CSVRecord key to be sorted by
     * @param isDistinct is the comparator key is distinct which means no two record are compare == 0
     *                 if isDistinct == true, but the data is not distinct, it will ignore the duplicate key randomly
     * @param isAppend isAppend == true, will append the file; otherwise, will overwrite the file.
     * @param cs charset to parse the input file
     * @param csvFormat the CSV Format you want, if no idea, pass CSVFormat.DEFAULT
     * @param headerLineNum how many line in csv header, this should be precluded before sorting starts
     * @param wrapper this used for wrap stream, eg. some one want to use AES Stream to protect file Stream
     * @return the file lines
     * @throws IOException
     */
    public static long sortCsv(File inCsvFile, File outCsvFile, Comparator<CSVRecord> comparator, boolean isDistinct,
                               boolean isAppend, Charset cs, CSVFormat csvFormat, int headerLineNum,
                               IStreamWrapper wrapper) throws IOException {
        List<File> tmpFiles = BatchSortedFileProducer.sortCsvInBatch(inCsvFile, comparator, TMP_DIR,
                cs, isDistinct, csvFormat, headerLineNum, wrapper);
        return SortedFilesMerger.mergeSortedCsvFiles(tmpFiles, outCsvFile, csvFormat, comparator, isDistinct,
                isAppend, wrapper);
    }

    public static long sortCsv(File inCsvFile, File outCsvFile, Comparator<CSVRecord> comparator) throws IOException {
        return sortCsv(inCsvFile, outCsvFile, comparator, false, false,
                Charset.defaultCharset(), CSVFormat.DEFAULT, 0);
    }

    public static long sortCsv(File inCsvFile, File outCsvFile, Comparator<CSVRecord> comparator,
                               boolean isDistinct, boolean isAppend, int headerLineNum) throws IOException {
        return sortCsv(inCsvFile, outCsvFile, comparator, isDistinct, isAppend,
                Charset.defaultCharset(), CSVFormat.DEFAULT, headerLineNum);
    }

    public static long sortCsv(File inCsvFile, File outCsvFile, Comparator<CSVRecord> comparator, boolean isDistinct,
                               boolean isAppend, Charset cs, CSVFormat csvFormat, int headerLineNum) throws IOException {
        return sortCsv(inCsvFile, outCsvFile, comparator, isDistinct, isAppend,
                Charset.defaultCharset(), CSVFormat.DEFAULT, headerLineNum, DEFAULT_WRAPPER);

    }

}
