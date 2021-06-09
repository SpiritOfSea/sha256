package ru.spiritofsea.sha256;

import java.nio.charset.StandardCharsets;

public class sha256 {

    private String byteContent = "";
    private int dataLength = 0;
    private int counter = 0;
    private String h0, h1, h2, h3, h4, h5, h6, h7;
    private String[] consts;
    private String[] wordQuery = new String[64];


    private void initializeHash() {
        this.h0 = "0x6a09e667";
        this.h1 = "0xbb67ae85";
        this.h2 = "0x3c6ef372";
        this.h3 = "0xa54ff53a";
        this.h4 = "0x510e527f";
        this.h5 = "0x9b05688c";
        this.h6 = "0x1f83d9ab";
        this.h7 = "0x5be0cd19";

        this.consts = new String[]{"0x428a2f98", "0x71374491", "0xb5c0fbcf", "0xe9b5dba5", "0x3956c25b", "0x59f111f1", "0x923f82a4", "0xab1c5ed5", "0xd807aa98", "0x12835b01", "0x243185be", "0x550c7dc3", "0x72be5d74", "0x80deb1fe", "0x9bdc06a7", "0xc19bf174", "0xe49b69c1", "0xefbe4786", "0x0fc19dc6", "0x240ca1cc", "0x2de92c6f", "0x4a7484aa", "0x5cb0a9dc", "0x76f988da", "0x983e5152", "0xa831c66d", "0xb00327c8", "0xbf597fc7", "0xc6e00bf3", "0xd5a79147", "0x06ca6351", "0x14292967", "0x27b70a85", "0x2e1b2138", "0x4d2c6dfc", "0x53380d13", "0x650a7354", "0x766a0abb", "0x81c2c92e", "0x92722c85", "0xa2bfe8a1", "0xa81a664b", "0xc24b8b70", "0xc76c51a3", "0xd192e819", "0xd6990624", "0xf40e3585", "0x106aa070", "0x19a4c116", "0x1e376c08", "0x2748774c", "0x34b0bcb5", "0x391c0cb3", "0x4ed8aa4a", "0x5b9cca4f", "0x682e6ff3", "0x748f82ee", "0x78a5636f", "0x84c87814", "0x8cc70208", "0x90befffa", "0xa4506ceb", "0xbef9a3f7", "0xc67178f2"};
    }

    private String asciiToByte(String input) {

        byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
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
        this.byteContent = asciiToByte(input);
        this.dataLength = this.byteContent.length();
        this.byteContent = setMarker(this.byteContent);
        this.byteContent = fillZeroes(this.byteContent, true, 448);
        this.byteContent = this.byteContent + fillZeroes(Integer.toBinaryString(this.dataLength), false, 64);
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
                outputString.append("\n"+input.charAt(count));
                count++;
            } else {
                outputString.append(((count % 8) == 0 & count > 0) ? (" " + input.charAt(count)) : input.charAt(count));  // divide into 8 char blocks
                count++;
            }
        }
        System.out.println(outputString.toString());
    }

    private void formWordQuery() {
        StringBuilder time = new StringBuilder();
        int count = 0;

        for (int i=0; i<16; i++) {
            time.delete(0, time.length());
            for (int a=0; a<32; a++) {
                time.append(this.byteContent.charAt(a+32*i));
            }
            this.wordQuery[i] = time.toString();
        }

        for (int i=16; i<64; i++) {
            this.wordQuery[i] = "00000000000000000000000000000000";
        }

    }

    private String rightRotate(String input, int shift) {
        StringBuilder time = new StringBuilder();

        for (int i=0; i<shift; i++) {
            time.insert(0, input.charAt(input.length()-i-1));
            System.out.println(time.toString());
        }
        for (int i=0; i<input.length()-shift; i++) {
            time.append(input.charAt(i));
        }

        return time.toString();
    }

    private String rightShift(String input, int shift) {
        StringBuilder time = new StringBuilder();
        for (int i = 0; i<shift; i++) {
            time.append("0");
        }
        for (int i=0; i<input.length()-shift; i++) {
            time.append(input.charAt(i));
        }
        return time.toString();
    }

    private String XOR(String input1, String input2) {
        int bin1 = Integer.parseInt(input1,2);
        int bin2 = Integer.parseInt(input2, 2);
        int res = bin1^bin2;
        return fillZeroes(Integer.toBinaryString(res), false, input1.length());
    }

    private String AND(String input1, String input2) {
        int bin1 = Integer.parseInt(input1, 2);
        int bin2 = Integer.parseInt(input2,2);
        int res = bin1&bin2;
        return fillZeroes(Integer.toBinaryString(res),false, input1.length());
    }

    public void genSHA() {
        initializeHash();
        formBlock("Hello world");
        formWordQuery();
        System.out.println(AND("11110000", "00111100"));
        System.out.println(rightShift("01101111001000000111011101101111", 3));
    }

}
