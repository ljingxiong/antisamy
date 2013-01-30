package org.owasp.validator.html;

import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author Kristian Rosenvold
 */
public class StateMachineMatcherTest {
    @Test
    public void testOneKey() throws Exception {
       StateMachineMatcher matcher = new StateMachineMatcher(true, '@', 0, "abc");
        assertFalse(matcher.matches("cdef"));
        assertTrue(matcher.matches("abc"));
        assertFalse( matcher.matches("a"));
    }

    @Test
    public void testMatches() throws Exception {
        StateMachineMatcher matcher = new StateMachineMatcher(true, '@', 0, "abc", "cde");
        assertTrue( matcher.matches("cde"));
        assertFalse(matcher.matches("cdef"));
        assertTrue(matcher.matches("abc"));
        assertFalse( matcher.matches("a"));
    }
}
