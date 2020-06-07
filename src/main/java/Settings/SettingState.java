package Settings;


public class SettingState {
    public String inputPath = "/target/classes";
    public String outputPath = "/src/test/TestPlugin";
    public String numberOfTests = "5";

    public void setNumberOfTests(String numberOfTests) {
        this.numberOfTests = numberOfTests;
    }

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }
}
