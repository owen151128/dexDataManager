package model.dex;

public class ProtoId {
    private int shortyIdx;
    private int returnTypeIdx;
    private int parametersOff;

    private int types[];

    public int getShortyIdx() {
        return shortyIdx;
    }

    public void setShortyIdx(int shortyIdx) {
        this.shortyIdx = shortyIdx;
    }

    public int getReturnTypeIdx() {
        return returnTypeIdx;
    }

    public void setReturnTypeIdx(int returnTypeIdx) {
        this.returnTypeIdx = returnTypeIdx;
    }

    public int getParametersOff() {
        return parametersOff;
    }

    public void setParametersOff(int parametersOff) {
        this.parametersOff = parametersOff;
    }

    public int[] getTypes() {
        return types;
    }

    public void setTypes(int[] types) {
        this.types = types;
    }
}
