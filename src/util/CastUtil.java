package util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CastUtil {

    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }
        int startIndex = 0;
        int endIndex = array.length - 1;
        byte tmp;
        while (endIndex > startIndex) {
            tmp = array[endIndex];
            array[endIndex] = array[startIndex];
            array[startIndex] = tmp;
            endIndex--;
            startIndex++;
        }
    }

    public static byte[] intToByteArray(int integer, ByteOrder byteOrder) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
        buffer.order(byteOrder);

        buffer.putInt(integer);

        return buffer.array();
    }

    public static int byteArrayToInt(byte[] array, ByteOrder order) {
        ByteBuffer buffer = ByteBuffer.allocate(Integer.SIZE / 8);
        buffer.order(order);

        buffer.put(array);
        buffer.flip();

        return buffer.getInt();
    }
}
