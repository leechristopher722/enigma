package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Christopher Lee
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numRotors = numRotors;
        _pawls = pawls;
        _allRotors = allRotors;
        _rotors = new ArrayList<Rotor>();
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Gets current rotors.
     * @return ArrayList<></> */
    ArrayList<Rotor> getRotors() {
        return _rotors;
    }

    /** Returns all rotors of Machine. */
    Collection<Rotor> getAllRotors() {
        return _allRotors;
    }

    /** Returns alphabet. */
    Alphabet getAlphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotors = new ArrayList<Rotor>();
        for (String rotorName : rotors) {
            boolean added = false;
            for (Rotor eachRotor : _allRotors) {
                if (eachRotor.name().equals(rotorName)) {
                    _rotors.add(eachRotor);
                    added = true;
                    break;
                }
            }
            if (!added) {
                throw error("_allRotors does not contain such rotor input");
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        for (int i = 0; i < setting.length(); i++) {
            _rotors.get(i + 1).set(_alphabet.toInt(setting.charAt(i)));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        int input = _plugboard.permute(c);
        boolean doubleStepping = false;
        for (int i = 1; i < _rotors.size(); i++) {
            Rotor currentRotor = _rotors.get(i);
            if (currentRotor.rotates()) {
                if (i == _rotors.size() - 1) {
                    currentRotor.advance();
                } else if (_rotors.get(i + 1).atNotch()) {
                    currentRotor.advance();
                    doubleStepping = true;
                } else if (doubleStepping) {
                    currentRotor.advance();
                    doubleStepping = false;
                }
            }
        }
        for (int i = _rotors.size() - 1; 0 <= i; i--) {
            Rotor currentRotor = _rotors.get(i);
            input = currentRotor.convertForward(input);
        }
        for (int j = 1; j < _rotors.size(); j++) {
            Rotor currentRotor = _rotors.get(j);
            input = currentRotor.convertBackward(input);
        }
        input = _plugboard.permute(input);
        return input;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String result = "";
        msg = msg.replaceAll(" ", "");
        for (int i = 0; i < msg.length(); i++) {
            result += _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(i))));
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private final int _numRotors;

    /** Number of pawls. */
    private final int _pawls;

    /** Collection of all rotors. */
    private Collection<Rotor> _allRotors;

    /** Arraylist of rotors. */
    private ArrayList<Rotor> _rotors;

    /** Plugboard of machine. */
    private Permutation _plugboard;
}
