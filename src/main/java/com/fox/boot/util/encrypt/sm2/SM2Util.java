package com.fox.boot.util.encrypt.sm2;

import com.fox.boot.util.ByteUtil;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @Author:
 * @Description: SM2加解密算法
 * @Date: 2021/8/30 7:05 下午
 * @Modified By:
 */
@Slf4j
public class SM2Util {

    /**
     * SM2加密算法
     * 默认排序"0-C1C2C3"
     * data-hexStringToBytes数据格式
     *
     * @param publicKey 公钥
     * @param data      待加密数据
     * @return 加密数据
     */
    public static String encrypt(String publicKey, String data) {
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        // 设置sm2为加密模式
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = ByteUtil.hexStringToBytes(data);
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            log.error("SM2加密时出现异常:{}", e.getMessage(), e);
        }

        return ByteUtil.bytesToHexString(arrayOfBytes);
    }

    /**
     * SM2解密算法
     * 默认排序"0-C1C2C3"
     * data-hexStringToBytes数据格式
     * <p>
     *
     * @param cipherData 加密密文
     * @param privateKey 私钥
     * @return 解密明文
     */
    public static String decrypt(String cipherData, String privateKey) throws InvalidCipherTextException {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());

        //加密密文
        byte[] cipherDataByte = Hex.decode(cipherData);

        //私钥Hex，还原私钥
        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        //用私钥解密
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKeyParameters);
        String result = ByteUtil.bytesToHex(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
        System.out.println("SM2-解密数据---" + result);

        return result;
    }

    /**
     * SM2加密算法
     * 默认排序"0-C1C2C3"
     * data-getBytes数据格式
     *
     * @param publicKey
     * @param data
     * @return
     */
    public static String encryptM2(String publicKey, String data) {
        // 获取一条SM2曲线参数
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        // 构造ECC算法参数，曲线方程、椭圆曲线G点、大整数N
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());
        //提取公钥点
        ECPoint pukPoint = sm2ECParameters.getCurve().decodePoint(Hex.decode(publicKey));
        // 公钥前面的02或者03表示是压缩公钥，04表示未压缩公钥, 04的时候，可以去掉前面的04
        ECPublicKeyParameters publicKeyParameters = new ECPublicKeyParameters(pukPoint, domainParameters);

        SM2Engine sm2Engine = new SM2Engine();
        // 设置sm2为加密模式
        sm2Engine.init(true, new ParametersWithRandom(publicKeyParameters, new SecureRandom()));

        byte[] arrayOfBytes = null;
        try {
            byte[] in = data.getBytes();
            arrayOfBytes = sm2Engine.processBlock(in, 0, in.length);
        } catch (Exception e) {
            log.error("SM2加密时出现异常:{}", e.getMessage(), e);
        }

        return ByteUtil.bytesToHexString(arrayOfBytes);
    }

    /**
     * SM2解密算法
     * 默认排序"0-C1C2C3"
     * data-getBytes数据格式
     *
     * @param cipherData
     * @param privateKey
     * @return
     * @throws InvalidCipherTextException
     */
    public static String decryptM2(String cipherData, String privateKey) throws InvalidCipherTextException {
        X9ECParameters sm2ECParameters = GMNamedCurves.getByName("sm2p256v1");
        ECDomainParameters domainParameters = new ECDomainParameters(sm2ECParameters.getCurve(), sm2ECParameters.getG(), sm2ECParameters.getN());

        //加密密文
        byte[] cipherDataByte = Hex.decode(cipherData);

        //私钥Hex，还原私钥
        BigInteger privateKeyD = new BigInteger(privateKey, 16);
        ECPrivateKeyParameters privateKeyParameters = new ECPrivateKeyParameters(privateKeyD, domainParameters);

        //用私钥解密
        SM2Engine sm2Engine = new SM2Engine();
        sm2Engine.init(false, privateKeyParameters);
        String result = new String(sm2Engine.processBlock(cipherDataByte, 0, cipherDataByte.length));
        System.out.println("SM2-解密数据---" + result);

        return result;
    }
}
