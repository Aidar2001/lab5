package basePackage;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class PasswordUtils {

    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static final char[] ALPHABET = ("abcdefghijklmnopqrstuvwxyz"
            + "abcdefghijklmnopqrstuvwxyz".toUpperCase()
            + "0123456789").toCharArray();

    public static String generatePassword(int passwordLength) {
        if (passwordLength <= 0) {
            throw new IllegalArgumentException();
        }
        StringBuilder builder = new StringBuilder(passwordLength);
        for (int i = 0; i < passwordLength; ++i) {
            int randomIndex = (int) (Math.random() * ALPHABET.length);
            char randomChar = ALPHABET[randomIndex];
            builder.append(randomChar);
        }

        return builder.toString();
    }

    public static String getPasswordHash(String password) {
        return Hashing.sha512().hashString(password, StandardCharsets.UTF_8).toString();
    }
}
