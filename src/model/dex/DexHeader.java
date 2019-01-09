package model.dex;

public class DexHeader {
    private int fileSize;
    private int headerSize;
    private int endianTag;
    private int stringIdsSize;
    private int stringIdsOff;
    private int typeIdsSize;
    private int typeIdsOff;
    private int protoIdsSize;
    private int protoIdsOff;
    private int fieldIdsSize;
    private int fieldIdsOff;
    private int methodIdsSize;
    private int methodIdsOff;
    private int classDefsSize;
    private int classDefsOff;

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }

    public int getEndianTag() {
        return endianTag;
    }

    public void setEndianTag(int endianTag) {
        this.endianTag = endianTag;
    }

    public int getStringIdsSize() {
        return stringIdsSize;
    }

    public void setStringIdsSize(int stringIdsSize) {
        this.stringIdsSize = stringIdsSize;
    }

    public int getStringIdsOff() {
        return stringIdsOff;
    }

    public void setStringIdsOff(int stringIdsOff) {
        this.stringIdsOff = stringIdsOff;
    }

    public int getTypeIdsSize() {
        return typeIdsSize;
    }

    public void setTypeIdsSize(int typeIdsSize) {
        this.typeIdsSize = typeIdsSize;
    }

    public int getTypeIdsOff() {
        return typeIdsOff;
    }

    public void setTypeIdsOff(int typeIdsOff) {
        this.typeIdsOff = typeIdsOff;
    }

    public int getProtoIdsSize() {
        return protoIdsSize;
    }

    public void setProtoIdsSize(int protoIdsSize) {
        this.protoIdsSize = protoIdsSize;
    }

    public int getProtoIdsOff() {
        return protoIdsOff;
    }

    public void setProtoIdsOff(int protoIdsOff) {
        this.protoIdsOff = protoIdsOff;
    }

    public int getFieldIdsSize() {
        return fieldIdsSize;
    }

    public void setFieldIdsSize(int fieldIdsSize) {
        this.fieldIdsSize = fieldIdsSize;
    }

    public int getFieldIdsOff() {
        return fieldIdsOff;
    }

    public void setFieldIdsOff(int fieldIdsOff) {
        this.fieldIdsOff = fieldIdsOff;
    }

    public int getMethodIdsSize() {
        return methodIdsSize;
    }

    public void setMethodIdsSize(int methodIdsSize) {
        this.methodIdsSize = methodIdsSize;
    }

    public int getMethodIdsOff() {
        return methodIdsOff;
    }

    public void setMethodIdsOff(int methodIdsOff) {
        this.methodIdsOff = methodIdsOff;
    }

    public int getClassDefsSize() {
        return classDefsSize;
    }

    public void setClassDefsSize(int classDefsSize) {
        this.classDefsSize = classDefsSize;
    }

    public int getClassDefsOff() {
        return classDefsOff;
    }

    public void setClassDefsOff(int classDefsOff) {
        this.classDefsOff = classDefsOff;
    }
}
