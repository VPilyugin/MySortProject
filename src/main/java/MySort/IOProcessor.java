package MySort;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

public class IOProcessor {
    private IOProcessor(){

    }
    public static void checkInputParams(Session session){
        try {
            checkZeroArgs(session);
            checkSortOrder(session);
            checkInputDataTypeDeclared(session);
            checkAndCreateOutputFile(session);
            checkNumberOfInputFiles(session);
            checkAndPrepareInputFiles(session);
            checkInputFileIsExist(session);
        } catch (MyException e) {
            System.out.println(e.getMessage()+" "+e);
            System.exit(1);
        }
    }
    private static void checkInputFileIsExist(Session session) throws MyException {
        int numberOfInputFiles = session.getInputFiles().length;
        session.setNumberOfInputFiles(numberOfInputFiles);
        for (int i = 0; i < numberOfInputFiles; i++) {
            if(!Files.exists(Paths.get(session.getInputFiles()[i]))){
                System.out.println("Input file "+(i)+" : "+(session.getInputFiles()[i])+" NOT EXISTS");
                session.setStatus(Errors.FILE_NOT_FOUND);
                throw new MyException(Errors.FILE_NOT_FOUND);
            }
            System.out.println("Input file "+(i)+" : "+(session.getInputFiles()[i])+" EXISTS");
        }
    }
    private static void checkAndPrepareInputFiles(Session session) throws MyException {
        String[] input = new String[session.getInputArgs().length- session.getParamnum()];
        session.setInputFiles(input);
        for (int i = session.getParamnum();i<session.getInputArgs().length;i++){
            input[(i- session.getParamnum())] = session.getCurrentRoot()+"\\"+session.getInputArgs()[i];
            if(!session.getInputArgs()[i].endsWith(".txt")){
                session.setStatus(Errors.INVALID_INPUT_FILE);
                throw new MyException (Errors.INVALID_INPUT_FILE);
            }
        }
    }
    private static void checkNumberOfInputFiles(Session session) throws MyException {
        if((session.getInputArgs().length- session.getParamnum())<2){
            session.setStatus(Errors.NOT_ENOUGHT_NUMBER_OF_INPUT_FILES);
            throw new MyException(Errors.NOT_ENOUGHT_NUMBER_OF_INPUT_FILES);
        }
    }
    private static void checkAndCreateOutputFile(Session session) throws MyException {
        if(!session.getInputArgs()[session.getParamnum()].endsWith(".txt")){
            session.setStatus(Errors.INVALID_OUTPUTFILE);
            throw new MyException(Errors.INVALID_OUTPUTFILE);
        }else {
            String output = session.getCurrentRoot()+"\\"+ session.getInputArgs()[session.getParamnum()];
            System.out.println("Output file: "+output);
            session.setOutputFile(output);
            try{
                Path of = Paths.get(output);
                if(!Files.exists(of)){
                    System.out.println("Try to create...");
                    Files.createFile(of);
                    System.out.println("Created successfully");
                }else{
                    System.out.println("Already exists... Will be overwrited");
                }
            }catch (IOException ex){
                session.setStatus(Errors.CANT_CREATE_OUTPUTFILE);
                throw new MyException(Errors.CANT_CREATE_OUTPUTFILE);
            }
            increaseParamNum(session);
        }
    }
    private static void checkInputDataTypeDeclared(Session session) throws MyException {
        if(session.getInputArgs()[session.getParamnum()].equals("-i")){
            session.setDataType(DataType.INTEGER);
            System.out.println("Data type set -i "+session.getDataType());
            increaseParamNum(session);
        } else if (session.getInputArgs()[session.getParamnum()].equals("-s")) {
            session.setDataType(DataType.STRING);
            System.out.println("Data type set -s "+session.getDataType());
            increaseParamNum(session);
        } else {
            System.out.println("Data type "+session.getInputArgs()[session.getParamnum()]);
            session.setStatus(Errors.INVALID_DATA_TYPE);
            throw new MyException(Errors.INVALID_DATA_TYPE);
        }
    }
    private static void checkSortOrder(Session session) {
        if(session.getInputArgs()[0].equals("-a")){
            System.out.println("Sort order set in -a "+session.getSortMode());
            increaseParamNum(session);
        }else if(session.getInputArgs()[0].equals("-d")){
            session.setSortMode(SortMode.DESC);
            System.out.println("Sort order set in -d "+session.getSortMode());
            increaseParamNum(session);
        }else{
            System.out.println("Sort order mode: "+session.getSortMode());
            if(!session.getInputArgs()[0].equals("-s")&&!session.getInputArgs()[0].equals("-i")){
                increaseParamNum(session);
            }
        }
    }
    private static void checkZeroArgs(Session session) throws MyException {
        System.out.println("Start parsing command line args...");
        if(session.getInputArgs().length<4){
            session.setStatus(Errors.NOT_ENOUGHT_ARGUMENTS);
            throw new MyException(Errors.NOT_ENOUGHT_ARGUMENTS);
        }
    }
    private static void increaseParamNum(Session session){
        int currentParamNum = session.getParamnum();
        currentParamNum++;
        session.setParamnum(currentParamNum);
    }
    public static void getConnections(Session session) {
        for (int i = 0; i < session.getNumberOfInputFiles(); i++) {
            try{
                BufferedReader bufferedReader = new BufferedReader(new FileReader(session.getInputFiles()[i]));
                session.getInputStreamHashMap().put(i,bufferedReader);
            }catch (IOException ioException){
                System.out.println("input file" + session.getInputFiles()[i] + " broken");
            }
            System.out.println("File number "+i+" "+session.getInputFiles()[i]+" was open to read");
        }
        if(session.getInputStreamHashMap().size()<2){
            System.out.println("Less than two valid input files");
            closeOpenFiles(session);
            System.exit(0);
        }
    }
    public static void openOutputFile(Session session) {
        try{
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(session.getOutputFile()));
            session.setOutputStream(bufferedWriter);
        }catch (IOException ioException){
            System.out.println("not possible to open outputfile");
            closeOpenFiles(session);
            System.exit(1);
        }
        System.out.println("OutputFile "+session.getOutputFile()+" was open to write");
    }
    public static void mergeFiles(Session session) {
        System.out.println("Start merging...");
        if(session.getDataType().equals(DataType.INTEGER)){
            IntService.merge(session);
        }else {
            StringService.merge(session);
        }
        System.out.println("Merging was finished");
    }
    public static void closeOpenFiles(Session session) {
        if(session.getOutputStream()!=null){
            try {
                session.getOutputStream().close();
                System.out.println("Output file close");
            } catch (IOException e) {
                System.out.println("Output file was not closed");
            }
        }
        if(session.getInputStreamHashMap().size()>0){
            for(Map.Entry<Integer, BufferedReader> entry:session.getInputStreamHashMap().entrySet()){
                try {
                    entry.getValue().close();
                    System.out.println("Input file number "+entry.getKey()+" was closed");
                } catch (IOException e) {
                    System.out.println("Input file number "+entry.getKey()+" was not closed");
                }
            }
        }
    }
    public enum Errors{
        VALID("VALID"),
        NOT_ENOUGHT_ARGUMENTS("Not enought arguments"),
        INVALID_DATA_TYPE("you have to set param -s 'String' or -i 'Integer'"),
        INVALID_OUTPUTFILE("type of outputfile must be *.txt"),
        CANT_CREATE_OUTPUTFILE ("not possible to create output file"),
        NOT_ENOUGHT_NUMBER_OF_INPUT_FILES("there are must be at least 2 input files"),
        INVALID_INPUT_FILE("type of input files must be *.txt"),
        FILE_NOT_FOUND("can`t find input file"),
        CANT_OPEN_INPUTFILE("impossible to open input file");
        private String description;
        private Errors(String description){
            this.description = description;
        }
        public String getDescription() {
            return description;
        }
    }
    public enum SortMode{
        ASC, DESC
    }
    public enum DataType{
        INTEGER, STRING
    }
}
