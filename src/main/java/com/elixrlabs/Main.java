package com.elixrlabs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) {
        Scanner userInput = new Scanner(System.in);

        // Get the folder path containing ZIP files
        System.out.println("Enter the folder that contains the ZIP files:");
        String zipFolderPath = userInput.nextLine();

        // Validate if the ZIP folder exists
        File zipFolder = new File(zipFolderPath);
        if (!zipFolder.exists() || !zipFolder.isDirectory()) {
            System.out.println("Error: The provided ZIP folder path is invalid or does not exist.");
            return;
        }

        // Get the destination folder for extraction
        System.out.println("Enter the folder where you want to extract the ZIP files:");
        String extractFolderPath = userInput.nextLine();
        userInput.close(); // Close scanner to prevent resource leaks

        try {
            Files.createDirectories(Paths.get(extractFolderPath)); // Ensure extraction folder exists

            File[] zipFiles = zipFolder.listFiles((dir, name) -> name.endsWith(".zip"));

            if (zipFiles == null || zipFiles.length == 0) {
                System.out.println("No ZIP files found in the provided folder.");
                return;
            }

            // Extract all ZIP files
            for (File zipFile : zipFiles) {
                String destinationPath = extractFolderPath + File.separator + zipFile.getName().replace(".zip", "");
                extractZip(zipFile, destinationPath);
            }

            System.out.println("All ZIP files extracted successfully!");
        } catch (IOException e) {
            System.err.println("Error during extraction: " + e.getMessage());
        }
    }

    private static void extractZip(File zipFile, String destDir) throws IOException {
        Files.createDirectories(Paths.get(destDir)); // Ensure extraction directory exists

        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());

                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs(); // Create parent directories
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        }
    }
}
