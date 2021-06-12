package ru.spiritofsea.sha256;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class sha256 {

    private String hash, byteContent, inputLength;
    private String h0, h1, h2, h3, h4, h5, h6, h7;
    private String[] inputBlocks = new String[512];
    private String[] consts;
    private final String[] wordList = new String[64];
    private int blockCounter = 0;

    public void genSHA(String input) {
        initializeHash();
        this.inputLength = Long.toBinaryString(input.length() * 8);
        formBlock(input);

        for (int i = 0; i < this.blockCounter; i++) {
            this.byteContent = this.inputBlocks[i];
            createWordList();
            fillWordList();
            compress();
        }

    }

    private void initializeHash() {
        this.h0 = hexTo32Bin("6a09e667");
        this.h1 = hexTo32Bin("bb67ae85");
        this.h2 = hexTo32Bin("3c6ef372");
        this.h3 = hexTo32Bin("a54ff53a");
        this.h4 = hexTo32Bin("510e527f");
        this.h5 = hexTo32Bin("9b05688c");
        this.h6 = hexTo32Bin("1f83d9ab");
        this.h7 = hexTo32Bin("5be0cd19");
        this.consts = new String[]{"428a2f98", "71374491", "b5c0fbcf", "e9b5dba5", "3956c25b", "59f111f1", "923f82a4", "ab1c5ed5", "d807aa98", "12835b01", "243185be", "550c7dc3", "72be5d74", "80deb1fe", "9bdc06a7", "c19bf174", "e49b69c1", "efbe4786", "0fc19dc6", "240ca1cc", "2de92c6f", "4a7484aa", "5cb0a9dc", "76f988da", "983e5152", "a831c66d", "b00327c8", "bf597fc7", "c6e00bf3", "d5a79147", "06ca6351", "14292967", "27b70a85", "2e1b2138", "4d2c6dfc", "53380d13", "650a7354", "766a0abb", "81c2c92e", "92722c85", "a2bfe8a1", "a81a664b", "c24b8b70", "c76c51a3", "d192e819", "d6990624", "f40e3585", "106aa070", "19a4c116", "1e376c08", "2748774c", "34b0bcb5", "391c0cb3", "4ed8aa4a", "5b9cca4f", "682e6ff3", "748f82ee", "78a5636f", "84c87814", "8cc70208", "90befffa", "a4506ceb", "bef9a3f7", "c67178f2"};

        for (int i = 0; i < consts.length; i++) {
            this.consts[i] = hexTo32Bin(this.consts[i]);
        }
    }

    private void formBlock(String input) {
        this.byteContent = asciiToByte(input);
        int shift;
        StringBuilder time = new StringBuilder(this.byteContent);

        time.append("1");
        shift = time.length() % 512;
        if (shift < 448) {
            time = new StringBuilder(fillZeroes(time.toString(), true, time.length() + (448 - shift)));
            time.append(fillZeroes(this.inputLength, false, 64));
        } else {
            time = new StringBuilder(fillZeroes(time.toString(), true, time.length() + 512 - shift + 448));
        }

        time.append(fillZeroes(this.inputLength, false, 64));

        while (time.length() >= 512) {
            this.inputBlocks[this.blockCounter] = time.substring(0, 512);
            time.replace(0, 512, "");
            this.blockCounter++;
        }

    }

    private void createWordList() {
        StringBuilder time = new StringBuilder();

        for (int i = 0; i < 16; i++) {
            time.delete(0, time.length());
            for (int a = 0; a < 32; a++) {
                time.append(this.byteContent.charAt(a + 32 * i));
            }
            this.wordList[i] = time.toString();
        }

        for (int i = 16; i < 64; i++) {
            this.wordList[i] = "00000000000000000000000000000000";
        }

    }

    public void fillWordList() {
        String s0;
        String s1;

        for (int i = 16; i < 64; i++) {
            s0 = XOR(rightShift(this.wordList[i - 15], 3), XOR(rightRotate(this.wordList[i - 15], 18), rightRotate(this.wordList[i - 15], 7)));
            s1 = XOR(rightShift(this.wordList[i - 2], 10), XOR(rightRotate(this.wordList[i - 2], 19), rightRotate(this.wordList[i - 2], 17)));
            this.wordList[i] = ADD(ADD(this.wordList[i - 16], s0), ADD(this.wordList[i - 7], s1));
        }
    }

    private void compress() {
        String a = this.h0;
        String b = this.h1;
        String c = this.h2;
        String d = this.h3;
        String e = this.h4;
        String f = this.h5;
        String g = this.h6;
        String h = this.h7;

        String S1, ch, temp1, S0, maj, temp2;


        for (int i = 0; i < 64; i++) {
            S1 = XOR(XOR(rightRotate(e, 6), rightRotate(e, 11)), rightRotate(e, 25));
            ch = XOR(AND(e, f), AND(NOT(e), g));
            temp1 = ADD(ADD(ADD(ADD(h, S1), ch), this.consts[i]), this.wordList[i]);
            S0 = XOR(XOR(rightRotate(a, 2), rightRotate(a, 13)), rightRotate(a, 22));
            maj = XOR(XOR(AND(a, b), AND(a, c)), AND(b, c));
            temp2 = ADD(S0, maj);
            h = g;
            g = f;
            f = e;
            e = ADD(d, temp1);
            d = c;
            c = b;
            b = a;
            a = ADD(temp1, temp2);

        }

        h0 = ADD(h0, a);
        h1 = ADD(h1, b);
        h2 = ADD(h2, c);
        h3 = ADD(h3, d);
        h4 = ADD(h4, e);
        h5 = ADD(h5, f);
        h6 = ADD(h6, g);
        h7 = ADD(h7, h);

        this.hash = binToHex(h0 + h1 + h2 + h3 + h4 + h5 + h7);

    }

    public String getHash() {
        return this.hash;
    }

    private String fillTo32(String input) {         // Gets String, returns String which is filled with 0's to 32 sym.
        if (input.length() < 32) {
            input = fillZeroes(input, false, 32);
        } else if (input.length() > 32) {
            input = input.substring(input.length() - 32);
        }
        return input;
    }

    private String fillZeroes(String input, boolean mode, int len) {
        StringBuilder rawBinary = new StringBuilder(input);

        if (mode) {
            while (rawBinary.length() < len) {
                rawBinary.append("0");
            }
        } else {
            while (rawBinary.length() < len) {
                rawBinary.insert(0, "0");
            }
        }

        return rawBinary.toString();
    }

    public void prettyBytePrint(String input) {
        int count = 0;
        StringBuilder outputString = new StringBuilder();

        while (count < input.length()) {
            if (count % 64 == 0 & count > 0) {
                outputString.append("\n").append(input.charAt(count));
            } else {
                outputString.append(((count % 8) == 0 & count > 0) ? (" " + input.charAt(count)) : input.charAt(count));  // divide into 8 char blocks
            }
            count++;
        }

        System.out.println("\n" + outputString + "\n");
    }

    // Math block below.

    private String rightRotate(String input, int shift) {
        StringBuilder time = new StringBuilder();

        for (int i = 0; i < shift; i++) {
            time.insert(0, input.charAt(input.length() - i - 1));
        }
        for (int i = 0; i < input.length() - shift; i++) {
            time.append(input.charAt(i));
        }

        return time.toString();
    }

    private String rightShift(String input, int shift) {
        StringBuilder time = new StringBuilder();
        time.append("0".repeat(Math.max(0, shift)));
        for (int i = 0; i < input.length() - shift; i++) {
            time.append(input.charAt(i));
        }
        return time.toString();
    }

    private String XOR(String input1, String input2) {
        StringBuilder result = new StringBuilder();

        for (int i = input1.length() - 1; i >= 0; i--) {
            if (input1.charAt(i) == '0' & input2.charAt(i) == '0') {
                result.insert(0, '0');
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '0' || input1.charAt(i) == '0' & input2.charAt(i) == '1') {
                result.insert(0, '1');
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '1') {
                result.insert(0, '0');
            }
        }
        return result.toString();
    }

    private String AND(String input1, String input2) {
        StringBuilder result = new StringBuilder();
        for (int i = input1.length() - 1; i >= 0; i--) {
            if (input1.charAt(i) == '0' & input2.charAt(i) == '0') {
                result.insert(0, '0');
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '0' || input1.charAt(i) == '0' & input2.charAt(i) == '1') {
                result.insert(0, '0');
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '1') {
                result.insert(0, '1');
            }
        }
        return result.toString();
    }

    private String NOT(String input1) {
        StringBuilder result = new StringBuilder();

        for (int i = input1.length() - 1; i >= 0; i--) {
            if (input1.charAt(i) == '0') {
                result.insert(0, '1');
            } else {
                result.insert(0, '0');
            }
        }

        return result.toString();
    }

    private String ADD(String input1, String input2) {
        StringBuilder result = new StringBuilder();
        boolean pow = false;

        for (int i = input1.length() - 1; i >= 0; i--) {
            if (input1.charAt(i) == '0' & input2.charAt(i) == '0') {
                if (pow) {
                    result.insert(0, '1');
                    pow = false;
                } else {
                    result.insert(0, '0');
                    pow = false;
                }
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '0' || input1.charAt(i) == '0' & input2.charAt(i) == '1') {
                if (pow) {
                    result.insert(0, '0');
                    pow = true;
                } else {
                    result.insert(0, '1');
                    pow = false;
                }
            } else if (input1.charAt(i) == '1' & input2.charAt(i) == '1') {
                if (pow) {
                    result.insert(0, '1');
                } else {
                    result.insert(0, '0');
                }
                pow = true;
            }
        }

        return result.toString();
    }

    private String asciiToByte(String input) {

        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }

        return binary.toString();
    }

    private String hexTo32Bin(String input) {
        return fillTo32(Long.toBinaryString(Long.parseLong(input.replaceAll("\\s*", ""), 16)));
    }

    private String binToHex(String input) {
        StringBuilder temp = new StringBuilder(input);
        StringBuilder res = new StringBuilder();
        while (temp.length() > 4) {
            res.insert(0, Integer.toHexString(Integer.parseInt(temp.substring(temp.length()-4), 2)));
            temp.delete(temp.length()-4, temp.length());
        }
        res.insert(0, Integer.toHexString(Integer.parseInt(temp.toString(), 2)));

        return res.toString().toUpperCase();
    }

}
