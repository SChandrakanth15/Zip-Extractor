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

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Enter the Folder that containing the zip files.");
        Scanner userInput = new Scanner(System.in);
        S zipFolderPath = userInput;  // Folder containing ZIP files
        String extractFolderPath = "C:\\Users\\YourName\\Downloads\\Extracted"; // Extraction folder
        Files.createDirectories(Paths.get(extractFolderPath));
        File[] zipFiles = new File(zipFolderPath).listFiles((dir, name) -> name.endsWith(".zip"));
        if (zipFiles != null) {
            for (File zipFile : zipFiles) {
                extractZip(zipFile, extractFolderPath + "\\" + zipFile.getName().replace(".zip", ""));
            }
            System.out.println("All ZIP files extracted!");
        }
    }

    private static void extractZip(File zipFile, String destDir) throws IOException {
        Files.createDirectories(Paths.get(destDir));
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(destDir, entry.getName());
                if (entry.isDirectory()) {
                    newFile.mkdirs();
                } else {
                    new File(newFile.getParent()).mkdirs();
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