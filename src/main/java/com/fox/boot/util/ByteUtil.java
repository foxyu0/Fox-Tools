package com.fox.boot.util;

public class ByteUtil {

    public static String intToHex(int i) {
        StringBuilder hex = new StringBuilder(String.format("%x", 12));
        int addNumber = 4 - hex.length();

        for (int j = 0; j < addNumber; j++) {
            hex.insert(0, "0");
        }
        return hex.toString();

    }

    /**
     * 将int数值转换为占2个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序
     */
    public static byte[] intToByteArray2(int value) {
        byte[] src = new byte[2];
        src[0] = (byte) ((value >> 8) & 0xFF);
        src[1] = (byte) (value & 0xFF);
        return src;
    }


    public static byte[] intToBytesLe(int intValue) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = (byte) (intValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    public static byte[] intToBytesBe(int intValue) {
        byte[] bytes = new byte[4];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[(bytes.length - i - 1)] = (byte) (intValue >> i * 8 & 0xFF);
        }

        return bytes;
    }

    /**
     * 字符串转换为字节数组
     * <p>
     * stringToBytes("0710BE8716FB"); return: b[0]=0x07;b[1]=0x10;...b[5]=0xfb;
     */
    public static byte[] hexStringToBytes(String string) {
        if (string == null || string.length() == 0 || string.length() % 2 != 0) {
            return null;
        }
        int stringLen = string.length();
        byte[] byteArrayResult = new byte[stringLen / 2];
        StringBuilder sb = new StringBuilder(string);
        String strTemp;
        int i = 0;
        while (i < sb.length() - 1) {
            strTemp = string.substring(i, i + 2);
            byteArrayResult[i / 2] = (byte) Integer.parseInt(strTemp, 16);
            i += 2;
        }
        return byteArrayResult;
    }

    /**
     * 字节数组转为16进制
     *
     * @param bytes 字节数组
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        int len = bytes.length;
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff));
        }
        return buff.toString().toUpperCase();
    }

    public static String printByteString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder buff = new StringBuilder();
        int len = bytes.length;
        for (int j = 0; j < len; j++) {
            if ((bytes[j] & 0xff) < 16) {
                buff.append('0');
            }
            buff.append(Integer.toHexString(bytes[j] & 0xff)).append(" ");
        }
        buff.deleteCharAt(buff.length() - 1);
        return buff.toString().toUpperCase();
    }

    /**
     * 填充80数据，首先在数据块的右边追加一个
     * '80',如果结果数据块是8的倍数，不再进行追加,如果不是,追加0x00到数据块的右边，直到数据块的长度是8的倍数。
     *
     * @param data1 待填充80的数据
     * @return
     */
    public static String padding80(String data1) {
        String data = data1;
        int padlen = 8 - (data.length() / 2) % 8;
        StringBuilder padstr = new StringBuilder();
        for (int i = 0; i < padlen - 1; i++) {
            padstr.append("00");
        }
        data = data + "80" + padstr;

        return data;
    }

    /**
     * 逆向去掉填80
     *
     * @param data
     * @return
     */
    public static String unPadding80(String data) {
        int len = data.length();
        int padlen = (len / 2) % 8;
        if (padlen != 0) {
            return data;
        } else {
            String tempStr = data.substring(data.length() - 16);
            for (int i = 0; i < 8; i++) {
                int start = 2 * i;
                String temp = tempStr.substring(start, start + 2);
                if ("80".equals(temp)) {
                    if (i == 7) {
                        return data.substring(0, len - 2);
                    } else {
                        long x = Long.parseLong(tempStr.substring(start + 2),
                                16);
                        if (x == 0) {
                            return data.substring(0, len - 16 + start);
                        }
                    }
                }
            }
            return data;
        }
    }

    /**
     * 获得引号
     *
     * @return
     */
    public static String getquotes() {
        byte[] quote = new byte[1];
        quote[0] = 0x22;
        return new String(quote);
    }

