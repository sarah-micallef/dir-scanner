package com.dirscanner;

import com.dirscanner.scanner.DirectoryElementScanner;

import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Created by Sarah Micallef on 13/06/2017.
 */
public class DU {

    public static void main(final String[] args) {

        final Scanner scanner = new Scanner(System.in);

        System.out.print("Enter absolute path of directory: ");

        final String absolutePath = scanner.nextLine();

        try {
            new DirectoryElementScanner()
                    .scan(Paths.get(absolutePath))
                    .forEach(elem -> System.out.println(elem.toString()));
        } catch (final NoSuchFileException e) {
            System.out.println("Given path does not exist. Please enter a valid path.");
            e.printStackTrace();
        } catch (final NotDirectoryException e) {
            System.out.println("Please enter a path to a directory, and not a file.");
        }
    }
}
