import java.util.ArrayList;

interface SystemFactory{ //Abstract Factory
    Ethernet createEthernet();
    TokenRing createTokenRing();
    Ram createMemory();
    CPU createCPU();

}

class ConcreteSystemFactory implements SystemFactory{ //Concrete Factory and Singleton
    private ConcreteSystemFactory() {
    }

    private static ConcreteSystemFactory instance;

    public static ConcreteSystemFactory getInstance(){
        if(instance == null){
            instance = new ConcreteSystemFactory();
        }
        else {
            System.out.println("SystemFactory already exists");
        }
        return instance;
    }

    @Override
    public ConcreteEthernet createEthernet() {
        return new ConcreteEthernet(10);
    }

    @Override
    public ConcreteRam createMemory() {
        return new ConcreteRam(10);
    }

    @Override
    public ConcreteCPU createCPU() {
        return new ConcreteCPU();
    }

    @Override
    public ConcreteTokenRing createTokenRing() {
        return new ConcreteTokenRing(10);
    }

}

interface CPU { //Abstract Product and Abstract Creator
    Process createProcess();
    Process createProcess(String name);
}
class ConcreteCPU implements CPU{ //Concrete Creator and Concrete Product
    private ConcreteProcess process;

    public ConcreteProcess createProcess() { //Factory method
        if (process == null){
            process = new ConcreteProcess("Unnamed Process");
        }
        else {
            System.out.println("Process already exists");
        }
        return process;
    }
    public ConcreteProcess createProcess(String name) { //Factory method
        if (process == null){
            process = new ConcreteProcess(name);
        }
        else {
            System.out.println("Process already exists");
        }
        return process;
    }
}

interface Process{ //Abstract Product and Abstract Creator
    Thread createThread();
    Thread createThread(String name);

}
class ConcreteProcess implements Process { //Concrete Creator and Concrete Product
    private ConcreteThread thread;
    private String name;

    public ConcreteProcess(String name) {
        this.name = name;
    }

    @Override
    public ConcreteThread createThread() { //Factory method
        if(thread == null){
            thread = new ConcreteThread("Unnamed Thread");
        }
        else {
            System.out.println("Thread already exists");
        }

        return thread;

    }

    @Override
    public ConcreteThread createThread(String name) { //Factory method
        if(thread == null){
            thread = new ConcreteThread(name);
        }
        else {
            System.out.println("Thread already exists");
        }

        return thread;

    }
}

abstract class Thread implements Subject{ //Abstract Product and Abstract Subject and Template Method
    protected Task task;
    protected ArrayList<Observer> observers;
    protected String name;

    public abstract void executeTask();
    public abstract void fetchTask();
    public abstract void discardTask();
    public void run(){ //Template Method
        fetchTask();
        if(task == null){
            return;
        }
        executeTask();
        notifyObservers(name + " has finished the task: " + task.getName());
        discardTask();
    }
    @Override
    public void attach(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) { //push model
        for(Observer observer : observers){
            observer.update(message);
        }
    }

    public Thread(String name){
        this.observers = new ArrayList<>();
        this.name = name;
    }


}
class ConcreteThread extends Thread{ //Concrete Product and Concrete Subject

    private TaskQueue taskQueue;

    public void fetchTask() {
        task = taskQueue.getNextTask();
    }
    public void executeTask() {
        if (task == null) {
            //notifyObservers("No task to execute");
            return;
        }
        task.execute();

    }
    public void discardTask() {task = null;}

    public ConcreteThread(String name) {
        super(name);
        taskQueue = TaskQueue.getInstance();
    }
}

