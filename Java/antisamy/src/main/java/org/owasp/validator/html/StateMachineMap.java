package org.owasp.validator.html;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Kristian Rosenvold
 */
public class StateMachineMap<T> {
    private static final char A = 'A';
    private static final char z = 'z';
    @SuppressWarnings("unchecked")
    private final StateMachineMap<T>[] directMatches = new StateMachineMap[(z - A) + 1];
    private final Map<Character, StateMachineMap<T>> indirect = new HashMap<Character, StateMachineMap<T>>();
    private char character;
    private T item;
    private final boolean caseSensitive;

    public StateMachineMap(boolean caseSensitive, Map<String, T> items) {
        this(caseSensitive, '@', items);
    }

    public StateMachineMap(boolean caseSensitive,  char character) {
        this.character = character;
        this.caseSensitive = caseSensitive;
    }
    private StateMachineMap(boolean caseSensitive, char character, Map<String, T> items) {
        this.character = caseSensitive ? character : Character.toLowerCase(character);
        this.caseSensitive = caseSensitive;
        for (String string : items.keySet()) {
            StateMachineMap<T> currentMatcher = this;
            for (int i = 0; i < string.length(); i++){
                char c = string.charAt(i);
                if (!caseSensitive){
                    c = Character.toLowerCase(c);
                }
                currentMatcher = currentMatcher.getOrCreateMatcher2(caseSensitive, c);
            }
            currentMatcher.item = items.get(string);
        }
    }

    private StateMachineMap<T> getOrCreateMatcher2(boolean caseSensitive, char c) {
        StateMachineMap<T> matcher = getMatcher(c);
        if (matcher == null) {
            matcher = new StateMachineMap<T>(caseSensitive, c);
            setMatcher(c, matcher);
        }
        return matcher;
    }

    public T get(String charSequence) {
        return get(charSequence, 0);
    }

    private T get(String charSequence, int pos) {
        if (pos >= charSequence.length()) return item;
        char c = charSequence.charAt(pos);
        if (!caseSensitive ) c = Character.toLowerCase(c);
        StateMachineMap<T> matcher = getMatcher(c);
        return matcher != null ? matcher.get(charSequence, ++pos) : null;
    }

    private StateMachineMap<T> getMatcher(char c) {
        if (c >= A && c <= z) {
            return directMatches[c - A];
        } else {
            return indirect.get(c);
        }
    }

    private void setMatcher(char c, StateMachineMap<T> matcher) {
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
