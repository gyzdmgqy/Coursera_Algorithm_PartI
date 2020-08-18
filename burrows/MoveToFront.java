/* *****************************************************************************
 *  Name:gyzdmgqy
 *  Date:8/17/2020
 *  Description:Move-to-front encoding and decoding. The main idea of move-to-front encoding is to maintain an ordered sequence of the characters in the alphabet by repeatedly reading a character from the input message; printing the position in the sequence in which that character appears; and moving that character to the front of the sequence. As a simple example, if the initial ordering over a 6-character alphabet is A B C D E F, and we want to encode the input CAAABCCCACCF, then we would update the move-to-front sequence as follows.
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] characters = new char[R];
        for (char i = 0; i < R; ++i) characters[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char inputChar = BinaryStdIn.readChar();
            for (char i = 0; i < R; ++i) {
                if (characters[i] == inputChar) {
                    BinaryStdOut.write(i);
                    moveFront(characters, i);
                    break;
                }
            }
        }
        BinaryStdOut.close();
    }

    private static void moveFront(char[] characters, int index) {
        char key = characters[index];
        for (int i = index; i > 0; --i) {
            characters[i] = characters[i - 1];
        }
        characters[0] = key;
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] characters = new char[R];
        for (char i = 0; i < R; ++i) characters[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            int inputCode = BinaryStdIn.readInt();
            char outputChar = characters[inputCode];
            BinaryStdOut.write(outputChar);
            moveFront(characters, inputCode);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding

    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();

    }
}