    static final char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * @return hex string. if input byte array is empty, return null
     * @description more efficient method to convert the byte array to hex string
     * @waring only suppert english character
     * @author power
     */
    public static String bytesToHex(byte[] data) {
        if (data == null || data.length == 0) {
            System.out.println("the String is empty");
            return null;
        } else {
            int len = data.length;
            char[] ac = new char[len << 1];
            int l = 1 << 4;
            int i1 = l - 1;
            for (int i = 0; i < len; ++i) {
                int p = i << 1;
                ac[p + 1] = digits[data[i] & i1];
                ac[p] = digits[data[i] >>> 4 & i1];
            }
            return new String(ac);
        }
    }

    /**
     * @param str hex string
     * @return the byte array.
     * if input string is empty or its length is not a multiple of 2
     * or it has any number which is not hex, return null
     * @description more efficient method to convert hex string to byte array *
     * @waring only suppert english character
     * @author power
     */
    public static byte[] hexToBytes(String str) {
        if ((str.length() & 0x1) == 1) {
            System.out.println("the String is empty or the lengh is odd");
            return null;
        }
        int len = str.length();
        byte[] data = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            char a = str.charAt(i);
            char b = str.charAt(i + 1);
            int c = -1;
            int d = -1;

            if (a <= 0x39 && a >= 0x30) {
                c = (int) a - 0x30;
            } else if (a >= 0x41 && a <= 0x46) {
                c = (int) a - 0x37;
            } else if (a >= 0x61 && a <= 0x66) {
                c = (int) a - 0x57;
            } else {
                return null;
            }

            if (b <= 0x39 && b >= 0x30) {
                d = (int) b - 0x30;
            } else if (b >= 0x41 && b <= 0x46) {
                d = (int) b - 0x37;
            } else if (b >= 0x61 && b <= 0x66) {
                d = (int) b - 0x57;
            } else {
                return null;
            }

            data[i >> 1] = (byte) ((c << 4) + d);
        }
        return data;
    }

    /**
     * @param nLen 1表示1个字节，2标示2个字节~~~
     * @return
     */
    public static String genRandom(Integer nLen) {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < nLen; i++) {
            value.append(String.format("%02X", (int) (Math.random() * (255 - 1 + 1))));
        }
        return value.toString();
    }

    public static Integer bytesToInt(byte[] src) {
        int nRet;
        switch (src.length) {
            case 1:
                nRet = (src[0] & 0xFF);
                break;
            case 2:
                nRet = ((src[0] & 0xFF) << 8) | (src[1] & 0xFF);
                break;
            case 3:
                nRet = ((src[0] & 0xFF) << 16) | ((src[1] & 0xFF) << 8) | (src[2] & 0xFF);
                break;
            case 4:
                nRet = ((src[0] & 0xFF) << 24) | ((src[1] & 0xFF) << 16) | ((src[2] & 0xFF) << 8) | (src[3] & 0xFF);
                break;
            default:
                return -1;
        }
        return nRet;
    }

    public static int bytesToInt(byte[] abyte0, int i, int j) {
        int k = 0;
        for (int l = 0; l < j; l++) {
            k = k << 8 | abyte0[l + i] & 0xff;
        }
        return k;
    }

    public static byte[] IntToBytes(Integer len, Integer buflen) {
        byte[] src = new byte[buflen];
        switch (buflen) {
            case 1:
                src[0] = (byte) (len & 0xFF);
                break;
            case 2:
                src[0] = (byte) ((len >> 8) & 0xFF);
                src[1] = (byte) (len & 0xFF);
                break;
            case 3:
                src[0] = (byte) ((len >> 16) & 0xFF);
                src[1] = (byte) ((len >> 8) & 0xFF);
                src[2] = (byte) (len & 0xFF);
                break;
            case 4:
                src[0] = (byte) ((len >> 24) & 0xFF);
                src[1] = (byte) ((len >> 16) & 0xFF);
                src[2] = (byte) ((len >> 8) & 0xFF);
                src[3] = (byte) (len & 0xFF);
                break;
            default:
                return src;
        }
        return src;
    }


    public static String LV(String s) {
        int length = s.length() / 2;
        return String.format("%02X%s", length, s);
    }

    public static String LV2(String s) {
        int length = s.length() / 2;
        return String.format("%04X%s", length, s);
    }


    /**
     * @param s1
     * @param s2
     * @return s1 xor s2, the length of s1 and s2 must be even and equal,
     * otherwise return null;
     * @description
     */
    public static String strXor(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return null;
        }
        byte[] bs1 = ByteUtil.hexToBytes(s1);
        byte[] bs2 = ByteUtil.hexToBytes(s2);
        for (int i = 0; i < bs2.length; ++i) {
            bs2[i] = (byte) (bs1[i] ^ bs2[i]);
        }
        return ByteUtil.bytesToHex(bs2);
    }

    public static String strNot(String s) {
        byte[] bs = ByteUtil.hexToBytes(s);
        for (int i = 0; i < bs.length; i++) {
            bs[i] = (byte) (bs[i] ^ 0xFF);
        }
        return ByteUtil.bytesToHex(bs);
    }


    public static String dataPadding(String input, boolean forcePadding) {
        StringBuilder sbInput = new StringBuilder(input);
        if (forcePadding) {
            sbInput.append("80");
        }
        while (sbInput.length() % 16 != 0) {
            sbInput.append("0");
        }
        return sbInput.toString();
    }

    public static String dataPadding32(String input) {
        StringBuilder sbInput = new StringBuilder(input);
        while (sbInput.length() < 32) {
            sbInput.append("0");
        }
        return sbInput.toString();
    }

    public static byte[] pkcs7Pad(byte[] source, int blockSize) {
        int sourceLength = source.length;
        int padDataLen = blockSize - (sourceLength % blockSize);
        int afterPadLen = sourceLength + padDataLen;
        byte[] paddingResult = new byte[afterPadLen];
        System.arraycopy(source, 0, paddingResult, 0, sourceLength);
        for (int i = sourceLength; i < afterPadLen; i++) {
            paddingResult[i] = (byte) padDataLen;
        }
        return paddingResult;
    }

    public static byte[] pkcs7Unpad(byte[] data) {
        int lastValue = data[data.length - 1];
        byte[] unpad = new byte[data.length - lastValue];
        System.arraycopy(data, 0, unpad, 0, unpad.length);
        return unpad;
    }

    public static void main(String[] args) {
        String data = "000102030405";
        String encrty = pkcs5Padding(data);
        System.out.println(encrty);
        System.out.println(unPkcs5Padding(encrty));
    }

    public static String fileDataPadding(String datum) {
        int length = datum.length() / 2;
        datum = "0000" + String.format("%2X", length) + datum;
        return dataPadding(datum, true);
    }

    //对输入数据 MSG，在 MSG 的右端添加 N-(||MSG|| % N)个 PAD 字符，每个字符取值为 N-(||MSG|| % N);也就是说，对 MSG，最右端数据块，
// 缺 m 个字节则补充 m 个数值 m，最少补 1 个 0x01，最多补 16 个x10(当 MSG 长度为分组长度 N 字节的整数倍时)。
    public static String pkcs5Padding(String datum) {
        int num = 8;
        int n = num - datum.length() / 2 % num;
        StringBuilder sbInput = new StringBuilder(datum);
        n = n == 0 ? num : n;
        for (int i = 0; i < n; i++) {
            sbInput.append(String.format("%02X", n));
        }
        return sbInput.toString();
    }

    public static String unPkcs5Padding(String datum) {
        int num = 8;
        int length = datum.length();
        if (length % num == 0) {
            String data = datum.substring(length - 16, length);
            int length1 = data.length();
            String temp = data.substring(length1 - 2, length1);
            int n = Integer.parseInt(temp, 16);
            datum = datum.substring(0, length - 2 * n);
            return datum.toString();
        }
        return null;
    }

}
