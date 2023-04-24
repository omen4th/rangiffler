package org.rangiffler.utils;

import org.rangiffler.condition.PhotoCondition;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class PhotoUtils {

    public static byte[] getPhotoByteFromClasspath(String photoPath) {
        ClassLoader classLoader = PhotoCondition.class.getClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(photoPath)) {
            byte[] photoBytes = is.readAllBytes();
            String base64 = Base64.getEncoder().encodeToString(photoBytes);
            return String.format("data:image/jpeg;base64,%s", base64).getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
