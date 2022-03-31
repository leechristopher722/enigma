package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Christopher Lee
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void checkPermute() {
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        assertEquals('E', perm.permute('A'));
        assertEquals(4, perm.permute(0));
        assertEquals('I', perm.permute('V'));
        assertEquals(8, perm.permute(21));
        assertEquals('S', perm.permute('S'));
        assertEquals(18, perm.permute(18));
        assertEquals('A', perm.permute('U'));
        assertEquals(0, perm.permute(20));
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV)", UPPER);
        assertEquals('S', perm.permute('S'));
        assertEquals(18, perm.permute(18));
        assertEquals('J', perm.permute('J'));
        assertEquals(9, perm.permute(9));
    }

    @Test
    public void checkInvert() {
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        assertEquals('U', perm.invert('A'));
        assertEquals(20, perm.invert(0));
        assertEquals('I', perm.invert('V'));
        assertEquals('S', perm.invert('S'));
        assertEquals(8, perm.invert(21));
        assertEquals('Z', perm.invert('J'));
        assertEquals(25, perm.invert(9));
        assertEquals('R', perm.invert('U'));
        assertEquals(17, perm.invert(20));
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) (DFG) (IV)", UPPER);
        assertEquals('S', perm.permute('S'));
        assertEquals(18, perm.permute(18));
        assertEquals('J', perm.permute('J'));
        assertEquals(9, perm.permute(9));
    }

    @Test
    public void checkDerangement() {
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZ) (S)", UPPER);
        assertEquals(false, perm.derangement());
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV)", UPPER);
        assertEquals(false, perm.derangement());
        perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                + "(DFG) (IV) (JZS)", UPPER);
        assertEquals(true, perm.derangement());
    }

    @Test
    public void checkAddCycle() {
        perm = new Permutation("(AELTPHQXRU)", UPPER);
        perm.addCycle("J");
        perm.addCycle("Z");
    }

}
