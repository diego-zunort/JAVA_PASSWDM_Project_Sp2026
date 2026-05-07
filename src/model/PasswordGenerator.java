package model;

public class PasswordGenerator {

    public String generate(int length) {
        // TODO: generate random password of given length
        return "";
    }

    public String checkStrength(String password) {
        if (password == null) return "weak";

        int length = password.length();
        boolean hasLower = false, hasUpper = false, hasDigit = false, hasSymbol = false;
        boolean hasWhitespace = false;

        for (int i = 0; i < length; i++) {
            char c = password.charAt(i);
            if (Character.isWhitespace(c)) hasWhitespace = true;
            else if (Character.isLowerCase(c)) hasLower = true;
            else if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else hasSymbol = true;
        }

        if (hasWhitespace) return "weak";

        int classes = 0;
        if (hasLower) classes++;
        if (hasUpper) classes++;
        if (hasDigit) classes++;
        if (hasSymbol) classes++;

        boolean commonWeak =
            password.equalsIgnoreCase("password") ||
            password.equalsIgnoreCase("123456") ||
            password.equalsIgnoreCase("qwerty") ||
            password.equalsIgnoreCase("admin");

        if (commonWeak) return "weak";

        int score = 0;
        if (length >= 8) score++;
        if (length >= 12) score++;
        if (length >= 16) score++;
        if (classes >= 2) score++;
        if (classes >= 3) score++;
        if (classes == 4) score++;

        for (int i = 0; i + 2 < length; i++) {
            char a = password.charAt(i);
            if (a == password.charAt(i + 1) && a == password.charAt(i + 2)) {
                score -= 2;
                break;
            }
        }

        if (length < 8 || classes < 2) return "weak";
        if (score >= 6) return "strong";
        return "medium";
    }
}
