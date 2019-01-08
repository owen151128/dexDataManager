package model;

import java.util.List;
import java.util.Map;

/**
 * Dex Data Manager class
 */
public class DexDataMgr {
    private static DexDataMgr instance;

    private Map<String, Long> dexMap = null;
    private List<String> largeDexList = null;
    private int dexCount = 0;

    /**
     * Default Constructor
     */
    private DexDataMgr() {
    }

    /**
     * Single-tone method
     *
     * @return DexDataMgr instance
     */
    public static DexDataMgr getInstance() {
        if (instance == null) {
            instance = new DexDataMgr();
        }

        return instance;
    }

    public Map<String, Long> getDexMap() {
        return dexMap;
    }

    public void setDexMap(Map<String, Long> dexMap) {
        this.dexMap = dexMap;
    }

    public List<String> getLargeDexList() {
        return largeDexList;
    }

    public void setLargeDexList(List<String> largeDexList) {
        this.largeDexList = largeDexList;
    }

    public int getDexCount() {
        return dexCount;
    }

    public void setDexCount(int dexCount) {
        this.dexCount = dexCount;
    }

    /**
     * Get MethodIds by smali_name
     *
     * @param name smali_name
     * @return methodIds
     */
    public long getMethodIdsByName(String name) {
        return dexMap.get(name);
    }
}
