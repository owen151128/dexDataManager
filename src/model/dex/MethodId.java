package model.dex;

public class MethodId {
    private int classIdx;
    private int protoIdx;
    private int nameIdx;

    public int getClassIdx() {
        return classIdx;
    }

    public void setClassIdx(int classIdx) {
        this.classIdx = classIdx;
    }

    public int getProtoIdx() {
        return protoIdx;
    }

    public void setProtoIdx(int protoIdx) {
        this.protoIdx = protoIdx;
    }

    public int getNameIdx() {
        return nameIdx;
    }

    public void setNameIdx(int nameIdx) {
        this.nameIdx = nameIdx;
    }
}
