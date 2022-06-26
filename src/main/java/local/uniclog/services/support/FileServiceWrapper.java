package local.uniclog.services.support;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
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

    @SneakyThrows({FileNotFoundException.class, IOException.class})
    public static <T> void saveObject(String path, T object) {
        try (var fos = new FileOutputStream(path);
             var oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        }
    }

    @SneakyThrows({FileNotFoundException.class, IOException.class, ClassNotFoundException.class})
    public static <T> T loadObject(String path, Class<T> objectType) {
        try (var fis = new FileInputStream(path);
             var ois = new ObjectInputStream(fis)) {
            return objectType.cast(ois.readObject());
        }
    }
}
