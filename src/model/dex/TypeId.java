package model.dex;

public class TypeId {
    private int descriptorIdx;
    private boolean internal;

    public int getDescriptorIdx() {
        return descriptorIdx;
    }

    public void setDescriptorIdx(int descriptorIdx) {
        this.descriptorIdx = descriptorIdx;
    }

    public boolean isInternal() {
        return internal;
    }

    public void setInternal(boolean internal) {
        this.internal = internal;
    }
}
