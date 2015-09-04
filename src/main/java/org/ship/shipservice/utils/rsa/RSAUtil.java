package org.ship.shipservice.utils.rsa;

import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.ship.shipservice.constants.HybConstants;
import org.ship.shipservice.utils.Base64;

public class RSAUtil{   
    public static final String KEY_ALGORITHM = "RSA";   
    public static final String SIGNATURE_ALGORITHM = "MD5withRSA";   
  
    /**
     * 私钥解码
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) {   
        try {
			byte[] keyBytes = Base64.decode(HybConstants.PRIVATEKEY);   
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);   
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
			Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);   
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
			cipher.init(Cipher.DECRYPT_MODE, privateKey);   
			return new String(cipher.doFinal(data.getBytes("ISO_8859-1")),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}   
        return null;
    }   
  
    /**
     * 公钥加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) {   
        try {
			byte[] keyBytes = Base64.decode(HybConstants.PUBLICKEY);   
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);   
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);   
			Key publicKey = keyFactory.generatePublic(x509KeySpec);   
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());   
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);   
			return new String(cipher.doFinal(data.getBytes()),"ISO-8859-1");
		} catch (Exception e) {
			e.printStackTrace();
		}   
        
        return null;
    }   
  
    public static void main(String[] args) throws Exception {
        System.err.println("公钥加密——私钥解密");   
        String inputStr = "雷萌23sd";   
        String encodedData = RSAUtil.encrypt(inputStr);   
        String decodedData = RSAUtil.decrypt(encodedData);   
        System.err.println("加密前: " + inputStr + " -- " + "解密后: " + decodedData);   
	}
}  

