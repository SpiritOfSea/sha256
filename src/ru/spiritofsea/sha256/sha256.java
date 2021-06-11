package ru.spiritofsea.sha256;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class sha256 {

    private String inputInformation;
    private String byteContent = "";
    private int dataLength = 0;
    private String h0, h1, h2, h3, h4, h5, h6, h7;
    private String[] consts;
    private String[] wordList = new String[64];
    private String hash = "";

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

    private String hexTo32Bin(String input) {
        return fillToSize(Long.toBinaryString(Long.parseLong(input.replaceAll("\\s*", ""), 16)), 32);
    }

    private String binToHex(String input) {
        BigInteger temp = new BigInteger(input, 2);
        return temp.toString(16).toUpperCase();
    }

    private String fillToSize(String input, int len) {
        if (input.length() < len) {
            input = fillZeroes(input, false, len);
        } else if (input.length() > len) {
            input = input.substring(input.length() - len);
        }
        return input;
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

    private String setMarker(String input) {
        return input + "10000000";
    }

    private void formBlock(String input) {
        this.inputInformation = input;
        this.byteContent = asciiToByte(input);
        this.dataLength = this.byteContent.length();
        this.byteContent = setMarker(this.byteContent);
        this.byteContent = fillZeroes(this.byteContent, true, 448);
        this.byteContent = this.byteContent + fillZeroes(Long.toBinaryString(this.dataLength), false, 64);
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

    private void prettyBytePrint(String input) {
        int count = 0;
        StringBuilder outputString = new StringBuilder();

        while (count < input.length()) {
            if (count % 64 == 0 & count > 0) {
                outputString.append("\n" + input.charAt(count));
                count++;
            } else {
                outputString.append(((count % 8) == 0 & count > 0) ? (" " + input.charAt(count)) : input.charAt(count));  // divide into 8 char blocks
                count++;
            }
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

    public void fillWordList() {
        String s0;
        String s1;

        for (int i = 16; i < 64; i++) {
            s0 = XOR(rightShift(this.wordList[i - 15], 3), XOR(rightRotate(this.wordList[i - 15], 18), rightRotate(this.wordList[i - 15], 7)));
            s1 = XOR(rightShift(this.wordList[i - 2], 10), XOR(rightRotate(this.wordList[i - 2], 19), rightRotate(this.wordList[i - 2], 17)));
            this.wordList[i] = ADD(ADD(this.wordList[i - 16], s0), ADD(this.wordList[i - 7], s1));
        }
    }

    public void genSHA() {
        initializeHash();
        formBlock("hello world");
        createWordList();
        fillWordList();
        compress();

        System.out.println("\n\nSOURCE TEXT: " + this.inputInformation + "\nFINAL SHA256-HASH: " + this.hash);
    }

}
