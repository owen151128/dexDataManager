package util;

import model.DexInfo;
import model.dex.IntHolder;
import model.dex.MethodInfo;

import java.util.*;

public class DexMethodCountUtil {
    private static DexMethodCountUtil instance = null;

    private Map<String, Integer> methodCounter = null;
    private int allMethodCount = 0;

    private DexMethodCountUtil() {
        methodCounter = new TreeMap<>();
        allMethodCount = 0;
    }

    public static DexMethodCountUtil getInstance() {
        if (instance == null) {
            instance = new DexMethodCountUtil();
        }

        return instance;
    }

    public void init() {
        methodCounter = new TreeMap<>();
        allMethodCount = 0;
    }

    public Map<String, Integer> getMethodCounter() {
        return methodCounter;
    }

    public void setMethodCounter(Map<String, Integer> methodCounter) {
        this.methodCounter = methodCounter;
    }

    public int getAllMethodCount() {
        return allMethodCount;
    }

    public void setAllMethodCount(int allMethodCount) {
        this.allMethodCount = allMethodCount;
    }

    public void caculateDexMethodCount(DexInfo dexInfo) {
        MethodInfo[] methodInfo = getMethodInfo(dexInfo);
        Map<String, IntHolder> counter = new HashMap<>();
        for (MethodInfo info : methodInfo) {
            String classDescriptor = getClassNameFromClassDescriptor(info.getDeclareClass());
            IntHolder count = counter.get(classDescriptor);

            if (count == null) {
                count = new IntHolder();
                counter.put(classDescriptor, count);
            }

            allMethodCount++;
            count.setVaule(count.getVaule() + 1);
        }

        for (Map.Entry<String, IntHolder> entry : counter.entrySet()) {
            this.methodCounter.put(entry.getKey(), entry.getValue().getVaule());
        }

        methodCounter = mapSortByVaule(methodCounter);

    }

    private static <K, V extends Comparable<? super V>> Map<K, V> mapSortByVaule(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private static String getClassNameFromClassDescriptor(String classDescriptor) {
        String result = classDescriptor.substring(1);
        result = result.replace(";", ".smali");

        return result;
    }

    private static MethodInfo[] getMethodInfo(DexInfo dexInfo) {
        return dexInfo.getMethodInfo();
    }
}
