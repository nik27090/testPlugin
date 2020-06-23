package com;

import generator.Generator;
import generator.RunGenerator;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Writer writer = new PrintWriter(System.out);
        Reader reader = new InputStreamReader(System.in);
        RunGenerator tool = new RunGenerator();
        RunTool runTool = new RunTool(tool, reader, writer);
        runTool.run();
    }

    public static void mains(String[] args) throws IOException, ClassNotFoundException {
        String inputPath = args[0];
        String outputPath = args[1];

        Path inpath = Paths.get(inputPath);
        Path outpath = Paths.get(outputPath);

        Path absolutePathInput = inpath.toAbsolutePath();
        Path absolutePathOutput = outpath.toAbsolutePath();

        Generator.setBinPath(absolutePathInput.toAbsolutePath().toString());

        Generator.generateTests(absolutePathInput.toString(), absolutePathOutput.toString());

    }
}
