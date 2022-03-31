package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Christopher Lee
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine M = readConfig();
        boolean hasSet = false;
        while (_input.hasNextLine()) {
            String inputLine = _input.nextLine();
            if (inputLine.length() == 0) {
                _output.println();
                continue;
            }
            if (inputLine.startsWith("*")) {
                setUp(M, inputLine);
                hasSet = true;
                if (!M.getRotors().get(0).reflecting()) {
                    throw error("First rotor is not a reflector");
                }
                int numMoving = 0;
                for (int i = 0; i < M.numRotors(); i++) {
                    if (M.getRotors().get(i).rotates()) {
                        numMoving += 1;
                    }
                }
                if (numMoving != M.numPawls()) {
                    throw error("Wrong number of moving rotors");
                }
            } else if (hasSet) {
                printMessageLine(M.convert(inputLine));
            }
        }
        if (!hasSet) {
            throw error("Missing setting");
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            _alphabet = new Alphabet(_config.nextLine());
            int numRotors = _config.nextInt();
            int numPawls = _config.nextInt();
            ArrayList<Rotor> allRotors = new ArrayList<>();
            String currRotorLine = "";
            if (_config.hasNextLine()) {
                currRotorLine = _config.nextLine();

            }
            String nextLine = "some";
            while (_config.hasNextLine()) {
                if (!currRotorLine.equals(nextLine)) {
                    currRotorLine = _config.nextLine();
                }

                if (_config.hasNextLine()) {
                    nextLine = _config.nextLine();
                }

                if (nextLine.trim().startsWith("(")) {
                    currRotorLine = currRotorLine + " " + nextLine.trim();
                    if (_config.hasNextLine()) {
                        nextLine = _config.nextLine();
                    }
                }

                if (currRotorLine.split(" ").length < 2) {
                    continue;
                }
                allRotors.add(readRotor(currRotorLine));
                currRotorLine = nextLine;
                if (!_config.hasNextLine() && !nextLine.trim().startsWith("(")
                        && currRotorLine.length() != 0) {
                    allRotors.add(readRotor(currRotorLine));
                }
            }
            return new Machine(_alphabet, numRotors, numPawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config.
     * @param rotorLine Line of rotor. */
    private Rotor readRotor(String rotorLine) {
        try {
            Scanner rotorSet = new Scanner(rotorLine);
            String name = rotorSet.next();
            String typeNotch = rotorSet.next();
            String cycles = "";
            while (rotorSet.hasNext()) {
                String currCycle = rotorSet.next();
                if (!currCycle.startsWith("(") || !currCycle.endsWith(")")) {
                    throw error("Bad permutation for rotor");
                }
                cycles = cycles + " " + currCycle;
            }
            Permutation perm = new Permutation(cycles, _alphabet);
            if (typeNotch.charAt(0) == 'M') {
                String notches = typeNotch.substring(1);
                return new MovingRotor(name, perm, notches);
            } else if (typeNotch.charAt(0) == 'N') {
                return new FixedRotor(name, perm);
            } else {
                return new Reflector(name, perm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        Scanner settingScan = new Scanner(settings);
        if (settingScan.next().charAt(0) != '*') {
            throw error("Settings should start with *");
        }
        String[] rotorNames = new String[M.numRotors()];
        for (int i = 0; i < M.numRotors(); i++) {
            rotorNames[i] = settingScan.next();
        }
        String setting = "";
        String plugboard = "";
        if (settingScan.hasNext()) {
            setting = settingScan.next();
            if (setting.startsWith("(") && setting.endsWith(")")) {
                plugboard = setting;
                setting = "";
            }
        }

        while (settingScan.hasNext()) {
            plugboard = plugboard + " " + settingScan.next();
            if (!plugboard.trim().startsWith("(")
                    || !plugboard.trim().endsWith(")")) {
                throw error("Incorrect plugboard input");
            }
        }
        Permutation plugboardPerm = new Permutation(plugboard, _alphabet);
        M.insertRotors(rotorNames);
        M.setPlugboard(plugboardPerm);
        M.setRotors(setting);
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String result = "";
        for (int i = 0; i < msg.length(); i += 5) {
            if (i + 5 >= msg.length()) {
                result = result + msg.substring(i) + " ";
            } else {
                result = result + msg.substring(i, i + 5) + " ";
            }
        }
        _output.println(result);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
