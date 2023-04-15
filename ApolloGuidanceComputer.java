import java.util.*;

public class ApolloGuidanceComputer {
    private final int MEMORY_SIZE = 2048;
    private final int[] memory = new int[MEMORY_SIZE];
    private int programCounter = 0;
    private int accumulator = 0;
    private Scanner scanner = new Scanner(System.in); // Create a single Scanner object for the entire program

    public void setMemory(int address, int value) {
        if (address >= 0 && address < MEMORY_SIZE) {
            memory[address] = value;
        }
    }

    public int getMemory(int address) {
        if (address >= 0 && address < MEMORY_SIZE) {
            return memory[address];
        }
        return -1;
    }

    public void setProgramCounter(int value) {
        programCounter = value;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setAccumulator(int value) {
        accumulator = value;
    }

    public int getAccumulator() {
        return accumulator;
    }

    public enum Instruction {
        LOAD, ADD, SUB, MUL, DIV, JUMP, JZ, JN, IN, OUT, TS, HALT
    }

    public void execute(Instruction instruction, int operand) {
        switch (instruction) {
            case LOAD:
                accumulator = getMemory(operand);
                break;
            case ADD:
                accumulator += getMemory(operand);
                // System.out.println("Accumulator after ADD: " + accumulator);
                break;
            case SUB:
                accumulator -= getMemory(operand);
                break;
            case MUL:
                accumulator *= getMemory(operand);
                break;
            case DIV:
                accumulator /= getMemory(operand);
                break;
            case JUMP:
                programCounter = operand;
                return;
            case JZ:
                if (accumulator == 0) {
                    programCounter = operand;
                } else {
                    programCounter += 2;
                }
                return;
            case JN:
                if (accumulator < 0) {
                    programCounter = operand;
                } else {
                    programCounter += 2;
                }
                return;
            case IN:
                System.out.print("Enter a value: ");
                int input = scanner.nextInt();
                setMemory(operand, input);
                break;
            case OUT:
                System.out.println("Output: " + getMemory(operand));
                break;
            case TS:
                setMemory(operand, accumulator);
                break;
            case HALT:
                System.out.println("Halted");
                break;
        }programCounter+=2;

    // System.out.println("Program Counter: " + programCounter);
    // System.out.println("Accumulator: " + accumulator);
    // System.out.println("Memory: " + Arrays.toString(memory));
    }

    public double calculateDeltaV(double initialMass, double finalMass, double specificImpulse) {
        double deltaV = specificImpulse * 9.81 * Math.log(initialMass / finalMass);
        return deltaV;
    }

    public void nextInstruction() {
        Instruction instruction = Instruction.values()[getMemory(programCounter)];
        int operand = getMemory(programCounter + 1);
    
        if (instruction == Instruction.HALT) {
            System.out.println("Halted");
            return;
        }
    
        execute(instruction, operand);
    }
    
    public static void main(String[] args) {
        ApolloGuidanceComputer agc = new ApolloGuidanceComputer();
    
        // Load a simple program into memory
        // This program adds two numbers (input by the user) and outputs the result
        agc.setMemory(0, Instruction.IN.ordinal());
        agc.setMemory(1, 10);
        agc.setMemory(2, Instruction.IN.ordinal());
        agc.setMemory(3, 11);
        agc.setMemory(4, Instruction.LOAD.ordinal());
        agc.setMemory(5, 10);
        agc.setMemory(6, Instruction.ADD.ordinal());
        agc.setMemory(7, 11);
        agc.setMemory(8, Instruction.TS.ordinal());
        agc.setMemory(9, 12);
        agc.setMemory(10, Instruction.OUT.ordinal());
        agc.setMemory(11, 12);
        agc.setMemory(12, Instruction.HALT.ordinal());
        agc.setMemory(13, 0);
    
        // Execute the program
        while (agc.getMemory(agc.getProgramCounter()) != Instruction.HALT.ordinal()) {
            agc.nextInstruction();
        }
        agc.scanner.close(); // Close the scanner when the program ends
    }
}