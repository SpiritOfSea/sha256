package ru.spiritofsea.sha256;

import java.nio.charset.StandardCharsets;

public class sha256 {

    private String byteContent = "";
    private int dataLength = 0;
    private int counter = 0;

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
        this.byteContent = fillZeroes(this.byteContent, true, 512-dataLength-8-64);
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

    public void genSHA() {

        formBlock("Hello world");
        prettyBytePrint(this.byteContent);
    }

}
