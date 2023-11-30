import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

abstract class Task{ //Abstract Command, Component of Composite Pattern
    abstract void execute();
    abstract String getName();
    protected String name;
}

class TaskData{ //Mediator for data transfer between tasks(commands)
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    TaskData(byte[] data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "TaskData{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}

class ReadMemoryTask extends Task{ //Concrete Command and Leaf of Composite Pattern
    private Memory memory;
    private int address;
    private int size;

    @Override
    public String getName() {
        return name;
    }

    private TaskData taskData;
    public TaskData getTaskData(){
        return taskData;
    }
    public ReadMemoryTask(Memory memory, int address, int size) {
        this.memory = memory;
        this.address = address;
        this.size = size;
        this.name = "ReadMemoryTask";
        this.taskData = new TaskData(new byte[size]);
    }

    @Override
    public void execute() {
        taskData.setData(memory.getMem(address, size));
    }
}
class WriteMemoryTask extends Task{ //Concrete Command and Leaf of Composite Pattern
    private Memory memory;

    private int address;
    private int result;
    private TaskData taskData;

    public WriteMemoryTask(Memory memory, TaskData taskData, int address) {
        this.memory = memory;
        this.taskData = taskData;
        this.address = address;
        this.name = "WriteMemoryTask";
    }

    public int getResult(){
        return result;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void execute() {
        result = memory.setMem(taskData.getData(), address);
    }
}
class ReadCardTask extends Task { //Concrete Command and Leaf of Composite Pattern
    private CommunicationCard communicationCard;
    private int size;

    private TaskData taskData;

    public TaskData getTaskData(){
        return taskData;
    }

    public ReadCardTask(CommunicationCard communicationCard, int size) {
        this.communicationCard = communicationCard;
        this.size = size;
        taskData = new TaskData(new byte[size]);
    }

    @Override
    public void execute() {
        byte[] data  = communicationCard.getCom(size);
        taskData.setData(data);
    }
    @Override
    public String getName() {
        return "Read" + communicationCard.getName() + "Task";
    }
}
class WriteCardTask extends Task{ //Concrete Command and Leaf of Composite Pattern
    private CommunicationCard communicationCard;

    private int result;
    private TaskData taskData;

    public WriteCardTask(CommunicationCard communicationCard, TaskData taskData) {
        this.communicationCard = communicationCard;
        this.taskData = taskData;
    }
    public String getName() {
        return "Write" + communicationCard.getName() + "Task";
    }

    @Override
    public void execute() {
        result = communicationCard.setCom(taskData.getData());
    }

    public int getResult() {
        return result;
    }
}
class CompositeTask extends Task{ //Concrete Command and Composite of Composite Pattern
    private ArrayList<Task> tasks;

    public CompositeTask(ArrayList<Task>tasks) {
        this.tasks = tasks;
    }
    public CompositeTask(){tasks = new ArrayList<>();}
    public void addTask(Task task){
        tasks.add(task);
    }
    public void removeTask(Task task){
        tasks.remove(task);
    }

    @Override
    public void execute() {
        for(Task task : tasks){
            task.execute();
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    @Override
    String getName() {
        String name = "(";
        for (Task task:tasks) {
            name += task.getName() + " ";
        }
        name = name.substring(0, name.length() - 1);
        name += ") CompositeTask";
        return name;
    }
}
class TaskQueue{
    private Queue<Task> queue;
    private static TaskQueue instance;

    public static TaskQueue getInstance() {
        if (instance == null) {
            instance = new TaskQueue();
        }
        return instance;
    }

    private TaskQueue() {
        this.queue = new LinkedList<>();
    }

    public void addTask(Task task) {
        queue.add(task);
    }

    public Task getNextTask() {
        return queue.poll();
    }
}
