package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Providing features related to Read Byte class
 */
public class ByteReadUtil {
    private static final String READ_MODE = "r";
    private static final String UTF_8 = "UTF-8";

    private static final int ONE = 1;
    private static final int INTEGER = 4;

    private static final String ERR_FILE_NOT_FOUND = "apk file not found";
    private static final String ERR_FILE_READ_FAILED = "apk file read failed";
    private static final String ERR_UTIL_INIT_FAILED = "ByteReadUtil initialize failed.";
    private static final String ERR_CLOSE_FAILED = "ByteReadUtil close failed";

    private static final Logger logUtil = Logger.getLogger(ByteReadUtil.class.getName());

    private RandomAccessFile filePointer = null;
    private FileChannel file = null;
    private Charset charset = null;

    private ByteReadUtil() {
    }

    /**
     * default constructor, set FilePointer
     *
     * @param targetPath Read targetPath
     */
    public ByteReadUtil(String targetPath) {
        try {
            filePointer = new RandomAccessFile(targetPath, READ_MODE);
            file = filePointer.getChannel();
            charset = Charset.forName(UTF_8);
        } catch (FileNotFoundException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_NOT_FOUND, e);
        }
    }

    /**
     * Read bytes to integer from File
     *
     * @param address start DexAddress
     * @return byte to integer data
     */
    public int readBytesToInt(int address) {
        int result = 0;

        try {
            if (filePointer == null || file == null) {
                logUtil.log(Level.SEVERE, ERR_UTIL_INIT_FAILED);
                return result;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INTEGER);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            filePointer.seek(address);
            file.read(byteBuffer);
            byteBuffer.flip();

            result = byteBuffer.getInt();

            byteBuffer.clear();
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_READ_FAILED, e);
        }

        return result;
    }

    /**
     * Read bytes to short from File
     *
     * @param address start DexAddress
     * @return byte to short data
     */
    public int readBytesToShort(int address) {
        short result = 0;

        try {
            if (filePointer == null || file == null) {
                logUtil.log(Level.SEVERE, ERR_UTIL_INIT_FAILED);
                return result;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(INTEGER);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            filePointer.seek(address);
            file.read(byteBuffer);
            byteBuffer.flip();

            result = byteBuffer.getShort();

            byteBuffer.clear();
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_READ_FAILED, e);
        }

        return result;
    }

    /**
     * Read bytes to Ascii String from File
     *
     * @param address start DexAddress
     * @return byte to asciiString data
     */
    public String readBytesToAsciiString(int address, int offset) {
        String result = null;

        try {
            if (filePointer == null || file == null) {
                logUtil.log(Level.SEVERE, ERR_UTIL_INIT_FAILED);
                return "";
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(offset);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            filePointer.seek(address);
            file.read(byteBuffer);

            byteBuffer.flip();
            byte[] byteArray = new byte[byteBuffer.limit()];
            byteBuffer.get(byteArray);
            result = new String(byteArray, charset);

            byteBuffer.clear();
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_READ_FAILED, e);
        }

        return result;
    }

    /**
     * Read bytes to Dex String from File
     *
     * @param address start DexAddress
     * @return byte to dexString data
     */
    public String readBytesToString(int address) {
        String result = null;

        try {
            if (filePointer == null || file == null) {
                logUtil.log(Level.SEVERE, ERR_UTIL_INIT_FAILED);
                return "";
            }
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(getUnsignedLeb128(address) * 3);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            filePointer.seek(++address);
            file.read(byteBuffer);

            byteBuffer.flip();
            byte[] byteArray = new byte[byteBuffer.limit()];
            byteBuffer.get(byteArray);
            result = new String(byteArray, charset);

            if (result.indexOf('\0') != -1) {
                result = result.substring(0, result.indexOf('\0'));
            }

            byteBuffer.clear();
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_READ_FAILED, e);
        }

        return result;
    }

    /**
     * Get Leb128 integer
     *
     * @param address startAddress
     * @return leb128 integer
     */
    private int getUnsignedLeb128(int address) {
        int result = 0;
        byte value;
        ByteBuffer byteBuffer = null;

        try {
            do {
                byteBuffer = ByteBuffer.allocateDirect(ONE);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                filePointer.seek(address++);
                file.read(byteBuffer);

                byteBuffer.flip();
                value = byteBuffer.get();
                result = (result << 7) | (value & 0x7f);
                byteBuffer.clear();
            } while (value < 0);

        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_READ_FAILED, e);
        }

        return result;
    }

    /**
     * ByteReadUtil close method
     * do resource clean
     */
    public void close() {
        try {
            file.close();
            filePointer.close();
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_CLOSE_FAILED, e);
        }
    }
}
