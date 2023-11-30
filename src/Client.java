import java.util.Arrays;

public class Client {
    public static void main(String[] args) {
        ConcreteSystemFactory concreteSystemFactory = ConcreteSystemFactory.getInstance();

        Byte[] dataWrapped = {1,2,3,4,5,6,7,8,9};

        Ram ram = concreteSystemFactory.createMemory(); //size of ram is 10 bytes
        ConcreteEthernet ethernet = concreteSystemFactory.createEthernet(); //size of ethernet is 10 bytes
        ConcreteTokenRing tokenRing = concreteSystemFactory.createTokenRing(); //size of token ring is 10 * 4 bytes

        ethernet.write(dataWrapped); //we need to write to the ethernet before we can read from it

        System.out.println("Initial state of the system:");
        System.out.println(ram);
        System.out.println(ethernet);
        System.out.println(tokenRing);
        System.out.println();

        /* these are the adapters we need to pass them to the tasks */
        EthernetToComAdapter ethernetAdapter = new EthernetToComAdapter(ethernet);
        RamToMemAdapter ramAdapter = new RamToMemAdapter(ram);
        TokenRingToComAdapter tokenRingAdapter = new TokenRingToComAdapter(tokenRing);
        /* ---------------------------------------------- */

        /* these are the tasks mentioned in the assignment */
        ReadCardTask readCardTask = new ReadCardTask(ethernetAdapter, dataWrapped.length);
        WriteMemoryTask writeMemoryTask = new WriteMemoryTask(ramAdapter, readCardTask.getTaskData(), 0);
        WriteCardTask writeCardTask = new WriteCardTask(tokenRingAdapter, readCardTask.getTaskData());
        /* ---------------------------------------------- */

        /* an extra task */
        ReadMemoryTask readMemoryTask = new ReadMemoryTask(ramAdapter, 0, dataWrapped.length);
        /* -------------- */


        /* append to the composite */
        CompositeTask compositeTask = new CompositeTask();
        compositeTask.addTask(readCardTask);
        compositeTask.addTask(writeMemoryTask);
        compositeTask.addTask(writeCardTask);
        /* ----------------------- */

        TaskQueue taskQueue = TaskQueue.getInstance();
        taskQueue.addTask(compositeTask);
        taskQueue.addTask(readMemoryTask);

        CPU cpu1 = concreteSystemFactory.createCPU();
        CPU cpu2 = concreteSystemFactory.createCPU();

        Process process1 = cpu1.createProcess("Process 1");
        Process process2 = cpu2.createProcess("Process 2");

        Thread thread1 = process1.createThread("Thread 1");
        Thread thread2 = process2.createThread("Thread 2");


        SystemEventLog systemEventLog = SystemEventLog.getInstance();

        thread1.attach(systemEventLog);
        thread2.attach(systemEventLog);

        thread1.run();
        thread2.run();

        System.out.println(systemEventLog);
        System.out.println();
        System.out.println("Final state of the system:");
        System.out.println(ram);
        System.out.println(ethernet);
        System.out.println(tokenRing + "(this is converted to an int array from a byte array)");

    }
}


