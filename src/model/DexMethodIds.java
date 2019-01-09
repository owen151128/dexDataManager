package model;

import java.util.List;
import java.util.Map;

public class DexMethodIds {
    private Map<String, Integer> dexList;
    public List<String> largeDexList;
    private int dexCount;

    public Map<String, Integer> getDexList() {
        return dexList;
    }

    public void setDexList(Map<String, Integer> dexList) {
        this.dexList = dexList;
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
}
