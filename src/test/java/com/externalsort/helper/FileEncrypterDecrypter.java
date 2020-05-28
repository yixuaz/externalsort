package com.externalsort.helper;

import org.apache.commons.io.FileUtils;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;

public class FileEncrypterDecrypter implements IStreamWrapper {
    private SecretKey secretKey;
    public FileEncrypterDecrypter() {
        final SecretKey secretKey = getKey(null);
        this.secretKey = secretKey;
    }

    public void encrypt(File toBeEncryptedFile, String fileName) throws IOException, InvalidKeyException {
        Cipher cipher = getCipher(secretKey);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
             FileInputStream fileIn = new FileInputStream(toBeEncryptedFile)) {
            int b;
            while ((b = fileIn.read()) != -1)
                cipherOut.write(b);
        }
    }

    @Override
    public OutputStream wrap(OutputStream outputStream) {
        Cipher cipher = getCipher(secretKey);
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return new CipherOutputStream(outputStream, cipher);
    }

    @Override
    public InputStream wrap(InputStream inputStream) {
        Cipher cipher = getCipher(secretKey);
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return new CipherInputStream(inputStream, cipher);
    }


    public void decrypt(String fileName, String outFile) throws IOException, InvalidAlgorithmParameterException, InvalidKeyException {
        Cipher cipher = getCipher(secretKey);
        try (FileInputStream fileIn = new FileInputStream(fileName)) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            try (
                    CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
                    InputStreamReader inputReader = new InputStreamReader(cipherIn);
                    BufferedReader reader = new BufferedReader(inputReader);
                    FileOutputStream outputStream = new FileOutputStream(outFile)
            ) {
                int b;
                while ((b = reader.read()) != -1) {
                    outputStream.write(b);
                }

            }

        }
    }

    private Cipher getCipher(SecretKey key) {
        try {
            return Cipher.getInstance("AES");
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    private SecretKey getKey(String keySeed) {
        if (keySeed == null || keySeed.trim().length() == 0) {
            keySeed = "abcd1234!@#$"; // default seed
        }
        try {
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            secureRandom.setSeed(keySeed.getBytes());
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(secureRandom);
            return generator.generateKey();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testEncryptedDecryptedFile() throws Exception {
        FileEncrypterDecrypter fileEncrypterDecrypter = new FileEncrypterDecrypter();

        String resourcesPath = getClass().getResource("/test_files/").getPath();
        String targetFileName = "external_mission.csv";
        File input = new File(resourcesPath + targetFileName);

        fileEncrypterDecrypter.encrypt(input, resourcesPath + targetFileName + ".aes");

        File decrypt = new File(resourcesPath + "decrypt_" + targetFileName);
        fileEncrypterDecrypter.decrypt(resourcesPath + targetFileName + ".aes",
                decrypt.getPath());

        assertTrue(FileUtils.contentEquals(input, decrypt));

    }
}
