package local.uniclog.services.support;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.Scanner;

import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;
import static local.uniclog.utils.ConfigConstants.EMPTY;
import static local.uniclog.utils.ConfigConstants.TEMPLATE_UTILITY_CLASS;

@Slf4j
public class FileServiceWrapper {

    private FileServiceWrapper() {
        throw new IllegalStateException(TEMPLATE_UTILITY_CLASS);
    }

    public static <T> T saveObjectAsText(T object, String path) {
        try (var writer = new FileWriter(path)) {
            writer.write(object.toString());
            return object;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static String loadObjectFromTextFile(String path) {
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

    public static String getFileName(String path) {
        if (EMPTY.equals(path) || path.lastIndexOf("\\") == -1)
            return EMPTY;

        var begin = path.lastIndexOf("\\") + 1;
        var end = path.indexOf(".", begin);
        return end == -1 ? path.substring(begin) : path.substring(begin, end);
    }

    public static <T> T saveObjectAsJson(String path, T object, Type type) {
        if (nonNull(saveObjectAsText(new Gson().toJson(object, type), path))) {
            return object;
        } else return null;
    }

    public static <T> T loadObjectFromJson(String path, Type objectType) {
        try {
            return new Gson().fromJson(requireNonNull(loadObjectFromTextFile(path)), objectType);
        } catch (NullPointerException | JsonSyntaxException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
