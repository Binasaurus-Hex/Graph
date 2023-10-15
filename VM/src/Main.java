import Bytecode.VirtualMachine;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static long[] convertByteArrayToLongArray(byte[] bytes) {
        if (bytes == null)
            return null;

        int count = bytes.length / 8;
        long[] longArray = new long[count];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        for (int i = 0; i < count; i++) {
            longArray[i] = byteBuffer.getLong();
        }
        return longArray;
    }
    public static void main(String[] args) {
        String filename = args[0];
        try {
            long[] program = convertByteArrayToLongArray(Files.readAllBytes(Path.of(filename)));
            VirtualMachine vm = new VirtualMachine();
            vm.run(program);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}