package model.dex;

import java.util.Arrays;

public class MethodInfo {
    private String declareClass;
    private String returnType;
    private String methodName;
    private String[] argumentType;

    public MethodInfo() {
    }

    public MethodInfo(String declareClass, String returnType, String methodName, String[] argumentType) {
        this.declareClass = declareClass;
        this.returnType = returnType;
        this.methodName = methodName;
        this.argumentType = argumentType;
    }

    public String getDeclareClass() {
        return declareClass;
    }

    public void setDeclareClass(String declareClass) {
        this.declareClass = declareClass;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgumentType() {
        return argumentType;
    }

    public void setArgumentType(String[] argumentType) {
        this.argumentType = argumentType;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MethodInfo)) {
            return false;
        }

        MethodInfo info = (MethodInfo) obj;

        return info.declareClass.equals(declareClass) &&
                info.returnType.equals(returnType) &&
                info.methodName.equals(methodName) &&
                Arrays.equals(info.argumentType, argumentType);
    }

    @Override
    public int hashCode() {
        return declareClass.hashCode() ^ returnType.hashCode() ^ methodName.hashCode() ^ Arrays.hashCode(argumentType);
    }
}
