package MySort;

import java.io.IOException;

public class StringService {
    private StringService(){

    }
    public static void merge(Session session) {
        if (session.getSortMode().equals(IOProcessor.SortMode.ASC)) {
            mergeStringAsc(session);
        } else {
            mergeStringDesc(session);
        }
    }

    private static void mergeStringDesc(Session session) {
        fillInInitialLineString(session);
        setInitialMax(session);
        boolean finishMerge = checkFinish(session);
        while (!finishMerge) {
            finishMerge = checkFinish(session);
            if (finishMerge) continue;
            checkIterationDesc(session);
            writeIntoOutputFile(session);
            getNextValueDesc(session);
            if (session.getWorkingLineString()[session.getKey()] == null) {
                setInitialMax(session);
            }
        }
    }
    private static void getNextValueDesc(Session session) {
        String oldValue = session.getWorkingLineString()[session.getKey()];
        session.getWorkingLineString()[session.getKey()] = null;
        String inputString = null;
        while (inputString == null) {
            try {
                if (session.getInputStreamHashMap().get(session.getKey()).ready()) {
                    try {
                        inputString = session.getInputStreamHashMap().get(session.getKey()).readLine();
                        int compare = oldValue.compareTo(inputString);
                        if (compare >= 0) {
                            session.getWorkingLineString()[session.getKey()] = inputString;
                            session.setStringValue(inputString);
                        } else {
                            System.out.println("Next line String " + inputString +
                                    " is bigger than previous " + oldValue +
                                    " and missed in file number " + session.getKey());
                            inputString = null;
                            continue;
                        }
                    } catch (IOException e) {
                        System.out.println("can`t read line in file number " + session.getKey());
                    }
                }
                if (session.getWorkingLineString()[session.getKey()]==null) {
                    System.out.println("Input file number " + session.getKey() + " read completely");
                    inputString = "";
                }
            } catch (IOException e) {
                System.out.println("file number " + session.getKey() + " not ready");
            }
        }
    }
private static void checkIterationDesc(Session session) {
    int key = session.getKey();
    String value = session.getWorkingLineString()[session.getKey()];
    for (int i = 0; i < session.getWorkingLineString().length; i++) {
        if (session.getWorkingLineString()[i]==null) {
            continue;
        }
        if (value == null) {
            key = i;
            value = session.getWorkingLineString()[i];
            session.setKey(key);
            session.setStringValue(value);
        } else {
            String testValue = session.getWorkingLineString()[i];
            int compare = value.compareTo(testValue);
            if (compare <= 0) {
                key = i;
                value = session.getWorkingLineString()[i];
                session.setKey(key);
                session.setStringValue(value);
            }
        }
    }
}

private static void setInitialMax(Session session) {
    int key = -1;
    String value = null;
    for (int i = 0; i < session.getWorkingLineString().length; i++) {
        if (session.getWorkingLineString()[i] == null) {
            continue;
        }
        if (value == null) {
            value = session.getWorkingLineString()[i];
            key = i;
            session.setKey(key);
            session.setStringValue(value);
            continue;
        }
        int compare = session.getWorkingLineString()[i].compareTo(value);
        if (compare > 0) {
            key = i;
            value = session.getWorkingLineString()[i];
            session.setKey(key);
            session.setStringValue(value);
        }
    }
}
    private static void mergeStringAsc(Session session) {
        fillInInitialLineString(session);
        setInitialMin(session);
        boolean finishMerge = checkFinish(session);
        while (!finishMerge) {
            finishMerge = checkFinish(session);
            if (finishMerge) continue;
            checkIteration(session);
            writeIntoOutputFile(session);
            getNextValue(session);
            if (session.getWorkingLineString()[session.getKey()]==null) {
                setInitialMin(session);
            }
        }
    }
    private static void getNextValue(Session session) {
        String oldValue = session.getWorkingLineString()[session.getKey()];
        session.getWorkingLineString()[session.getKey()] = null;
        String inputString = null;
        while (inputString == null) {
            try {
                if (session.getInputStreamHashMap().get(session.getKey()).ready()) {
                    try {
                        inputString = session.getInputStreamHashMap().get(session.getKey()).readLine();
                        int compare = oldValue.compareTo(inputString);
                        if (compare <= 0) {
                            session.getWorkingLineString()[session.getKey()] = inputString;
                            session.setStringValue(inputString);
                        } else {
                            System.out.println("Next line String " + inputString +
                                    " is less than previous " + oldValue +
                                    " and missed in file number " + session.getKey());
                            inputString = null;
                            continue;
                        }
                    } catch (IOException e) {
                        System.out.println("can`t read line in file number " + session.getKey());
                    }
                }
                if (session.getWorkingLineString()[session.getKey()]==null) {
                    System.out.println("Input file number " + session.getKey() + " read completely");
                    inputString = "";
                }
            } catch (IOException e) {
                System.out.println("file number " + session.getKey() + " not ready");
            }
        }
    }
    private static void writeIntoOutputFile(Session session) {
        try {
            session.getOutputStream().write(session.getStringValue() + "\n");
            session.getOutputStream().flush();
        } catch (IOException e) {
            System.out.println("FATAL ERROR: output file is broken");
            IOProcessor.closeOpenFiles(session);
            System.exit(1);
        }
    }
    private static void checkIteration(Session session) {
        int key = session.getKey();
        String value = session.getWorkingLineString()[session.getKey()];
        for (int i = 0; i < session.getWorkingLineString().length; i++) {
            if (session.getWorkingLineString()[i]==null) {
                continue;
            }
            if (value == null) {
                key = i;
                value = session.getWorkingLineString()[i];
                session.setKey(key);
                session.setStringValue(value);
            } else {
                String testValue = session.getWorkingLineString()[i];
                int compare = value.compareTo(testValue);
                if (compare >= 0) {
                    key = i;
                    value = session.getWorkingLineString()[i];
                    session.setKey(key);
                    session.setStringValue(value);
                }

            }
        }
    }
    private static boolean checkFinish(Session session) {
        boolean flag = true;
        for (int i = 0; i < session.getWorkingLineString().length; i++) {
            if (session.getWorkingLineString()[i]!=null) {
                flag = false;
            }
        }
        return flag;
    }
    private static void setInitialMin(Session session) {
        int key = -1;
        String value = null;
        for (int i = 0; i < session.getWorkingLineString().length; i++) {
            if (session.getWorkingLineString()[i]==null) {
                continue;
            }
            if (value == null) {
                value = session.getWorkingLineString()[i];
                key = i;
                session.setKey(key);
                session.setStringValue(value);
                continue;
            }
            int compare = session.getWorkingLineString()[i].compareTo(value);
            if (compare < 0) {
                key = i;
                value = session.getWorkingLineString()[i];
                session.setKey(key);
                session.setStringValue(value);
            }
        }
    }
        private static void fillInInitialLineString(Session session) {
        session.setWorkingLineString(new String[session.getInputStreamHashMap().size()]);
        for (int i = 0; i < session.getWorkingLineString().length; i++) {
            String inputString = null;
            while (inputString == null) {
                try {
                    if (session.getInputStreamHashMap().get(i).ready()) {
                        try {
                            inputString = session.getInputStreamHashMap().get(i).readLine();
                            session.getWorkingLineString()[i] = inputString;
                        } catch (IOException e) {
                            System.out.println("can`t read line in file number " + i);
                        }
                    }
                    if(session.getWorkingLineString()[i]==null){
                        System.out.println("ERROR: input file number "+i+" not consist any VALID string");
                        inputString="";
                    }
                } catch (IOException e) {
                    System.out.println("file number " + i + " not ready");
                }
            }
        }
    }
}






