package org.owasp.validator.html;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kristian Rosenvold
 */
public class StateMachineMatcher {
    private static final char A = 'A';
    private static final char z = 'z';
    private final StateMachineMatcher[] directMatches = new StateMachineMatcher[z - A];
    private final Map<Character, StateMachineMatcher> indirect = new HashMap<Character, StateMachineMatcher>();
    private boolean canTerminate = false;
    private char character;

    public StateMachineMatcher(boolean caseSensitive, char character, int pos, CharSequence... strings) {
        this.character = character;
        for (CharSequence string : strings) {
            int len = string.length();
            if (pos < len) {
                char c = string.charAt(pos);
                if (pos == 0 || character == string.charAt(pos - 1)) {
                    StateMachineMatcher matcher = getOrCreateMatcher(caseSensitive, pos + 1, c, strings);
                    matcher.canTerminate = pos == (len - 1);
                    char other = Character.isLowerCase(c) ? Character.toUpperCase(c) : Character.toLowerCase(c);
                    if (!caseSensitive && other != c) {
                        matcher = getOrCreateMatcher(caseSensitive, pos + 1, c, string);
                        matcher.canTerminate = pos == (len - 1);
                    }
                }
            }
        }
    }

    private StateMachineMatcher getOrCreateMatcher(boolean caseSensitive, int pos, char c, CharSequence... strings) {
        StateMachineMatcher matcher = getMatcher(c);
        if (matcher == null) {
            matcher = new StateMachineMatcher(caseSensitive, c, pos, strings);
            setMatcher(c, matcher);
        }
        return matcher;
    }


    public boolean matches(CharSequence charSequence) {
        return matches(charSequence, 0);
    }

    private boolean matches(CharSequence charSequence, int pos) {
        if (pos >= charSequence.length()) return canTerminate;
        char c = charSequence.charAt(pos);
        StateMachineMatcher matcher;
        matcher = getMatcher(c);
        return matcher != null && matcher.matches(charSequence, ++pos);
    }

    private StateMachineMatcher getMatcher(char c) {
        StateMachineMatcher matcher;
        if (c >= A && c <= z) {
            matcher = directMatches[c - A];
        } else {
            matcher = indirect.get(c);
        }
        return matcher;
    }

    private void setMatcher(char c, StateMachineMatcher matcher) {
        if (c >= A && c <= z) {
            directMatches[c - A] = matcher;
        } else {
            indirect.put(c, matcher);
        }
    }

    @Override
    public String toString() {
        return "StateMachineMatcher{" + character + '}';
    }
}
