import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

interface Ram{ //Abstract Product, Adaptee
    byte[] get(int address, int size);
    int set(byte[] data, int address);

}
class ConcreteRam implements Ram{ //Concrete Product, Adaptee
    private byte[] data;

    public ConcreteRam() {
        this.data = new byte[1024]; //1 KB
    }
    public ConcreteRam(int size){
        this.data = new byte[size];
    }
    public byte[] get(int address, int size){
        byte[] data = new byte[size];
        for(int i = 0; i < size; i++){
            data[i] = this.data[address + i];
        }
        return data;
    }

    public int set(byte[] data, int address){
        try {
            for(int i = 0; i < data.length; i++){
                this.data[address + i] = data[i];
            }
            return 0;
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: Out of bounds");
            return -1;
        }

    }

    @Override
    public String toString() {
        return "Ram{" +
                "memory=" + Arrays.toString(data) +
                '}';
    }
}

interface CommunicationCard{ //common interface for Ethernet and TokenRing, Target for adapter, Receiver in Command Pattern
    byte[] getCom(int size);
    int setCom(byte[] data);
    String getName();

}
class TokenRingToComAdapter implements CommunicationCard{ //Adapter for TokenRing
    private TokenRing tokenRing;
    TokenRingToComAdapter(TokenRing tokenRing){
        this.tokenRing = tokenRing;
    }
    @Override
    public byte[] getCom(int size) {
        int[] data = tokenRing.receive(size);
        byte[] convertedData = intArrayToByteArray(data);


        return convertedData;
    }
    @Override
    public int setCom(byte[] data) {
        int[] convertedData = byteArrayToIntArray(data);

        return tokenRing.send(convertedData, convertedData.length);

    }

    @Override
    public String getName() {
        return "TokenRing";
    }



    private static byte[] intArrayToByteArray(int[] intArray) {
        int length = intArray.length;
        int byteLength = length*4;

        ByteBuffer buffer = ByteBuffer.allocate(byteLength);
        buffer.order(ByteOrder.BIG_ENDIAN); // Set the byte order if needed

        for (int i = 0; i < length; i++) {
            buffer.putInt(intArray[i]);
        }

        return Arrays.copyOf(buffer.array(), byteLength);
    }

    private static int[] byteArrayToIntArray(byte[] byteArray) {
        int[] intArray;
        int length = byteArray.length;

        if (length % 4 == 0) {
            intArray = new int[length / 4];
        } else {
            intArray = new int[length / 4 + 1];
        }

        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        buffer.order(ByteOrder.BIG_ENDIAN); // Set the byte order if needed

        for (int i = 0; i < intArray.length; i++) {
            if (i == intArray.length - 1 && length % 4 != 0) {
                byte[] temp = new byte[4];
                System.arraycopy(byteArray, i * 4, temp, 0, length % 4);
                intArray[i] = ByteBuffer.wrap(temp).getInt();
            } else {
                intArray[i] = buffer.getInt();
            }
        }

        return intArray;
    }


}
class EthernetToComAdapter implements CommunicationCard{ //Adapter for Ethernet
    private ConcreteEthernet ethernet;

    EthernetToComAdapter(ConcreteEthernet ethernet){
        this.ethernet = ethernet;
    }
    @Override
    public byte[] getCom(int size) {
        Byte[] data = ethernet.read(size);
        byte[] bytes = new byte[data.length];
        for(int i = 0; i < data.length; i++){
            if (data[i] != null){ //data may be null because the type is Byte, not byte
                bytes[i] = data[i];
            }
            else {
                bytes[i] = 0;
            }

        }
        return bytes;

    }

    @Override
    public int setCom(byte[] data) {
        Byte[] bytes = new Byte[data.length];
        for(int i = 0; i < data.length; i++){
            bytes[i] = data[i];
        }
        return ethernet.write(bytes);
    }

    @Override
    public String getName() {
        return "Ethernet";
    }
}

interface Memory{ //interface for memory related classes(there is only one class in this case which is Ram, but it can be extended), also target in adapter pattern, receiver in command pattern
    byte[] getMem(int addr, int size);
    int setMem(byte[] data, int addr);
}

class RamToMemAdapter implements Memory{ //Adapter for Ram, ConcreteReceiver in command pattern
    private Ram ram;

    RamToMemAdapter(Ram ram){
        this.ram = ram;
    }
    @Override
    public byte[] getMem(int addr, int size) {
        return ram.get(addr, size);
    }

    @Override
    public int setMem(byte[] data, int addr) {
        return ram.set(data, addr);
    }
}


interface Ethernet{ //adaptee
    Byte[] read(int size);
    Integer write(Byte[] data);
}
class ConcreteEthernet implements Ethernet{ //Adapter for Ram, ConcreteReceiver in command pattern
    private Byte[] data;

    public ConcreteEthernet() {
        data = new Byte[1024]; //1 KB
    }
    public ConcreteEthernet(int size){
        data = new Byte[size];
    }

    @Override
    public Byte[] read(int size){
        Byte[] data = new Byte[size];
        for(int i = 0; i < size; i++){
            data[i] = this.data[i];
        }
        return data;

    }

    @Override
    public String toString() {
        return "Ethernet{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
    @Override
    public Integer write(Byte[] data){
        try {
            for(int i = 0; i < data.length; i++){
                this.data[i] = data[i];
            }
            return 0;
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: Out of bounds");
            return -1;
        }

    }

}
interface TokenRing{ //adaptee
    int[] receive(int size);
    int send(int[] data, int size);
}
class ConcreteTokenRing implements TokenRing{ //Adaptee
    private int[] data;

    public ConcreteTokenRing() {
        data = new int[1024]; //1 KB
    }
    public ConcreteTokenRing(int size){
        data = new int[size];
    }
    public int[] receive(int size){
        int[] data = new int[size];
        for(int i = 0; i < size; i++){
            data[i] = this.data[i];
        }
        return data;
    }

    public int send(int[] data, int size){
        try {
            for(int i = 0; i < size; i++){
                this.data[i] = data[i];
            }
            return 0;
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Error: Out of bounds");
            return -1;
        }

    }

    @Override
    public String toString() {
        return "TokenRing{" +
                "data=" + Arrays.toString(data) +
                '}';
    }
}





