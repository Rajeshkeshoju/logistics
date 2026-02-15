package com.example.logstics.util;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static boolean writeToFile(String fileContent, String fileName, String filePath) {
        Path dirPath = Paths.get(filePath);
        Path fPath = dirPath.resolve(fileName);

        if(!Files.exists(dirPath)) {
            try {
                Files.createDirectories(dirPath);
                System.out.println("Created directory: " + dirPath);
            } catch(IOException e) {
                System.err.println("Error creating directory: " + e.getMessage());
                return false;
            }
        }

        try {
            Files.writeString(fPath, fileContent);
            System.out.println("File written successfully to: " + filePath);
            return true;
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        return false;
    }

    public static String serializeToJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch(Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    public static boolean isValidPinCode(String pinCode) {
        if(pinCode == null || pinCode.isBlank()) {
            return false;
        }

        if(pinCode.length() != 6) {
            return false;
        }

        return pinCode.chars().allMatch(Character::isDigit);
    }
}
