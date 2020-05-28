package com.externalsort.helper;

import org.apache.commons.csv.CSVRecord;

public class SizeCalculator {
    private static int OBJ_HEADER;
    private static int OBJ_REF;
    private static int OBJ_OVERHEAD;
    private static int CSV_OVERHEAD = 26000;
    private static boolean IS_64_BIT_JVM;

    private SizeCalculator() {

    }

    static {
        String arch = System.getProperty("sun.arch.data.model");

        IS_64_BIT_JVM = (arch == null || arch.indexOf("32") == -1);

        // The sizes below are a bit rough as we don't take into account
        // advanced JVM options such as compressed oops
        // however if our calculation is not accurate it'll be a bit over
        // so there is no danger of an out of memory error because of this.

        // some flags used by the garbage collector and to manage synchronization
        OBJ_HEADER = IS_64_BIT_JVM ? 16 : 8;

        // a reference to the object's class
        OBJ_REF = IS_64_BIT_JVM ? 8 : 4;

        OBJ_OVERHEAD = OBJ_HEADER + OBJ_REF;
    }

    /**
     * Estimates the size of a CSVRecord in bytes.
     *
     * @param s The CSVRecord to estimate memory footprint.
     * @return The <strong>estimated</strong> size in bytes.
     */
    public static long estimatedSizeOf(CSVRecord s) {
        return ((long) (s.toString().length() * 15) + OBJ_OVERHEAD + CSV_OVERHEAD);
    }

}
