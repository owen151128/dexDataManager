package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Providing features related to File I/O class
 */
public class FileUtil {
    private static final Logger logUtil = Logger.getLogger(FileUtil.class.getName());

    private static final String PREFIX_MINUS = "-";
    private static final String PREFIX_BLANK = "";

    private static final String ERR_DELETE_FAILED = "directory delete failed";

    /**
     * Get random directory Path from apk target
     *
     * @param apkTarget apk's Path
     * @return random directory Path from apk target
     */
    public static Path getRandomExtractPathFromTarget(Path apkTarget) {
        return new File(apkTarget.getParent() + File.separator + UUID.randomUUID().toString().replace(PREFIX_MINUS, PREFIX_BLANK)).toPath();
    }

    /**
     * Delete directory or file from parameter's path
     *
     * @param path Delete Directory or file path
     */
    public static void deleteFile(Path path) {
        File target = path.toFile();

        try {
            if (target.isDirectory()) {
                File[] list = target.listFiles();

                if (list != null) {
                    for (File f : list) {
                        if (f.isDirectory()) {
                            deleteFile(f.toPath());
                        } else {
                            Files.delete(f.toPath());
                        }
                    }
                }
            }
            Files.delete(target.toPath());
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_DELETE_FAILED, e);
        }
    }
}
