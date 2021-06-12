package ru.spiritofsea.sha256;

public class shaMain {
    public static void main(String[] args) {
            sha256 Example = new sha256();
            Example.genSHA("hello world");
            System.out.println(Example.getHash());
    }
}