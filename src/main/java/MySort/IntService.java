package MySort;

import java.io.IOException;
public class IntService {
    private IntService(){

    }

    public static void merge(Session session) {
        if (session.getSortMode().equals(IOProcessor.SortMode.ASC)) {
            mergeIntsAsc(session);
        } else {
            mergeIntsDesc(session);
        }
    }
    private static void mergeIntsDesc(Session session) {
        fillInInitialLineInts(session,Integer.MIN_VALUE);
        setInitialMax(session);
        boolean finishMerge = checkFinishDesc(session);
        while (!finishMerge) {
            finishMerge = checkFinishDesc(session);
            if (finishMerge) continue;
            checkIterationDesc(session);
            writeIntoOutputFile(session);
            getNextValueDesc(session);
            if (session.getWorkingLineInts()[session.getKey()] == Integer.MIN_VALUE) {
                setInitialMax(session);
            }
        }
    }
    private static void getNextValueDesc(Session session) {
        int oldValue = session.getWorkingLineInts()[session.getKey()];
        session.getWorkingLineInts()[session.getKey()]=Integer.MIN_VALUE;
        String inputString = null;
        while (inputString == null) {
            try {
                if (session.getInputStreamHashMap().get(session.getKey()).ready()) {
                    try {
                        inputString = session.getInputStreamHashMap().get(session.getKey()).readLine();
                        try {
                            int newValue = Integer.parseInt(inputString);
                            if(newValue<=oldValue){
                                session.getWorkingLineInts()[session.getKey()]=newValue;
                                session.setIntValue(newValue);
                            }else {
                                System.out.println("Next line Integer "+newValue+
                                        " is bigger than previous "+oldValue+
                                        " and missed in file number "+session.getKey());
                                inputString = null;
                                continue;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Input line: " + inputString +
                                    " is not VALID and missed in file number "+session.getKey());
                            inputString = null;
                            continue;
                        }
                    } catch (IOException e) {
                        System.out.println("can`t read line in file number " + session.getKey());
                    }
                }
                if(session.getWorkingLineInts()[session.getKey()]==Integer.MIN_VALUE){
                    System.out.println("Input file number "+session.getKey()+" read completely");
                    inputString="";
                }
            } catch (IOException e) {
                System.out.println("file number " + session.getKey() + " not ready");
            }
        }
    }
    private static void checkIterationDesc(Session session) {
        int key = session.getKey();
        int value = session.getIntValue();
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]>value){
                key = i;
                value = session.getWorkingLineInts()[i];
                session.setKey(key);
                session.setIntValue(value);
            }
        }
    }
    private static boolean checkFinishDesc(Session session) {
        boolean flag = true;
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]>Integer.MIN_VALUE){
                flag = false;
            }
        }
        return flag;
    }
    private static void setInitialMax(Session session) {
        int key=Integer.MIN_VALUE;
        int value=Integer.MIN_VALUE;
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]>value){
                key = i;
                value = session.getWorkingLineInts()[i];
                session.setKey(key);
                session.setIntValue(value);
            }
        }
    }
    private static void mergeIntsAsc(Session session) {
        fillInInitialLineInts(session,Integer.MAX_VALUE);
        setInitialMin(session);
        boolean finishMerge = checkFinish(session);
        while (!finishMerge) {
            finishMerge = checkFinish(session);
            if (finishMerge) continue;
            checkIteration(session);
            writeIntoOutputFile(session);
            getNextValue(session);
            if (session.getWorkingLineInts()[session.getKey()] == Integer.MAX_VALUE) {
                setInitialMin(session);
            }
        }
    }
    private static void getNextValue(Session session) {
        int oldValue = session.getWorkingLineInts()[session.getKey()];
        session.getWorkingLineInts()[session.getKey()]=Integer.MAX_VALUE;
        String inputString = null;
        while (inputString == null) {
            try {
                if (session.getInputStreamHashMap().get(session.getKey()).ready()) {
                    try {
                        inputString = session.getInputStreamHashMap().get(session.getKey()).readLine();
                        try {
                            int newValue = Integer.parseInt(inputString);
                            if(newValue>=oldValue){
                                session.getWorkingLineInts()[session.getKey()]=newValue;
                                session.setIntValue(newValue);
                            }else {
                                System.out.println("Next line Integer "+newValue+
                                        " is less than previous "+oldValue+
                                        " and missed in file number "+session.getKey());
                                inputString = null;
                                continue;
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Input line: " + inputString +
                                    " is not VALID and missed in file number "+session.getKey());
                            inputString = null;
                            continue;
                        }
                    } catch (IOException e) {
                        System.out.println("can`t read line in file number " + session.getKey());
                    }
                }
                if(session.getWorkingLineInts()[session.getKey()]==Integer.MAX_VALUE){
                    System.out.println("Input file number "+session.getKey()+" read completely");
                    inputString="";
                }
            } catch (IOException e) {
                System.out.println("file number " + session.getKey() + " not ready");
            }
        }
    }
    private static void writeIntoOutputFile(Session session) {
        try {
            session.getOutputStream().write(String.valueOf(session.getIntValue())+"\n");
            session.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("FATAL ERROR: output file is broken");
            IOProcessor.closeOpenFiles(session);
            System.exit(1);
        }
    }
    private static void checkIteration(Session session) {
        int key = session.getKey();
        int value = session.getIntValue();
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]<value){
                key = i;
                value = session.getWorkingLineInts()[i];
                session.setKey(key);
                session.setIntValue(value);
            }
        }
    }
    private static void setInitialMin(Session session) {
        int key=Integer.MAX_VALUE;
        int value=Integer.MAX_VALUE;
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]<value){
                key = i;
                value = session.getWorkingLineInts()[i];
                session.setKey(key);
                session.setIntValue(value);
            }
        }
    }
    private static boolean checkFinish(Session session) {
        boolean flag = true;
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            if(session.getWorkingLineInts()[i]<Integer.MAX_VALUE){
                flag = false;
            }
        }
        return flag;
    }
    private static void fillInInitialLineInts(Session session,Integer lim) {
        session.setWorkingLineInts(new int[session.getInputStreamHashMap().size()]);
        for (int i = 0; i < session.getWorkingLineInts().length; i++) {
            session.getWorkingLineInts()[i]=lim;
            String inputString = null;
            while (inputString == null) {
                try {
                    if (session.getInputStreamHashMap().get(i).ready()) {
                        try {
                            inputString = session.getInputStreamHashMap().get(i).readLine();
                            try {
                                session.getWorkingLineInts()[i] = Integer.parseInt(inputString);
                            } catch (NumberFormatException e) {
                                System.out.println("Input line: " + inputString + " is not VALID and missed in file number "+i);
                                inputString = null;
                                continue;
                            }
                        } catch (IOException e) {
                            System.out.println("can`t read line in file number " + i);
                        }
                    }
                    if(session.getWorkingLineInts()[i]==lim){
                        System.out.println("ERROR: input file number "+i+" not consist any VALID ints");
                        inputString="";
                    }
                } catch (IOException e) {
                    System.out.println("file number " + i + " not ready");
                }
            }
        }
    }
}

