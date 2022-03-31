package enigma;

import java.util.Arrays;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Christopher Lee
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notch = notches.split("");
        set(0);
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    /** Return true iff I have a ratchet and can move. */
    @Override
    boolean rotates() {
        return true;
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    @Override
    boolean atNotch() {
        return Arrays.asList(_notch).contains(Character
                        .toString(permutation().alphabet().toChar(setting())));
    }

    /** Notch of rotor. */
    private String[] _notch;
}
