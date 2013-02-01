package org.owasp.validator.html;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Kristian Rosenvold
 */
public class StateMachineMapTest {
    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testOneKey() throws Exception {
        Map<String, String> items = getItems();
        StateMachineMap<String> matcher = new StateMachineMap<String>(true, items);
        assertEquals("cde", matcher.get("abc"));
        assertEquals("fff", matcher.get("abd"));
        assertNull(matcher.get("xy"));
        assertNull(matcher.get("a"));
    }

    @Test
    public void multiKeys() {
        String foo[] = new String[]{
                "colorName", "paragraph", "cssPsuedoElementExclusion", "cssIdentifier",
                "frequency", "cssPseudoElementSelector", "cssElementExclusion", "cssCommentText",
                "time", "cssClassExclusion", "positivePercentage", "length", "onsiteURL", "boolean",
                "cssOnsiteUri", "cssIDSelector", "htmlId", "offsiteURL", "cssClassSelector", "cssAttributeExclusion",
                "percentage", "integer", "cssIDExclusion", "absolute-size", "htmlClass", "cssElementSelector",
                "relative-size", "number", "rgbCode", "angle", "systemColor", "positiveLength", "positiveInteger",
                "cssOffsiteUri", "flashSites", "numberOrPercent", "anything", "htmlTitle", "singlePrintable",
                "cssAttributeSelector", "colorCode"};

        Map<String, String> things = new HashMap<String, String>();
        for (String s : foo) {
            things.put(s, "baz-" + s);
        }

         StateMachineMap<String> matcher = new StateMachineMap<String>(false, things);
        String s = matcher.get("relative-size");
        assertEquals("baz-relative-size", s);

    }

    @Test
    public void largerMap() throws Exception {
        Map<String, String> items = getMoreItems();
        StateMachineMap<String> matcher = new StateMachineMap<String>(false, items);
        assertEquals("cde", matcher.get("abcA"));
        assertEquals("fff", matcher.get("abcB"));
        assertEquals("fff", matcher.get("aBcB"));
        assertNull(matcher.get("xy"));
        assertNull(matcher.get("a"));
    }

    private Map<String, String> getItems() {
        Map<String, String> items = new HashMap<String, String>();
        items.put("abc", "cde");
        items.put("abd", "fff");
        return items;
    }

    private Map<String, String> getMoreItems() {
        Map<String, String> items = new HashMap<String, String>();
        items.put("abcA", "cde");
        items.put("abcB", "fff");
        items.put("yabcBOOOOOTY", "fff1");
        items.put("zabcBOOOOOTX", "fff2");
        items.put("pabcBOOOOOTX1", "fff3");
        items.put("qabcBOXOOOTX1", "fff3");
        return items;
    }
}
