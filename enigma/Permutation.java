package enigma;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Christopher Lee
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        _eachCycle = _cycles.replaceAll("[()]", "").split(" ");
    }

    /** Returns each cycle of a permutation. */
    String[] eachCycle() {
        return _eachCycle;
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    public void addCycle(String cycle) {
        String[] newEachCycle = new String[_eachCycle.length + 1];
        for (int i = 0; i < _eachCycle.length; i++) {
            newEachCycle[i] = _eachCycle[i];
        }
        newEachCycle[_eachCycle.length] = cycle;
        _eachCycle = newEachCycle;
    }

    /** Adds missing cycles that are contained in the _alphabet. */
    public void addMissingCycles() {
        String remain = _alphabet.toString();
        for (int i = 0; i < _alphabet.size(); i++) {
            char curLetter = _alphabet.toChar(i);
            String current = Character.toString(curLetter);
            for (String oneCycle : _eachCycle) {
                if (oneCycle.contains(current)) {
                    remain = remain.replace(current, "");
                }
            }
        }
        String [] leftovers = remain.split("");
        for (String left : leftovers) {
            addCycle(left);
        }
    }

    /** Returns the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char inputChar = _alphabet.toChar(wrap(p));
        char outputChar = permute(inputChar);
        return _alphabet.toInt(outputChar);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char inputChar = _alphabet.toChar(wrap(c));
        char outputChar = invert(inputChar);
        return _alphabet.toInt(outputChar);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        char permutedChar = p;
        for (String group : _eachCycle) {
            if (group.contains(Character.toString(p))) {
                int index = group.indexOf(p);
                if (index == group.length() - 1) {
                    permutedChar = group.charAt(0);
                } else {
                    permutedChar = group.charAt(index + 1);
                }
                break;
            }
        }
        return permutedChar;
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        char invertedChar = c;
        for (String group : _eachCycle) {
            if (group.contains(Character.toString(c))) {
                int index = group.indexOf(c);
                if (index == 0) {
                    invertedChar = group.charAt(group.length() - 1);
                } else {
                    invertedChar = group.charAt(index - 1);
                }
                break;
            }
        }
        return invertedChar;
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        int lettersSoFar = 0;
        for (String oneCycle : _eachCycle) {
            if (oneCycle.length() == 1) {
                return false;
            }
            lettersSoFar += oneCycle.length();
        }
        if (lettersSoFar < _alphabet.size()) {
            return false;
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private String _cycles;

    /** Each cycle of this permutation. */
    private String[] _eachCycle;
}
