package local.uniclog.services;

import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Scanner;

import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class FileServiceWrapper {

    private FileServiceWrapper() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    public static void write(String object, String path) {
        try (var writer = new FileWriter(path)) {
            writer.write(object);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static String read(String path) {
        try (var reader = new FileReader(path); var scan = new Scanner(reader)) {
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
