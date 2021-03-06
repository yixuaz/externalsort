package com.externalsort;

import com.externalsort.helper.IStreamWrapper;
import com.externalsort.helper.SizeCalculator;
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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class BatchSortedFileProducer {

    private static final Logger LOG = LoggerFactory.getLogger(BatchSortedFileProducer.class);


    public static List<File> sortCsvInBatch(File csvFile, Comparator<CSVRecord> cmp, File tmpDirectory, Charset cs,
                                            boolean isDistinct, CSVFormat csvFormat, int excludeHeaderLines)
            throws IOException{
        return sortCsvInBatch(csvFile, cmp, tmpDirectory, cs, isDistinct, csvFormat, excludeHeaderLines,
                new IStreamWrapper(){});

    }

    /**
     * Given a huge CSV file, This will simply load the file by blocks of lines, then sort them
     * in-memory, and write the result to temporary files that have to be merged later.
     * @param csvFile input CSV file
     * @param cmp comparator passed by user
     * @param tmpDirectory the directory path where to save the tmp file
     * @param cs charset to parse the file
     * @param isDistinct is the comparator key is distinct which means no two record are compare == 0
     *                  if isDistinct == true, but the data is not distinct, it will ignore the duplicate key randomly
     * @param csvFormat the CSV Format you want, if no idea, use CSVFormat.DEFAULT
     * @param excludeHeaderLines how many line in csv header, this should be precluded before sorting starts
     * @param wrapper this used for Stream Wrapper, eg. some one want to use AES Stream to protect file Stream
     * @return result to temporary files that have to be merged later
     * @throws IOException
     */
    public static List<File> sortCsvInBatch(File csvFile, Comparator<CSVRecord> cmp, File tmpDirectory, Charset cs,
                                            boolean isDistinct, CSVFormat csvFormat, int excludeHeaderLines,
                                            IStreamWrapper wrapper) throws IOException {

        // TODO: ADD YOUR CODE HERE
        return null;
    }

    /**
     * Sort a list and save it to a temporary file
     * @param tmpList data to be sorted
     * @param cmp comparator passed by user
     * @param tmpFolderPath the directory path where to save the tmp file
     * @param isDistinct is the comparator key is distinct which means no two record are compare == 0
     *                   if isDistinct == true, but the data is not distinct, it will ignore the duplicate key randomly
     * @param csvFormat the CSV Format you want, if no idea, pass CSVFormat.DEFAULT
     * @param wrapper this used for Stream Wrapper, eg. some one want to use AES Stream to protect file Stream
     * @return the tmp file which content is data in tmpList sorted
     * @throws IOException
     */
    private static File sortSingleTmpFile(List<CSVRecord> tmpList, Comparator<CSVRecord> cmp,
                                          File tmpFolderPath, boolean isDistinct, CSVFormat csvFormat,
                                          IStreamWrapper wrapper) throws IOException {
        // TODO: ADD YOUR CODE HERE
        return null;
    }

    /**
     * This method calls the garbage collector and then returns the free
     * memory. This avoids problems with applications where the GC hasn't
     * reclaimed memory and reports no available memory.
     *
     * @return available memory
     */
    private static long estimateAvailableMemory() {
        System.gc();
        return Runtime.getRuntime().freeMemory();
    }

    /**
     * we divide the file into small blocks. If the blocks are too small, we
     * shall create too many temporary files. If they are too big, we shall
     * be using too much memory. So avoid to have OutofMemoryError, we choose maxMemory/6
     * as a block size
     *
     * @param maxMemory Maximum memory to use (in bytes)
     * @return the estimate
     */
    private static long estimateBestSizeOfBlocks(final long maxMemory) {
        return maxMemory / 2;
    }

}
