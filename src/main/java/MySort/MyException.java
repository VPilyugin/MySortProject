package MySort;

public class MyException extends Exception{
    private IOProcessor.Errors er;
    public MyException(IOProcessor.Errors error){
        super(error.toString());
        er = error;
    }

    @Override
    public String toString() {
        String returnString = er.getDescription();
        return returnString;
    }
}
