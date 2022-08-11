package MySort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;

public class Session {
    private Integer key;
    private Integer intValue;
    private String stringValue;
    private HashMap<Integer, BufferedReader> inputStreamHashMap = new HashMap<>();
    private BufferedWriter outputStream;
    private int[] workingLineInts;
    private String[] workingLineString;
    private String[] inputArgs;
    private String[] inputFiles;
    private IOProcessor.SortMode sortMode = IOProcessor.SortMode.ASC;
    private IOProcessor.DataType dataType = IOProcessor.DataType.INTEGER;
    private String currentRoot = System.getProperty("user.dir");
    private String outputFile = "";
    private int numberOfInputFiles = 0;
    private int paramnum = 0;
    private IOProcessor.Errors status = IOProcessor.Errors.VALID;
    public Session(String[] args){
        inputArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            inputArgs[i] = args[i];
        }
    }

    public String[] getWorkingLineString() {
        return workingLineString;
    }

    public void setWorkingLineString(String[] workingLineString) {
        this.workingLineString = workingLineString;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public int[] getWorkingLineInts() {
        return workingLineInts;
    }

    public void setWorkingLineInts(int[] workingLineInts) {
        this.workingLineInts = workingLineInts;
    }

    public BufferedWriter getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(BufferedWriter outputStream) {
        this.outputStream = outputStream;
    }

    public HashMap<Integer, BufferedReader> getInputStreamHashMap() {
        return inputStreamHashMap;
    }

    public void setInputStreamHashMap(HashMap<Integer, BufferedReader> inputStreamHashMap) {
        this.inputStreamHashMap = inputStreamHashMap;
    }

    public String[] getInputFiles() {
        return inputFiles;
    }

    public int getNumberOfInputFiles() {
        return numberOfInputFiles;
    }

    public void setNumberOfInputFiles(int numberOfInputFiles) {
        this.numberOfInputFiles = numberOfInputFiles;
    }

    public void setInputFiles(String[] inputFiles) {
        this.inputFiles = inputFiles;
    }

    public IOProcessor.SortMode getSortMode() {
        return sortMode;
    }

    public void setSortMode(IOProcessor.SortMode sortMode) {
        this.sortMode = sortMode;
    }

    public IOProcessor.DataType getDataType() {
        return dataType;
    }

    public void setDataType(IOProcessor.DataType dataType) {
        this.dataType = dataType;
    }

    public int getParamnum() {
        return paramnum;
    }

    public void setParamnum(int paramnum) {
        this.paramnum = paramnum;
    }

    public String[] getInputArgs() {
        return inputArgs;
    }

    public IOProcessor.Errors getStatus() {
        return status;
    }

    public void setStatus(IOProcessor.Errors status) {
        this.status = status;
    }

    public String getCurrentRoot() {
        return currentRoot;
    }

    public void setCurrentRoot(String currentRoot) {
        this.currentRoot = currentRoot;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
