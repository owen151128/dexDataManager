package util;

/**
 * Providing features related to Array class
 */
public class ArrayUtil {

    /**
     * Byte array's elements index reverse method
     *
     * @param array target Byte Array
     */
    public static void reverse(byte[] array) {
        if (array == null) {
            return;
        }

        int index = 0;
        int limitIndex = array.length - 1;
        byte tmp;

        while (limitIndex > index) {
            tmp = array[limitIndex];
            array[limitIndex] = array[index];
            array[index] = tmp;
            limitIndex--;
            index++;
        }
    }
}
