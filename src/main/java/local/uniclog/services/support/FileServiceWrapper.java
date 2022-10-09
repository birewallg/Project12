package local.uniclog.services.support;

import com.google.gson.Gson;
import local.uniclog.ui.controlls.model.MacrosItem;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Scanner;

import static java.util.Objects.requireNonNull;
import static local.uniclog.utils.ConfigConstants.EMPTY;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class FileServiceWrapper {

    private FileServiceWrapper() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    public static MacrosItem write(String object, String path) {
        try (var writer = new FileWriter(path)) {
            writer.write(object);
            return new MacrosItem(getFileName(path), object, path);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static MacrosItem read(String path) {
        try (var reader = new FileReader(path); var scan = new Scanner(reader)) {
            var builder = new StringBuilder();
            while (scan.hasNextLine()) {
                builder.append(scan.nextLine()).append("\n");
            }
            return new MacrosItem(getFileName(path), builder.toString(), path);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String getFileName(String path) {
        if (EMPTY.equals(path) || path.lastIndexOf("\\") == -1)
            return EMPTY;

        var begin = path.lastIndexOf("\\") + 1;
        var end = path.indexOf(".", begin);
        return end == -1 ? path.substring(begin) : path.substring(begin, end);
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

    public static void saveJson(String path, Object object, Type type) {
        write(new Gson().toJson(object, type), path);
    }

    public static <T> T loadJson(String path, Type objectType) {
        return new Gson().fromJson(requireNonNull(read(path)).getText(), objectType);
    }
}
