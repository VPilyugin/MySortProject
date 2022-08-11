package MySort;

public class MySortProject {
    public static void main(String[] args) {
        Session session = new Session(args);
        IOProcessor.checkInputParams(session);
        IOProcessor.openOutputFile(session);
        IOProcessor.getConnections(session);
        IOProcessor.mergeFiles(session);
        IOProcessor.closeOpenFiles(session);
    }
}
