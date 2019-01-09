package util;

import model.DexMethodIds;
import model.dex.DexAddress;
import sun.jvm.hotspot.debugger.Address;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Providing features related to Dex class
 */
public class DexUtil {
    private static final Logger logUtil = Logger.getLogger(DexUtil.class.getName());

    private static final int LIMIT_METHOD_IDS = 55000;

    private static final String DEX_EXTENSION = ".dex";
    private static final String MAIN_DEX_PREFIX = "classes.dex";
    private static final String MAIN_SMALI = "smali";
    private static final String SMALI_PREFIX = "smali_classes";

    private static final String ERR_APK_EXTRACT_IO = "There was an error extract apk";

    /**
     * Default Constructor
     */
    private DexUtil() {
    }

    /**
     * Extract the many of dex from Apk
     *
     * @param targetApk       apk path
     * @param targetDirectory extract target path
     * @return if success, return true
     */
    public static List<Path> dexExtractFromApk(Path targetApk, Path targetDirectory) {
        List<Path> resultList = new ArrayList<>();
        try {
            ZipInputStream zis = new ZipInputStream(Files.newInputStream(targetApk));
            ZipEntry entry;

            Path extractTarget;
            Path parentDirectory;

            if (Files.exists(targetDirectory)) {
                Files.createDirectory(targetDirectory);
            }

            while ((entry = zis.getNextEntry()) != null) {
                if (entry.getName().endsWith(DEX_EXTENSION)) {
                    extractTarget = targetDirectory.resolve(entry.getName());
                    parentDirectory = extractTarget.getParent();

                    Files.createDirectories(parentDirectory);
                    Files.copy(zis, extractTarget);
                    resultList.add(extractTarget);
                }
            }
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_APK_EXTRACT_IO, e);

            return null;
        }

        return resultList;
    }

    /**
     * Get MethodInfo
     *
     * @param targetDirectory dexFile's directory
     * @return DexMethodIds
     */
    public static DexMethodIds getMethodInfo(Path targetDirectory) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<String> resultList = new ArrayList<>();
        int dexCount = 0;
        DexMethodIds result = new DexMethodIds();
        File[] fileArray = targetDirectory.toFile().listFiles();
        ByteReadUtil byteReadUtil;

        int index = 2;

        if (fileArray != null) {
            for (File f : fileArray) {
                byteReadUtil = new ByteReadUtil(f.getAbsolutePath());
                int methodIds = byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.METHOD_IDS_SIZE);

                if (f.getName().equals(MAIN_DEX_PREFIX)) {
                    if (methodIds > LIMIT_METHOD_IDS) {
                        resultList.add(MAIN_SMALI);
                    }

                    resultMap.put(MAIN_SMALI, methodIds);
                } else {
                    if (methodIds > LIMIT_METHOD_IDS) {
                        resultList.add(SMALI_PREFIX + index);
                    }

                    resultMap.put(SMALI_PREFIX + index, byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.METHOD_IDS_SIZE));
                    index++;
                }
                byteReadUtil.close();
                dexCount++;
            }
        }

        result.setDexList(resultMap);
        result.setLargeDexList(resultList);
        result.setDexCount(dexCount);

        return result;
    }
}
