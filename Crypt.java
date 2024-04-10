//Authors: Rainah Allen, Brianna Simon
//Date: Sunday March 17, 2024

package Project1AllenSimon;
import java.lang.StringBuilder;

public class Crypt {
    
    // s-box and permutation P implemented
    private static final int[][] S = new int[16][16];
    private static final int[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25};

    //method for keyschedule transformation which creates the 10 subkeys
    public static String[] keyScheduleTransform(String key) {
        String X = key.substring(0, 28);
        String Y = key.substring(28);
        String[] subKey = new String[10];
        for (int i = 0; i < 10; i++) {
            X = shiftIt(X);
            Y = shiftIt(Y);
            subKey[i] = X + Y; 
        }
        return subKey;
    }

    //Runs the xorIt function to compare two strings of binary
    public static String xorIt(String binary1, String binary2) {
        StringBuilder xor = new StringBuilder();
        for (int i = 0; i < binary1.length(); i++) {
            xor.append(binary1.charAt(i) == binary2.charAt(i) ? '0' : '1');
        }
        return xor.toString();
    }

    //shifts the binary input one index to the left
    public static String shiftIt(String binaryInput) {
        return binaryInput.substring(1) + binaryInput.charAt(0);
    }

    //permuteIt method using the permutation P
    public static String permuteIt(String binaryInput) {
        StringBuilder permuted = new StringBuilder();
        for (int i : P) {
            permuted.append(binaryInput.charAt(i - 1));
        }
        return permuted.toString();
    }

     //functionF method
    public static String functionF(String rightHalf, String subKey) {
        String operation1 = xorIt(rightHalf, subKey); 
        StringBuilder operation2 = new StringBuilder(); // Concatenates after substitution
        for (int i = 0; i < 4; i++) {
            String sub = operation1.substring(8 * i, 8 * (i + 1));
            operation2.append(SubstitutionS(sub));
        }
        return permuteIt(operation2.toString());
    }
    
    //Susbstitutes the binary with the value given in sbox
    public static String SubstitutionS(String binaryInput) {
        int row = BinaryToDecimal(binaryInput.substring(0, 4));
        int collumn = BinaryToDecimal(binaryInput.substring(4));
        int sBoxValue = S [row][collumn]; 
        return Integer.toBinaryString(sBoxValue).format("%8s", Integer.toBinaryString(sBoxValue)).replace(' ', '0'); // Pad with zeros
    }

    //Method that changes binary to decimal value
    public static int BinaryToDecimal(String binary) {
        return Integer.parseInt(binary, 2);
    }

   

    //method to encrypt the block with the given input Key
    public static String encryptBlock(String block, String inputKey) {
        String[] left = new String[11];
        String[] right = new String[11];
        left[0] = block.substring(0, 32);
        right[0] = block.substring(32);

        String[] subKey = keyScheduleTransform(inputKey);

        for (int i = 1; i <= 10; i++) {
            left[i] = right[i - 1];
            right[i] = xorIt(left[i - 1], functionF(right[i - 1], subKey[i - 1]));
        }

        return left[10] + right[10];
    }

    //metbod for decryption of encrypted data
    public static String decryptBlock(String block, String inputKey) {
        String[] left = new String[11];
        String[] right = new String[11];
        left[10] = block.substring(0, 32);
        right[10] = block.substring(32);

        String[] subKey = keyScheduleTransform(inputKey);

        for (int i = 9; i >= 0; i--) {
            right[i] = left[i + 1];
            left[i] = xorIt(right[i + 1], functionF(left[i + 1], subKey[i]));
        }

        return left[0] + right[0];
    }

    // method to convert decimal to binary with padding to ensure 8 bits
    public static String DecToBinary(int num) {
        return String.format("%8s", Integer.toBinaryString(num)).replace(' ', '0');
    }

    //encryption algorithm which splits the input into 64 bit blocks and runs the algoriithm on them
    public static String encryption(String longBinaryInput, String inputKey) {
        StringBuilder encryptedCode = new StringBuilder();
        int length = longBinaryInput.length();
        int blocks = length / 64;
        for (int i = 0; i < blocks; i++) {
            String block = longBinaryInput.substring(64 * i, 64 * (i + 1));
            encryptedCode.append(encryptBlock(block, inputKey));
        }
        // adds 0s as padding if the binary input is less than 64 bits
        if (length % 64 != 0) {
            String lastBlock = longBinaryInput.substring(64 * blocks);
            lastBlock += "0".repeat(64 - lastBlock.length()); // Zero padding
            encryptedCode.append(encryptBlock(lastBlock, inputKey));
        }
        return encryptedCode.toString();
    }

    //method for decrypting the binary message
    public static String decryption(String longBinaryInput, String inputKey) {
        StringBuilder decryptedCode = new StringBuilder();
        for (int i = 0; i < longBinaryInput.length(); i += 64) {
            String block = longBinaryInput.substring(i, Math.min(i + 64, longBinaryInput.length()));
            decryptedCode.append(decryptBlock(block, inputKey));
        }
        return decryptedCode.toString();
    }

}