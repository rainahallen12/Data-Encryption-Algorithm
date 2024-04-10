//Authors: Rainah Allen, Brianna Simon
//Date: Sunday March 17, 2024
//Title: ProjectOne.java

package Project1AllenSimon;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
    
    public class ProjectOne {
        public static void runTests() {
            
            //different tests with different inputKeys
            String output = "";
    
            output = Crypt.encryptBlock("1111111111111111111111111111111111111111111111111111111111111111",
                    "11111111111111111111111111111111111111111111111111111111");
            System.out.println("Output for: encryption(all ones, all ones)");
            System.out.println(output);
    
            output = Crypt.encryptBlock("0000000000000000000000000000000000000000000000000000000000000000",
                    "11111111111111111111111111111111111111111111111111111111");
            System.out.println("\nOutput for: encryption(all zeros, all ones)");
            System.out.println(output);
    
            output = Crypt.encryptBlock("0000000000000000000000000000000000000000000000000000000000000000",
                    "00000000000000000000000000000000000000000000000000000000");
            System.out.println("\nOutput for: encryption(all zeros, all zeros)");
            System.out.println(output);
    
            output = Crypt.encryptBlock("1100110010000000000001110101111100010001100101111010001001001100",
                    "00000000000000000000000000000000000000000000000000000000");
            System.out.println("\nOutput for: encryption(11001100100000000000011101"
                    + "01111100010001100101111010001001001100, all zeros)");
            System.out.println(output);
    
            output = Crypt.decryptBlock("1111111111111111111111111111111111111111111111111111111111111111",
                    "11111111111111111111111111111111111111111111111111111111");
            System.out.println("\nOutput for: decryption(all ones, all ones)");
            System.out.println(output);
    
            output = Crypt.decryptBlock("0000000000000000000000000000000000000000000000000000000000000000",
                    "11111111111111111111111111111111111111111111111111111111");
            System.out.println("\nOutput for: decryption(all zeros, all ones)");
            System.out.println(output);
    
            output = Crypt.decryptBlock("0000000000000000000000000000000000000000000000000000000000000000",
                    "00000000000000000000000000000000000000000000000000000000");
            System.out.println("\nOutput for: decryption(all zeros, all zeros)");
            System.out.println(output);
    
            output = Crypt.decryptBlock("0101011010001110111001000111100001001110010001100110000011110101",
                    "11111111111111111111111111111111111111111111111111111111");
            System.out.println("\nOutput for: decryption(0101011010001110111001000111100"
                    + "001001110010001100110000011110101, all ones)");
            System.out.println(output);
    
            output = Crypt.decryptBlock("0011000101110111011100100101001001001101011010100110011111010111",
                    "00000000000000000000000000000000000000000000000000000000");
            System.out.println("\nOutput for: decryption(0101011010001110111001000111100"
                    + "001001110010001100110000011110101, all zeros)");
            System.out.println(output);
            System.out.println();
    
        }
    
        public static void main(String[] args) {
            Scanner input = new Scanner(System.in);
            runTests(); 
            
            //user selects options (e or d, enters file names, secret key)
            System.out.println("Would you like to encrypt or decrypt (E/D): ");
            String action = input.nextLine().trim().toUpperCase(); //save for later
            
            System.out.println("Enter the Filename: ");
            String filename = input.nextLine().trim();
            
            System.out.println("Enter the Output Filename: ");
            String outputFilename = input.nextLine().trim();
            
            System.out.println("Enter the Secret Key: ");
            String secretKey = input.nextLine().trim();
            
            input.close();

            //convert characters to binary
            StringBuilder fileInfo = new StringBuilder();
            
            try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
                String line;
                while ((line = br.readLine()) != null) {
                    for (char character : line.toCharArray()) {
                        fileInfo.append(Crypt.DecToBinary(character));
                    }
                    fileInfo.append(Crypt.DecToBinary('\n')); 
                }
                if (fileInfo.length() > 8) { 
                    fileInfo.setLength(fileInfo.length() - 8);
                }
            } catch (IOException e) { //error in reading file, print error and return
                System.err.println("Error: " + e.getMessage());
                return;
            }
    
            String result;
            if ("E".equals(action)) { //if user inputted Encrypt
                result = Crypt.encryption(fileInfo.toString(), secretKey);
            } else if ("D".equals(action)) { // If user inputted Decrypt
                result = Crypt.decryption(fileInfo.toString(), secretKey);
            } else { // If user didn't put E or D
                System.out.println("Invalid option.");
                return;
            }
    
            // writing the output to output file that the user inputted
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilename))) {
                bw.write(result);
            } catch (IOException e) { //if theres an error writing to output file
                System.err.println("Error: " + e.getMessage());
            }
        }
    }