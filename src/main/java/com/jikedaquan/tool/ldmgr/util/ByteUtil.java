package com.jikedaquan.tool.ldmgr.util;

public class ByteUtil {
    public static final int SIZE_KB = 1024;
    public static final int SIZE_MB = 1024^2;
    public static final int SIZE_GB = 1024^3;
    public static final int SIZE_TB = 1024^4;

    public static String getByteToString(long size){
        // 10240%1024=10kb 1025 1024
        double resultSize = size;
        int count = 1;
        //整除为1或小于1024时
        for (; resultSize/SIZE_KB>1&&resultSize/SIZE_KB<1024; count++) {
            resultSize=resultSize/SIZE_KB;
            System.out.println(resultSize);

        }
        String result = size+"";
        return result;
    }

    public static void main(String[] args) {
        ByteUtil.getByteToString(2028);
    }
}
