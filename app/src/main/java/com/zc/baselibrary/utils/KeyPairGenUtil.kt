package com.zc.baselibrary.utils

import android.os.Build
import androidx.annotation.RequiresApi
import org.apache.commons.codec.binary.Hex
import java.nio.charset.StandardCharsets
import java.security.*

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object KeyPairGenUtil {
    /**
     * 指定加密算法为RSA
     */
    private val ALGORITHM: String = "RSA"

    /**
     * 密钥长度，用来初始化
     */
    private val KEYSIZE: Int = 1024

    /**
     * 指定公钥存放文件
     */
    private val PUBLIC_KEY_FILE: String = "PublicKey"

    /**
     * 指定私钥存放文件
     */
    private val PRIVATE_KEY_FILE: String = "PrivateKey"

    /**
     * 生成密钥对
     *
     * @throws Exception
     */
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun generateKeyPair(): RSAKey? {

        //     /** RSA算法要求有一个可信任的随机数源 */
        //     SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象  */
        val keyPairGenerator: KeyPairGenerator
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            return null;
        }
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象  */
        //     keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(KEYSIZE)
        /** 生成密匙对  */
        val keyPair: KeyPair = keyPairGenerator.generateKeyPair()

        /** 得到公钥  */
        val publicKey: Key = keyPair.public

        /** 得到私钥  */
        val privateKey: Key = keyPair.private
        val publicKeyBytes: ByteArray = publicKey.encoded
        val privateKeyBytes: ByteArray = privateKey.encoded
        val publicKey1: String = ("-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder()
                    .encodeToString(publicKeyBytes) + "\n" + "-----END PUBLIC KEY-----")
        val publicKeyBase64: String? = String(
            Hex.encodeHex(
                publicKey1.toByteArray(
                    StandardCharsets.UTF_8
                )
            )
        )
        val privateKeyBase64: String = Base64.getEncoder().encodeToString(privateKeyBytes)
        return RSAKey(publicKeyBase64, privateKeyBase64)
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun genKeyPair() {
        /** RSA算法要求有一个可信任的随机数源  */
        val secureRandom: SecureRandom = SecureRandom()

        /** 为RSA算法创建一个KeyPairGenerator对象  */
        val keyPairGenerator: KeyPairGenerator = KeyPairGenerator.getInstance(
            ALGORITHM
        )
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象  */
        keyPairGenerator.initialize(KEYSIZE, secureRandom)
        //keyPairGenerator.initialize(KEYSIZE);
        /** 生成密匙对  */
        val keyPair: KeyPair = keyPairGenerator.generateKeyPair()

        /** 得到公钥  */
        val publicKey: Key = keyPair.public

        /** 得到私钥  */
        val privateKey: Key = keyPair.private
        val publicKeyBytes: ByteArray = publicKey.encoded
        val privateKeyBytes: ByteArray = privateKey.encoded
        val publicKeyBase64: String = Base64.getEncoder().encodeToString(publicKeyBytes)
        val privateKeyBase64: String = Base64.getEncoder().encodeToString(privateKeyBytes)
        println("publicKeyBase64.length():" + publicKeyBase64.length)
        println("publicKeyBase64:" + publicKeyBase64)
        println("privateKeyBase64.length():" + privateKeyBase64.length)
        println("privateKeyBase64:" + privateKeyBase64)
    }

    class RSAKey constructor(val publicKey: String?, val privateKey: String?)
}