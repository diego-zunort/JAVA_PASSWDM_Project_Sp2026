package model;

import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DIGITS = "0123456789";
    private static final String PUNCTUATION = "!@#$%&*()_+-=[]|,./?><";
    private static final SecureRandom RNG = new SecureRandom();

    private static char pick(String s) {
            return s.charAt(RNG.nextInt(s.length()));
    }
    
    public String generate(int length) {
        char[] pw = new char[length];

        // guarantee one of each
        pw[0] = pick(UPPERCASE);
        pw[1] = pick(LOWERCASE);
        pw[2] = pick(DIGITS);
        pw[3] = pick(PUNCTUATION);

        // fill the rest from all allowed chars
        String all = UPPERCASE + LOWERCASE + DIGITS + PUNCTUATION;
        for (int i = 4; i < length; i++) pw[i] = pick(all);

        // shuffle (Fisher–Yates)
        for (int i = pw.length - 1; i > 0; i--) {
        int j = RNG.nextInt(i + 1);
        char tmp = pw[i]; pw[i] = pw[j]; pw[j] = tmp;
        }
        return new String(pw);
    }

    public String checkStrength(String password) {
        // TODO: return strength rating (e.g. "Weak", "Medium", "Strong")
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

        boolean has3Repeat = false;
        for (int i = 0; i + 2 < length; i++) {
            char a = password.charAt(i);
            if (a == password.charAt(i + 1) && a == password.charAt(i + 2)) {
            has3Repeat = true;
            break;
            }
        }
        if (has3Repeat) score -= 2;

        if (length < 8 || classes < 2) return "weak";
        if (score >= 6) return "strong";
        return "medium";
    }
}

