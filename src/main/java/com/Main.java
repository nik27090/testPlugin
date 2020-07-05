package com;

import generator.RunGenerator;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Writer writer = new PrintWriter(System.out);
        Reader reader = new InputStreamReader(System.in);
        RunGenerator tool = new RunGenerator();
        RunTool runTool = new RunTool(tool, reader, writer);
        runTool.run();
    }

    //Main class for testing the contest configuration independently of docker

//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        String inputPath = args[0];
//        String outputPath = args[1];
//
//        Path inpath = Paths.get(inputPath);
//        Path outpath = Paths.get(outputPath);
//
//        Path absolutePathInput = inpath.toAbsolutePath();
//        Path absolutePathOutput = outpath.toAbsolutePath();
//
//        Generator.setBinPath(absolutePathInput.toAbsolutePath().toString());
//
//        Generator.generateTests(absolutePathInput.toString(), absolutePathOutput.toString());
//
//    }
}
