package enigma;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Christopher Lee
 */
public class MachineTest {
    Permutation perm = new Permutation("(AELTPHQXRU) (BKNW) (CMOY) "
                                   + "(DFG) (IV) (JZ) (S)", UPPER);
    Permutation permIV = new Permutation("(AEPLIYWCOXMRFZBSTGJQNH) "
            + "(DV) (KU)", UPPER);
    Permutation permIII = new Permutation("(ABDHPEJT) "
            + "(CFLVMZOYQIRWUKXSG) (N)", UPPER);
    Permutation permReflector = new Permutation("(AE) (BN) "
            + "(CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP) (RX) (SZ) (TV)", UPPER);
    Permutation permBeta = new Permutation("(ALBEVFCYODJWUGNMQTZSKPR) "
            + "(HIX)", UPPER);
    Reflector reflector = new Reflector("reflector", permReflector);
    MovingRotor rotor1 = new MovingRotor("1", perm, "Q");
    MovingRotor rotor2 = new MovingRotor("2", perm, "Q");
    MovingRotor rotor3 = new MovingRotor("3", perm, "Q");
    FixedRotor rotorBeta = new FixedRotor("Beta", permBeta);
    MovingRotor _IV = new MovingRotor("_IV", permIV, "J");
    MovingRotor _III = new MovingRotor("_III", permIII, "V");
    Collection<Rotor> allRotors = new ArrayList<Rotor>();
    Alphabet alpha = new Alphabet();

    @Test
    public void checkInsertRotors() {
        allRotors.add(reflector);
        allRotors.add(rotor1);
        allRotors.add(rotor2);
        allRotors.add(rotor3);
        Machine mach = new Machine(alpha, 4, 0, allRotors);
        String[] inputString = {"reflector", "1", "2", "3"};
        assertEquals(4, mach.numRotors());
        mach.insertRotors(inputString);
        assertEquals(allRotors, mach.getRotors());
    }

    @Test
    public void checkSetRotors() {
        allRotors.add(reflector);
        allRotors.add(rotor1);
        allRotors.add(rotor2);
        allRotors.add(rotor3);
        Machine mach = new Machine(alpha, 4, 3, allRotors);
        String[] inputString = {"reflector", "1", "2", "3"};
        assertEquals(4, mach.numRotors());
        mach.insertRotors(inputString);
        assertEquals(allRotors, mach.getRotors());
        String input = "ABC";
        mach.setRotors(input);
        assertEquals(0, mach.getRotors().get(1).setting());
        assertEquals(1, mach.getRotors().get(2).setting());
    }

    @Test
    public void checkConvert() {
        allRotors.add(reflector);
        allRotors.add(rotor1);
        MovingRotor newRotor = new MovingRotor("new", perm, "A");
        allRotors.add(newRotor);
        allRotors.add(rotor2);
        allRotors.add(rotor3);
        allRotors.add(rotorBeta);
        allRotors.add(_IV);
        allRotors.add(_III);
        Machine mach = new Machine(alpha, 4, 3, allRotors);
        String[] inputString = {"reflector", "1", "2", "3"};
        mach.insertRotors(inputString);
        Permutation plugPerm = new Permutation("(AB) (HK)", UPPER);
        mach.setPlugboard(plugPerm);
        assertEquals(12, mach.convert(2));
        assertEquals(25, mach.convert(2));


        Machine mach2 = new Machine(alpha, 4, 3, allRotors);
        String[] inputString2 = {"reflector", "new", "2", "new"};
        mach2.insertRotors(inputString2);
        Permutation plugPerm2 = new Permutation("(AB) (HK)", UPPER);
        mach2.setPlugboard(plugPerm2);
        mach2.convert(2);
        System.out.println(mach2.getRotors().get(3).setting());
        System.out.println(mach2.getRotors().get(2).setting());
        System.out.println(mach2.getRotors().get(1).setting());
        System.out.println(mach2.getRotors().get(0).setting());
        mach2.convert(2);
        System.out.println(mach2.getRotors().get(3).setting());
        System.out.println(mach2.getRotors().get(2).setting());
        System.out.println(mach2.getRotors().get(1).setting());
        System.out.println(mach2.getRotors().get(0).setting());

        Machine mach1 = new Machine(alpha, 5, 3, allRotors);
        String[] inputString1 = {"reflector", "Beta", "_III", "_IV", "1"};
        mach1.insertRotors(inputString1);
        Permutation plugPerm1 = new Permutation("(YF) (ZH)", UPPER);
        mach1.setPlugboard(plugPerm1);
        mach1.setRotors("AXLE");
        for (int i = 0; i < 12; i++) {
            mach1.convert(i);
        }
        mach1.convert(0);
        System.out.println(mach1.getRotors().get(1).setting());
        System.out.println(mach1.getRotors().get(2).setting());
        System.out.println(mach1.getRotors().get(3).setting());
        System.out.println(mach1.getRotors().get(4).setting());
    }

    @Test
    public void checkConvertString() {
        allRotors.add(reflector);
        allRotors.add(rotor1);
        allRotors.add(rotor2);
        allRotors.add(rotor3);
        allRotors.add(rotorBeta);
        allRotors.add(_IV);
        allRotors.add(_III);
        Machine mach = new Machine(alpha, 4, 3, allRotors);
        mach.insertRotors(new String[]{"reflector", "1", "2", "3"});
        Permutation plugPerm = new Permutation("(AB) (HK)", UPPER);
        mach.setPlugboard(plugPerm);
        String inputString = "C C";
        assertEquals("MZ", mach.convert(inputString));
    }
}
