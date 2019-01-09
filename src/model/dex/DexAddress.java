package model.dex;

public class DexAddress {
    public static final byte[] DEX_FILE_MAGIC = {
            0x64, 0x65, 0x78, 0x0a, 0x30, 0x33, 0x36, 0x00};
    public static final byte[] DEX_FILE_MAGIC_HONEY_COM = {
            0x64, 0x65, 0x78, 0x0a, 0x30, 0x33, 0x35, 0x00};

    public static final int LITTLE_ENDIAN = 0x12345678;
    public static final int BIG_ENDIAN = 0x78563412;

    public class STARTADDRESS {
        public static final int MAGIC = 0x00;
        public static final int CHECKSUM = 0x08;
        public static final int SA1 = 0x0C;
        public static final int FILE_SIZE = 0x20;
        public static final int HEADER_SIZE = 0x24;
        public static final int ENDIAN_TAG = 0x28;
        public static final int LINK_SIZE = 0x2C;
        public static final int LINK_OFF = 0x30;
        public static final int MAP_OFF = 0x34;
        public static final int STRING_IDS_SIZE = 0x38;
        public static final int STRING_IDS_OFF = 0x3C;
        public static final int TYPE_IDS_SIZE = 0x40;
        public static final int TYPE_IDS_OFF = 0x44;
        public static final int PROTO_IDS_SIZE = 0x48;
        public static final int PROTO_IDS_OFF = 0x4C;
        public static final int FIELD_IDS_SIZE = 0x50;
        public static final int FIELD_IDS_OFF = 0x54;
        public static final int METHOD_IDS_SIZE = 0x58;
        public static final int METHOD_IDS_OFF = 0x5C;
        public static final int CLASS_DEFS_SIZE = 0x60;
        public static final int CLASS_DEFS_OFF = 0x64;
        public static final int DATA_SIZE = 0x68;
        public static final int DATA_OFF = 0x6C;
    }

    public class OFFSET {
        public static final int MAGIC = 0x08;
        public static final int SA1 = 0x14;
        public static final int INTEGER = 0x04;
        public static final int SHORT = 0x02;
        public static final int DOUBLE = 0x08;
    }
}
