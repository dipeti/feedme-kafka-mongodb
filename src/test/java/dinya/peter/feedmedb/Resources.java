package dinya.peter.feedmedb;

import java.io.IOException;
import java.nio.file.Path;

import static java.nio.file.Files.readAllBytes;

public class Resources {
    public static String EVENT_CREATE_MESSAGE;
    public static String EVENT_UPDATE_MESSAGE;
    public static String MARKET_CREATE_MESSAGE;
    public static String MARKET_UPDATE_MESSAGE;
    public static String OUTCOME_CREATE_MESSAGE;
    public static String OUTCOME_UPDATE_MESSAGE;


    static {
        try {
            EVENT_CREATE_MESSAGE = readFile("json/event-create.json");
            EVENT_UPDATE_MESSAGE = readFile("json/event-update.json");
            MARKET_CREATE_MESSAGE = readFile("json/market-create.json");
            MARKET_UPDATE_MESSAGE = readFile("json/market-update.json");
            OUTCOME_CREATE_MESSAGE = readFile("json/outcome-create.json");
            OUTCOME_UPDATE_MESSAGE = readFile("json/outcome-update.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readFile(String s) throws IOException {
        return new String(readAllBytes(Path.of("src", "test", "resources", s)));
    }
}
