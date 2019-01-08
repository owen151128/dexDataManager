package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Providing features related to Read Byte class
 */
public class ByteReadUtil {
    private static final String READ_MODE = "r";

    private static final String ERR_FILE_NOT_FOUND = "apk file not found";
    private static final String ERR_FILE_READ_FAILED = "apk file read failed";
    private static final String ERR_CLOSE_FAILED = "ByteReadUtil close failed";

    private static final Logger logUtil = Logger.getLogger(ByteReadUtil.class.getName());

    private RandomAccessFile filePointer = null;
    private FileChannel file = null;

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
        } catch (FileNotFoundException e) {
            logUtil.log(Level.SEVERE, ERR_FILE_NOT_FOUND, e);
        }
    }

    /**
     * Read bytes from File
     *
     * @param address start Address
     * @param offset  byte Offset
     * @return byte to long data
     */
    public long readBytes(int address, int offset) {
        long result = 0;

        try {
            if (filePointer == null || file == null) {
                logUtil.log(Level.SEVERE, "ByteReadUtil initialize failed.");
                return result;
            }

            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(offset);
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
