package com.hh.hlibrary.utils

import org.apache.commons.codec.binary.Hex

/**
 *
 * @author zhangchao
 * @date  2020年12月3日
 */
object KeyPairGenUtil {
    /**
     * 指定加密算法为RSA
     */
    private val ALGORITHM: String? = "RSA"

    /**
     * 密钥长度，用来初始化
     */
    private val KEYSIZE: Int = 1024

    /**
     * 指定公钥存放文件
     */
    private val PUBLIC_KEY_FILE: String? = "PublicKey"

    /**
     * 指定私钥存放文件
     */
    private val PRIVATE_KEY_FILE: String? = "PrivateKey"

    /**
     * 生成密钥对
     *
     * @throws Exception
     */
    fun generateKeyPair(): RSAKey? {

        //     /** RSA算法要求有一个可信任的随机数源 */
        //     SecureRandom secureRandom = new SecureRandom();
        /** 为RSA算法创建一个KeyPairGenerator对象  */
        var keyPairGenerator: KeyPairGenerator? = null
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM)
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象  */
        //     keyPairGenerator.initialize(KEYSIZE, secureRandom);
        keyPairGenerator.initialize(KEYSIZE)
        /** 生成密匙对  */
        val keyPair: KeyPair? = keyPairGenerator.generateKeyPair()
        /** 得到公钥  */
        val publicKey: Key? = keyPair.getPublic()
        /** 得到私钥  */
        val privateKey: Key? = keyPair.getPrivate()
        val publicKeyBytes: ByteArray? = publicKey.getEncoded()
        val privateKeyBytes: ByteArray? = privateKey.getEncoded()
        val publicKey1: String? = ("-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKeyBytes)
                    .toString() + "\n".toString() + "-----END PUBLIC KEY-----")
        val publicKeyBase64: String? =
            String(Hex.encodeHex(publicKey1.getBytes(StandardCharsets.UTF_8)))
        val privateKeyBase64: String? = Base64.getEncoder().encodeToString(privateKeyBytes)
        return RSAKey(publicKeyBase64, privateKeyBase64)
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun genKeyPair() {
        /** RSA算法要求有一个可信任的随机数源  */
        val secureRandom: SecureRandom? = SecureRandom()
        /** 为RSA算法创建一个KeyPairGenerator对象  */
        val keyPairGenerator: KeyPairGenerator? = KeyPairGenerator.getInstance(
            ALGORITHM
        )
        /** 利用上面的随机数据源初始化这个KeyPairGenerator对象  */
        keyPairGenerator.initialize(KEYSIZE, secureRandom)
        //keyPairGenerator.initialize(KEYSIZE);
        /** 生成密匙对  */
        val keyPair: KeyPair? = keyPairGenerator.generateKeyPair()
        /** 得到公钥  */
        val publicKey: Key? = keyPair.getPublic()
        /** 得到私钥  */
        val privateKey: Key? = keyPair.getPrivate()
        val publicKeyBytes: ByteArray? = publicKey.getEncoded()
        val privateKeyBytes: ByteArray? = privateKey.getEncoded()
        val publicKeyBase64: String? = Base64.getEncoder().encodeToString(publicKeyBytes)
        val privateKeyBase64: String? = Base64.getEncoder().encodeToString(privateKeyBytes)
        System.out.println("publicKeyBase64.length():" + publicKeyBase64.length())
        System.out.println("publicKeyBase64:" + publicKeyBase64)
        System.out.println("privateKeyBase64.length():" + privateKeyBase64.length())
        System.out.println("privateKeyBase64:" + privateKeyBase64)
    }

    class RSAKey constructor(val publicKey: String?, val privateKey: String?)
}