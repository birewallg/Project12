package local.uniclog.services;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

@Slf4j
public class FileServiceWrapper {
    public static final String CONFIG_LIST_JSON = "config_list.json";

    public void write(String object) {
        try (var writer = new FileWriter(CONFIG_LIST_JSON)) {
            writer.write(object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public String read() {
        try (var reader = new FileReader(CONFIG_LIST_JSON); var scan = new Scanner(reader)) {
            var builder = new StringBuilder();
            while (scan.hasNextLine()) {
                builder.append(scan.nextLine()).append("\n");
            }
            return builder.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
