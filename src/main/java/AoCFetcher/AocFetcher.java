package AoCFetcher;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AocFetcher {
    public static void main(String[] args) {
        Fetcher fetcher = null;
        try {
            fetcher = Fetcher.tryFromFile("session.txt");
        } catch (FileNotFoundException|FileNotValidException e) {
            System.out.println("File \"session.txt\" not found or invalid");
            System.exit(1);
        }

        String problem = null;
        try {
            problem = fetcher.fetch(2021, 1);
        } catch (IOException|InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println(problem);
    }
}

