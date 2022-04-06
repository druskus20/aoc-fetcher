package AoCFetcher;

import jdk.jshell.execution.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Scanner;

public class Fetcher {
    private String sessionId;

    private Fetcher(String sessionId) {
        this.sessionId = sessionId;
    }

    public static Fetcher tryFromFile(String sessionFile)
            throws FileNotFoundException, FileNotValidException {

        if (Utils.fileIsValid(sessionFile)) {
            File f = new File(sessionFile);
            Scanner scanner = new Scanner(f);
            String sessionId = scanner.nextLine();
            return new Fetcher(sessionId);
        }

        throw new FileNotValidException();
    }

    public String fetch(int year, int day) throws IOException, InterruptedException {
        HttpCookie sessionCookie = new HttpCookie("session", this.sessionId);
        sessionCookie.setPath("/");
        sessionCookie.setVersion(0);

        CookieHandler.setDefault(new CookieManager());
        try {
            ((CookieManager) CookieHandler.getDefault()).getCookieStore().add(new URI("https://adventofcode.com"), sessionCookie);
        }
        catch (URISyntaxException impossible) {
            // The URI is hardcoded so this cant happen
        }

        var client = HttpClient.newBuilder()
                .cookieHandler(CookieHandler.getDefault())
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        var url = URI.create(String.format("https://adventofcode.com/%s/day/%s/input", year, day));
        var request = HttpRequest
                .newBuilder(url)
                .build();
        var response  = client.send(request, BodyHandlers.ofString());

        return response.body();
    }

    private static class Utils {
        private static Boolean fileIsValid(String filename) {
            Path path = Paths.get(filename);
            try {
                return Files.lines(path).count() == 1;
            }
            catch (IOException e) {
                return false;
            }
        }
    }
};
