interface Observer { //Abstract Observer
    void update(String message);
}
interface Subject { //Abstract Subject
    void attach(Observer observer);
    void detach(Observer observer);
    void notifyObservers(String message);
}
public class SystemEventLog implements Observer{ //Concrete Observer and Singleton
    private String log;
    private static SystemEventLog instance;

    public static SystemEventLog getInstance(){
        if(instance == null){
            instance = new SystemEventLog();
        }
        else {
            System.out.println("System Event Log already exists");
        }
        return instance;
    }



    private SystemEventLog() {
        log = "";
    }

    public String getLog() {
        return log;
    }

    @Override
    public void update(String message){
        log += message + "\n";
    }

    @Override
    public String toString() {
        return "SystemEventLog{\n" +
                "log='\n" + log + '\'' +
                '}';
    }
}

