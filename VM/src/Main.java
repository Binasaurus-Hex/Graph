import Bytecode.VirtualMachine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static int[] convertByteArrayToIntArray(byte[] bytes) {
        if (bytes == null)
            return null;

        int count = bytes.length / Integer.BYTES;
        int[] longArray = new int[count];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        for (int i = 0; i < count; i++) {
            ByteOrder order = byteBuffer.order();
            longArray[i] = byteBuffer.getInt();
        }
        return longArray;
    }
    public static void main(String[] args) {
        String filename = args[0];
        try {
            int[] program = convertByteArrayToIntArray(Files.readAllBytes(Path.of(filename)));
            VirtualMachine vm = new VirtualMachine();
            int[] output = vm.run(program);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}