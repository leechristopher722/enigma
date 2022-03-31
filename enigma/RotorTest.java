package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Rotor class.
 *  @author Christopher Lee
 */
public class RotorTest {

    private Permutation perm;
    private String alpha = UPPER_STRING;
    private Rotor rotor;

    @Test
    public void checkConvertForward() {
        perm = new Permutation("(AELTPHQXRU) "
                + "(BKNW) (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        rotor = new Rotor("Chris", perm);
        rotor.set(5);
        assertEquals(1, rotor.convertForward(0));
        assertEquals(25, rotor.convertForward(21));
        assertEquals(13, rotor.convertForward(13));
        assertEquals(3, rotor.convertForward(16));
        rotor.set(0);
        assertEquals(4, rotor.convertForward(0));
    }

    @Test
    public void checkConvertBackward() {
        perm = new Permutation("(AELTPHQXRU) (BKNW) "
                + "(CMOY) (DFG) (IV) (JZ) (S)", UPPER);
        rotor = new Rotor("ChrisLee", perm);
        rotor.set(5);
        assertEquals(24, rotor.convertBackward(0));
        assertEquals(15, rotor.convertBackward(21));
        assertEquals(13, rotor.convertBackward(13));
        assertEquals(3, rotor.convertBackward(16));
        rotor.set(0);
        assertEquals(20, rotor.convertBackward(0));
    }
}
