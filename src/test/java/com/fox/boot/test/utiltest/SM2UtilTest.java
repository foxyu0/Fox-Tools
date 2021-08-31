package com.fox.boot.test.utiltest;

import com.fox.boot.util.encrypt.sm2.SM2Util;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.jupiter.api.Test;

/**
 * @Author:
 * @Description:
 * @Date: 2021/8/30 7:21 下午
 * @Modified By:
 */
@Slf4j
public class SM2UtilTest {


    private static final String publicKey = "04587E01B0996DDDE022502EE9A6995E3E34A4F059F75BE3AF9D6A37AE54590D6ADB332772C3BFEDDF2F1278B8428C9083E4E475551A73199E575DC1CFDCE773C6";
    private static final String privateKey = "2D8C449B34DB68398D7A54F16A94AFF0F94A5B0A762DCA7198984DDBADDD24F2";


    /**
     * SM2加密-默认排序-hexStringToBytes数据格式
     * 1.2.0.1
     */
    @Test
    public void sm2EncryptTest() {
        String data = "20210801113210001474224305706786";
        String encData = SM2Util.encrypt(publicKey, data);
        System.out.println("SM2-加密数据---" + encData);
    }

    /**
     * SM2解密-默认排序-hexStringToBytes数据格式
     * 1.2.0.2
     */
    @Test
    public void sm2DecryptTest() {
        String cipherData = "04780519E6CAD8333F01FC43170D6329E6CEE91CEDD9AAAADC81056DDC2674EC2EEA66532BAA9FD2A0D2601E6D21748BF7F2D47B45F2E7A78BE4ECAACAE69C1E2F7008369A528BF7AF0FAA0EDA65B4F508076DA18AE623480ADD0C725D3BFCE5B56E21854119A1C59362BBD0550EC3143A";

        try {
            String result = SM2Util.decrypt(cipherData, privateKey);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
    }

    /**
     * SM2加密-默认排序-bytes数据格式
     * 1.2.1.1
     */
    @Test
    public void sm2EncryptM2Test() {
        String data = "20210801113210001474224305706786";
        String encData = SM2Util.encryptM2(publicKey, data);
        System.out.println("SM2-加密数据---" + encData);
    }

    /**
     * SM2解密-默认排序-bytes数据格式
     * 1.2.1.2
     */
    @Test
    public void sm2DecryptM2Test() {
        String cipherData = "04F62105BF144585AE742C63E812C346292D8C250D3A423CDEFE92F6486A0B89D7C096210679EF00533BB4B14F8985BB5BB56110579C98C788F6B355B837807A986B58CFD70D727EF612D2BD9DBE793DE543335E654CD4B7DCC6DFE2CDB78483162684A02C89E478E408D2463E1B24E48BC07D0D134E0285A8ABA27A0BDA7C98C8";

        try {
            String result = SM2Util.decryptM2(cipherData, privateKey);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
        }
    }

}
