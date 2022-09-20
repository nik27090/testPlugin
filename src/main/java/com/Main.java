package com;

import com.benchmark.RunGenerator;
import com.benchmark.RunTool;
import com.generator.Generator;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        switch (args[0]) {
            case "benchmark": {
                runBenchmark();
                break;
            }
            case "terminal": {
                runTerminal(args);
                break;
            }
            case "help": {
                printHelpMessage();
            }
        }
    }

    private static void printHelpMessage() {
        System.out.println("This is a fuzzing test generator.");
        System.out.println("options: benchmark, terminal, help");
    }

    private static void runBenchmark() throws IOException, ClassNotFoundException {
        Writer writer = new PrintWriter(System.out);
        Reader reader = new InputStreamReader(System.in);
        RunGenerator tool = new RunGenerator();
        RunTool runTool = new RunTool(tool, reader, writer);
        runTool.run();
    }

    private static void runTerminal(String[] args) throws IOException, ClassNotFoundException {
        if (args.length == 3) {
            throw new IllegalArgumentException("Invalid argument amount: " + args.length);
        }
        String inputPath = args[1];
        String outputPath = args[2];

        Path absolutePathInput = Paths.get(inputPath).toAbsolutePath();
        Path absolutePathOutput = Paths.get(outputPath).toAbsolutePath();

        Generator.setBinPath(absolutePathInput.toString());

        Generator.generateTests(absolutePathInput.toString(), absolutePathOutput.toString());
    }
}
