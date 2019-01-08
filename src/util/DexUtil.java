package util;

import model.Pair;

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

    private static final int METHOD_IDS_ADDRESS = 0x58;
    private static final int METHOD_IDS_OFFSET = 4;
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
    public static boolean dexExtractFromApk(Path targetApk, Path targetDirectory) {
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
                }
            }
        } catch (IOException e) {
            logUtil.log(Level.SEVERE, ERR_APK_EXTRACT_IO, e);

            return false;
        }

        return true;
    }

    /**
     * Return smaliHashMap and LargeDexList pair, dexMap's key is smali's name, value is dex's methodIds
     *
     * @param targetDirectory dexExtract path
     * @return smaliHashMap and LargeDexList pair, dexMap's key is smali's name, value is dex's methodIds
     */
    public static Pair<Map<String, Long>, List<String>, Integer> getMethodInfo(Path targetDirectory) {
        Map<String, Long> resultMap = new HashMap<>();
        List<String> resultList = new ArrayList<>();
        int dexCount = 0;
        Pair<Map<String, Long>, List<String>, Integer> resultPair;
        File[] fileArray = targetDirectory.toFile().listFiles();
        ByteReadUtil byteReadUtil;

        int index = 2;

        if (fileArray != null) {
            for (File f : fileArray) {
                byteReadUtil = new ByteReadUtil(f.getAbsolutePath());
                long methodIds = byteReadUtil.readBytes(METHOD_IDS_ADDRESS, METHOD_IDS_OFFSET);

                if (f.getName().equals(MAIN_DEX_PREFIX)) {
                    if (methodIds > LIMIT_METHOD_IDS) {
                        resultList.add(MAIN_SMALI);
                    }

                    resultMap.put(MAIN_SMALI, methodIds);
                } else {
                    if (methodIds > LIMIT_METHOD_IDS) {
                        resultList.add(SMALI_PREFIX + index);
                    }

                    resultMap.put(SMALI_PREFIX + index, byteReadUtil.readBytes(METHOD_IDS_ADDRESS, METHOD_IDS_OFFSET));
                    index++;
                }
                byteReadUtil.close();
                dexCount++;
            }
        }

        resultPair = new Pair<>(resultMap, resultList, dexCount);
        return resultPair;
    }
}
