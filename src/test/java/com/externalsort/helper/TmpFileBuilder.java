package com.externalsort.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TmpFileBuilder {
    public static File tmpFileBuilder(List<String> fileContentLines) throws IOException {
        File tmp = File.createTempFile("testTmp", null);
        try (PrintWriter writer = new PrintWriter(new FileWriter(tmp.getPath()))) {
            for (String line : fileContentLines) {
                writer.println(line);
            }
        }
        tmp.deleteOnExit();
        return tmp;
    }

    public static File tmpFileBuilder(String allFileContent) throws IOException {
        File tmp = File.createTempFile("testTmp", null);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tmp.getPath()))) {
            writer.write(allFileContent);
        }
        tmp.deleteOnExit();
        return tmp;
    }


    public static File getSuperHugeTestFile(final int blockNums, final int blockLines) throws IOException {
        System.out.println("Temp File Creation: Started");
        final Path path = Files.createTempFile("IntegrationTestFile", ".csv");
        Files.write(path, "".getBytes(StandardCharsets.UTF_8), StandardOpenOption.TRUNCATE_EXISTING);
        final String newLine = "\n";
        IntStream.range(0, blockNums)
                .forEach(i -> {
                    final List<String> idList = new ArrayList<>();
                    IntStream.range(0, blockLines)
                            .forEach(j -> idList.add(RandomString.random(16)));
                    final String content = idList.stream().collect(Collectors.joining("\n"));
                    try {
                        Files.write(path, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                        Files.write(path, newLine.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage());
                    }
                });
        File inputFile = new File(path.toString());
        System.out.println("Temp File Creation: Finished, size : " + inputFile.length() + " Bytes");
        return inputFile;
    }
}
