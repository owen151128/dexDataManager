package model;

import model.dex.*;
import util.ByteReadUtil;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DexInfo {
    private static final Logger logUtil = Logger.getLogger(DexInfo.class.getName());

    private static final String BLANK_ERASE_REGEX = "(^\\p{Z}+|\\p{Z}+$)";
    private static final String BLANK = "";
    private static final String ERR_NOT_DEX = "This file isn't dex format";

    private ByteReadUtil byteReadUtil;

    private DexHeader dexHeader;
    private String[] strings;
    private TypeId[] typeIds;
    private ProtoId[] protoIds;
    private FieldId[] fieldIds;
    private MethodId[] methodIds;
    private ClassDef[] classDefs;

    private boolean isBigEndian = false;

    public DexInfo(Path dexFile) throws FileNotFoundException {
        byteReadUtil = new ByteReadUtil(dexFile.toFile().getAbsolutePath());
    }

    public boolean isDexFile(String magic) {
        return Arrays.equals(magic.getBytes(), DexAddress.DEX_FILE_MAGIC) || Arrays.equals(magic.getBytes(), DexAddress.DEX_FILE_MAGIC_HONEY_COM);
    }

    public void loadDexHeader() {
        dexHeader = new DexHeader();

        String magic = byteReadUtil.readBytesToAsciiString(DexAddress.STARTADDRESS.MAGIC, DexAddress.OFFSET.MAGIC);

        if (!isDexFile(magic)) {
            logUtil.log(Level.SEVERE, ERR_NOT_DEX);
            throw new DexDataIllegalException();
        }

        dexHeader.setEndianTag(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.ENDIAN_TAG));

        if (dexHeader.getEndianTag() == DexAddress.BIG_ENDIAN) {
            isBigEndian = true;
        } else if (dexHeader.getEndianTag() != DexAddress.LITTLE_ENDIAN) {
            throw new DexDataIllegalException();
        }

        dexHeader.setFileSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.FILE_SIZE));
        dexHeader.setHeaderSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.HEADER_SIZE));
        dexHeader.setStringIdsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.STRING_IDS_SIZE));
        dexHeader.setStringIdsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.STRING_IDS_OFF));
        dexHeader.setTypeIdsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.TYPE_IDS_SIZE));
        dexHeader.setTypeIdsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.TYPE_IDS_OFF));
        dexHeader.setProtoIdsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.PROTO_IDS_SIZE));
        dexHeader.setProtoIdsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.PROTO_IDS_OFF));
        dexHeader.setFieldIdsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.FIELD_IDS_SIZE));
        dexHeader.setFieldIdsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.FIELD_IDS_OFF));
        dexHeader.setMethodIdsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.METHOD_IDS_SIZE));
        dexHeader.setMethodIdsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.METHOD_IDS_OFF));
        dexHeader.setClassDefsSize(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.CLASS_DEFS_SIZE));
        dexHeader.setClassDefsOff(byteReadUtil.readBytesToInt(DexAddress.STARTADDRESS.CLASS_DEFS_OFF));
    }

    public void loadStrings() {
        int size = dexHeader.getStringIdsSize();
        int stringOffsets[] = new int[size];
        int offsetIndex = dexHeader.getStringIdsOff();

        for (int i = 0; i < size; i++) {
            stringOffsets[i] = byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER));
        }

        strings = new String[size];

        for (int i = 0; i < size; i++) {
            strings[i] = byteReadUtil.readBytesToString(stringOffsets[i]);
        }
    }

    public void loadTypeIds() {
        int size = dexHeader.getTypeIdsSize();
        typeIds = new TypeId[size];
        int offsetIndex = dexHeader.getTypeIdsOff();

        for (int i = 0; i < size; i++) {
            typeIds[i] = new TypeId();
            typeIds[i].setDescriptorIdx(byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER)));
        }
    }

    public void loadProtoIds() {
        int size = dexHeader.getProtoIdsSize();
        protoIds = new ProtoId[size];
        int offsetIndex = dexHeader.getProtoIdsOff();

        for (int i = 0; i < size; i++) {
            protoIds[i] = new ProtoId();
            protoIds[i].setShortyIdx(byteReadUtil.readBytesToInt(offsetIndex + (i * (DexAddress.OFFSET.INTEGER * 3))));
            protoIds[i].setReturnTypeIdx(byteReadUtil.readBytesToInt(offsetIndex + (i * (DexAddress.OFFSET.INTEGER * 3)) + DexAddress.OFFSET.INTEGER));
            protoIds[i].setParametersOff(byteReadUtil.readBytesToInt(offsetIndex + (i * (DexAddress.OFFSET.INTEGER * 3)) + (DexAddress.OFFSET.INTEGER * 2)));
        }

        for (int i = 0; i < size; i++) {
            ProtoId protoId = protoIds[i];

            int offset = protoId.getParametersOff();

            if (offset == 0) {
                protoId.setTypes(new int[0]);
                continue;
            } else {
                int protoSize = byteReadUtil.readBytesToInt(offset);
                int[] protoIdTypes = new int[protoSize];

                for (int j = 0; j < protoSize; j++) {
                    protoIdTypes[j] = byteReadUtil.readBytesToShort((offset + DexAddress.OFFSET.INTEGER) + (i * DexAddress.OFFSET.SHORT)) & 0xffff;
                }
                protoId.setTypes(protoIdTypes);
            }
        }
    }

    public void loadFieldIds() {
        int size = dexHeader.getFieldIdsSize();
        fieldIds = new FieldId[size];
        int offsetIndex = dexHeader.getFieldIdsOff();

        for (int i = 0; i < size; i++) {
            fieldIds[i] = new FieldId();
            fieldIds[i].setClassIdx(byteReadUtil.readBytesToShort(offsetIndex + (i * DexAddress.OFFSET.DOUBLE)) & 0xffff);
            fieldIds[i].setTypeIdx(byteReadUtil.readBytesToShort((offsetIndex) + (i * DexAddress.OFFSET.DOUBLE) + DexAddress.OFFSET.SHORT) & 0xffff);
            fieldIds[i].setNameIdx(byteReadUtil.readBytesToInt((offsetIndex) + (i * DexAddress.OFFSET.DOUBLE) + DexAddress.OFFSET.INTEGER));
        }
    }

    public void loadMethodIds() {
        int size = dexHeader.getMethodIdsSize();
        methodIds = new MethodId[size];
        int offsetIndex = dexHeader.getMethodIdsOff();

        for (int i = 0; i < size; i++) {
            methodIds[i] = new MethodId();
            methodIds[i].setClassIdx(byteReadUtil.readBytesToShort(offsetIndex + (i * DexAddress.OFFSET.DOUBLE)) & 0xffff);
            methodIds[i].setProtoIdx(byteReadUtil.readBytesToShort((offsetIndex) + (i * DexAddress.OFFSET.DOUBLE) + DexAddress.OFFSET.SHORT) & 0xffff);
            methodIds[i].setNameIdx(byteReadUtil.readBytesToInt((offsetIndex) + (i * DexAddress.OFFSET.DOUBLE) + DexAddress.OFFSET.INTEGER));
        }
    }

    public void loadClassDefs() {
        int size = dexHeader.getClassDefsSize();
        classDefs = new ClassDef[size];
        int offsetIndex = dexHeader.getClassDefsOff();

        for (int i = 0; i < size; i++) {
            classDefs[i] = new ClassDef();
            classDefs[i].setClassIdx(byteReadUtil.readBytesToInt(offsetIndex + (i * (DexAddress.OFFSET.INTEGER * 8))));

//            /* access_flags */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.INTEGER);
//            /* superclass_idx */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.DOUBLE);
//            /* interfaces_off */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.INTEGER * 3);
//            /* source_file_idx */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.DOUBLE * 2);
//            /* annotation_off */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.INTEGER * 5);
//            /* class_data_off */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.DOUBLE * 3);
//            /* static_values_off */
//            byteReadUtil.readBytesToInt(offsetIndex + (i * DexAddress.OFFSET.INTEGER) + DexAddress.OFFSET.INTEGER * 7);
        }
    }

    public void markInternalClasses() {
        for (int i = classDefs.length - 1; i >= 0; i--) {
            typeIds[classDefs[i].getClassIdx()].setInternal(true);
        }

        for (int i = 0; i < typeIds.length; i++) {
            String name = strings[typeIds[i].getDescriptorIdx()];

            if (name.length() == 1) {
                typeIds[i].setInternal(true);
            } else if (name.charAt(0) == '[') {
                typeIds[i].setInternal(true);
            }
        }
    }

    public String getClassNameFromTypeIndex(int idx) {
        return strings[typeIds[idx].getDescriptorIdx()];
    }

    public String[] getArgumentArrayFromProtoIndex(int idx) {
        ProtoId protoId = protoIds[idx];
        String[] result = new String[protoId.getTypes().length];

        for (int i = 0; i < protoId.getTypes().length; i++) {
//            result[i] = strings[typeIds[protoId.getTypes()[i]].getDescriptorIdx()];
            result[i] = "a";
        }

        return result;
    }

    public String getReturnTypeFromProtoIndex(int idx) {
        ProtoId protoId = protoIds[idx];

        return strings[typeIds[protoId.getReturnTypeIdx()].getDescriptorIdx()];
    }

    public MethodInfo[] getMethodInfo() {
        MethodInfo[] resultInfos = new MethodInfo[methodIds.length];

        for (int i = 0; i < methodIds.length; i++) {
            MethodId id = methodIds[i];
            resultInfos[i] = new MethodInfo();

            resultInfos[i].setDeclareClass(getClassNameFromTypeIndex(id.getClassIdx()));
            resultInfos[i].setReturnType(getReturnTypeFromProtoIndex(id.getProtoIdx()));
            resultInfos[i].setMethodName(strings[id.getNameIdx()]);
            resultInfos[i].setArgumentType(getArgumentArrayFromProtoIndex(id.getProtoIdx()));
        }

        return resultInfos;
    }

    public void load() {
        loadDexHeader();

        loadStrings();
        loadTypeIds();
        loadProtoIds();
        loadFieldIds();
        loadMethodIds();
        loadClassDefs();

        markInternalClasses();
    }
}
