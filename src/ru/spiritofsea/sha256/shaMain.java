package ru.spiritofsea.sha256;

import org.apache.commons.cli.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class shaMain {
    public static void main(String[] args) {
        run(args);
    }

    private static void run(String[] args) {

        sha256 Example = new sha256();

        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("v", "verbose", false, "verbose output");
        options.addOption("i", "input", true, "hash file");
        options.addOption("s", "string", true, "hash string");
        options.addOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "display help message");
        HelpFormatter formatter = new HelpFormatter();

        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption("h")) {
                formatter.printHelp("sha256", options);
                return;
            }

            if (line.hasOption("v")) {
                Example.setVerbose(true);
            }

            if (line.hasOption("i")) {
                try {
                    Path file = Paths.get(line.getOptionValue("i"));
                    byte[] byteContent = Files.readAllBytes(file);
                    Example.genSHA(byteContent);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (line.hasOption("o")) {
                    try {
                        Path file = Paths.get(line.getOptionValue("o"));
                        Files.write(file, Collections.singleton(Example.getHash()), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Example.getHash());
                }
            }

            if (line.hasOption("s")) {
                Example.genSHA(line.getOptionValue("s"));

                if (line.hasOption("o")) {
                    try {
                        Path file = Paths.get(line.getOptionValue("o"));
                        Files.write(file, Collections.singleton(Example.getHash()), StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println(Example.getHash());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}