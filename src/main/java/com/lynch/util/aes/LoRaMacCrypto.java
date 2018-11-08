package com.lynch.util.aes;


import com.lynch.util.base64.base64__;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Arrays;

public class LoRaMacCrypto {
	 /**
	  * 终端具有的加密函数, 可以进行仿照
	  * void LoRaMacComputeMic( const uint8_t *buffer, uint16_t size, const uint8_t *key, 
	  * uint32_t address, uint8_t dir, uint32_t sequenceCounter, uint32_t *mic );
	  * 
	  * void LoRaMacPayloadEncrypt( const uint8_t *buffer, uint16_t size, const uint8_t *key, 
	  * uint32_t address, uint8_t dir, uint32_t sequenceCounter, uint8_t *encBuffer );
	  * 
	  * void LoRaMacPayloadDecrypt( const uint8_t *buffer, uint16_t size, const uint8_t *key, 
	  * uint32_t address, uint8_t dir, uint32_t sequenceCounter, uint8_t *decBuffer );
	  * 
	  * void LoRaMacJoinComputeMic( const uint8_t *buffer, uint16_t size, const uint8_t *key, uint32_t *mic );
	  * 
	  * void LoRaMacJoinDecrypt( const uint8_t *buffer, uint16_t size, const uint8_t *key, uint8_t *decBuffer );
	  * 
	  * void LoRaMacJoinComputeSKeys( const uint8_t *key, const uint8_t *appNonce, 
	  * uint16_t devNonce, uint8_t *nwkSKey, uint8_t *appSKey );
	  * 
	  */
	
	public static final byte[] APPSKEY0 = {
			0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00, 
			0x00, 0x00, 0x00, 0x00,
			0x00, 0x00, 0x00, 0x00};
	public static final byte[] APPKEY = {
			0x2B, 0x7e, 0x15, 0x16,
			0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6, 
			(byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88,
			0x09, (byte) 0xcf, 0x4f, 0x3c};
	/**
	 * 
	 */
	public static final String KEY_ALGORITHM = "AES";
	
	/**
	 * 模式
	 */
	public static final String CIPHER_ALGORITHM_ECBNopadding = "AES/ECB/Nopadding";
	public static final String CIPHER_ALGORITHM_CBCNopadding = "AES/CBC/Nopadding";
	
	
	/**
	 * 对含有 FRMPayload 的数据帧进行加密, 
	 * 使用 ECB/NoPadding 模式, 
	 * 加密秘钥及需要加密的内容均为 16B, 
	 * 加密前后等长, 即为 16B
	 * 
	 * @param frmPayload
	 * @param size : frmPayload 的大小
	 * @param key: 加密的秘钥 AppSKey/NwkSKey
	 * @param address: DevAddr
	 * @param dir: up:0 down:1
	 * @param sequenceCounter: fcnt 传进来的参数是 2 字节的, 需将其转为 4 字节的
	 * @return
	 */
	public static byte[] LoRaMacPayloadEncrypt(byte[] frmPayload, int size, byte[] key, byte[] address, byte dir, byte[] sequenceCounter){
		byte[] aBlock = {
				0x01, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		byte[] sBlock = {
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 
				0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
		aBlock[5] = dir;
		System.arraycopy(address, 0, aBlock, 6, 4);
		System.arraycopy(sequenceCounter, 0, aBlock, 10, 4);
		
		int ctr = 1, bufferIndex = 0;
		while(size >= 16){
			aBlock[15] = (byte) ( ( ctr ) & 0xFF );
	        ctr++;
	        sBlock = LoRaMacCrypto.encrypt_ECB(aBlock, key);
	        for( int i = 0; i < 16; i++ ) {
	            frmPayload[bufferIndex + i] = (byte) (frmPayload[bufferIndex + i] ^ sBlock[i]);
	        }
	        size -= 16;
	        bufferIndex += 16;
		}
		if( size > 0 ) {
	        aBlock[15] = (byte) ( ( ctr ) & 0xFF );
	        sBlock = LoRaMacCrypto.encrypt_ECB(aBlock, key);
	        for(int i = 0; i < size; i++ ) {
	        	frmPayload[bufferIndex + i] = (byte) (frmPayload[bufferIndex + i] ^ sBlock[i]);
	        }
	    }
		return frmPayload;
		
	}

	/**
	 * 对含有 FRMPayload 的数据帧进行解密, 
	 * 使用 ECB/NoPadding 模式, 
	 * 解密秘钥及需要加密的内容均为 16B, 
	 * 解密前后等长, 即为 16B
	 * 
	 * @param frmPayload
	 * @param size : frmPayload 的大小
	 * @param key: 加密的秘钥 AppSKey/NwkSKey
	 * @param address: DevAddr
	 * @param dir: up:0 down:1
	 * @param sequenceCounter: fcnt 传进来的参数是 2 字节的, 需将其转为 4 字节的
	 * @return
	 */
	public static byte[] LoRaMacPayloadDecrypt(byte[] frmPayload, int size, byte[] key, byte[] address, byte dir, byte[] sequenceCounter){
		return LoRaMacCrypto.LoRaMacPayloadEncrypt(frmPayload, size, key, address, dir, sequenceCounter);
	}
	
	/**
	 * 
	 * @param buffer: 用于计算 MIC 的协议字段数据(不包含 B0)
	 * @param size: 用于计算 MIC 的长度(不包含 B0)
	 * @param key: 秘钥 AppSKey/NwkSKey
	 * @param address: DevAddr
	 * @param dir: up 0  down 1
	 * @param sequenceCounter: fcnt, 传进来的参数是 2 字节的, 需将其转为 4 字节的
	 * @return 4B 的 mic
	 * @throws Exception 
	 */
	public static byte[] LoRaMacComputeMic(byte[] buffer, int size, byte[] key, byte[] address, byte dir, byte[] sequenceCounter){
		
		byte[] data = new byte[buffer.length + 16];		// 用于计算 MIC 的所有字段
		byte[] B0 = {
				0x49,0x00,0x00,0x00,0x00,0x00,0x00,0x00,
				0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		
		// 准备用于计算 MIC 的字段, 即 data[]
		B0[5] = dir;
		System.arraycopy(address, 0, B0, 6, 4);
		// TODO 需要对考虑 sequenceCounter 的字节数
		
		System.arraycopy(sequenceCounter, 0, B0, 10, 2);
		B0[15] = (byte) (size & 0xff);
		System.arraycopy(B0, 0, data, 0, 16);
		System.arraycopy(buffer, 0, data, 16, size);
		
		try {
			return ComputeMic(data, data.length, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 
	 * @param buffer: 用于计算 MIC 的数据
	 * @param size: 用于计算 MIC 的数据长度
	 * @param key: 秘钥 AppSKey/NwkSKey
	 * @return 4B 的 MIC
	 * @throws Exception 
	 */
	public static byte[] LoRaMacJoinAcceptComputeMic(byte[] buffer, int size, byte[] key){
		try {
			return ComputeMic(buffer, size, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] LoRaMacJionRequestComputeMic(byte[] buffer, int size, byte[] key){
		try {
			return ComputeMic(buffer, size, key);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 对含有 Accept 帧进行加密, 使用解密算法进行加密, 秘钥 AppKey 
	 * 使用 ECB/NoPadding 模式, 
	 * 加密秘钥及需要加密的内容均为 16B, 
	 * 加密前后等长, 即为 16B
	 *  
	 * @param buffer: 需要加密的数据
	 * @param size: 加密数据长度
	 * @param key: 秘钥 AppSKey
	 * @return 加密完成后的字节数组
	 */
	public static byte[] LoRaMacAcceptEncrypt(byte[] buffer, int size, byte[] key){
		return LoRaMacCrypto.decrypt_ECB(buffer, key);
	}
	
	/**
	 * 加密模式：ECB/Nopadding模式
	 * @param key: AppKey
	 * @param appNonce
	 * @param devNonce
	 * @param netId
	 * @return 16B 的 NwkSKey
	 */
	public static byte[] LoRaMacJoinComputeNwkSKey(byte[] key, byte[] appNonce, byte[] devNonce, byte[] netId){
		byte[] nwkSKey = new byte[16];
		byte[] content = new byte[16];
		content[0] = 0x01;
		System.arraycopy(appNonce, 0, content, 1, 3);
		System.arraycopy(netId, 0, content, 4, 3);
		System.arraycopy(devNonce, 0, content, 7, 2);
		nwkSKey = encrypt_ECB(content, APPKEY);
		return nwkSKey;
	}
	
	/**
	 * 加密模式：ECB/Nopadding模式
	 * @param key: AppKey
	 * @param appNonce
	 * @param devNonce
	 * @param netId
	 * @return 16B 的 AppSKey
	 */
	public static byte[] LoRaMacJoinComputeAppSKey(byte[] key, byte[] appNonce, byte[] devNonce, byte[] netId){
		byte[] appSKey = new byte[16];
		byte[] content = new byte[16];
		content[0] = 0x02;
		System.arraycopy(appNonce, 0, content, 1, 3);
		System.arraycopy(netId, 0, content, 4, 3);
		System.arraycopy(devNonce, 0, content, 7, 2);
		appSKey = encrypt_ECB(content, APPKEY);
		
		return appSKey;
	}

    /**
     * @param key
     * @param beacon_time
     * @param address
     * @return
     */
 public static byte[] LoRaMacBeaconComputeOffset(byte[] key, byte [] beacon_time, byte[] address){
     byte[] Rand = new byte[16];
     byte[] content = new byte[16];
     System.arraycopy(beacon_time, 0, content, 0, 4);
     System.arraycopy(address, 0, content, 4, 4);
     Rand = encrypt_ECB(content, APPSKEY0);
     return Rand;

 }

	/**
	 * 
	 * @param data: 用于计算 MIC 的数据字段
	 * @param size: 数据字段长度
	 * @param key: 秘钥
	 * @return: 4 字节的 MIC
	 * @throws Exception 
	 */
	public static byte[] ComputeMic(byte[] data, int size, byte[] key) throws Exception {
		byte[] mic = new byte[4];
		AesCmac aes = new AesCmac();
		
		SecretKeySpec sKeySpec = new SecretKeySpec(key,"AES");
		aes.init(sKeySpec);	
		byte[] iv = DatatypeConverter.parseHexBinary("00000000000000000000000000000000");
		byte[] key1 = aes.k1;	
		byte[] key2 = aes.k2;	
		
		try {
			int  nBlocks = size / 16;
			int  lastBlen = size % 16;
			byte[] lastState;
			byte[] lastBData;
			
			boolean padding = false;
			if(lastBlen > 0){
				padding = true;
				nBlocks++;
			}

			if(nBlocks > 1) {
				byte[] cbcdata =  Arrays.copyOf(data, (nBlocks-1)*16);
				
				SecretKeySpec aesKey = new SecretKeySpec(key,"AES");
				IvParameterSpec ivparam = new IvParameterSpec(iv);
				Cipher cbc = Cipher.getInstance(CIPHER_ALGORITHM_CBCNopadding);
				cbc.init(Cipher.ENCRYPT_MODE,aesKey,ivparam);
				byte[] cbcCt = cbc.doFinal(cbcdata);
				lastState = Arrays.copyOfRange(cbcCt,(nBlocks-2)*16, (nBlocks-1)*16);
				lastBData = Arrays.copyOfRange(data,(nBlocks-1)*16 ,(nBlocks)*16);
			} else {
				if(size == 0) {
					lastState = DatatypeConverter.parseHexBinary("00000000000000000000000000000000");
					lastBData = DatatypeConverter.parseHexBinary("00000000000000000000000000000000");
					padding=true;
				} else {
					lastState = DatatypeConverter.parseHexBinary("00000000000000000000000000000000");
					lastBData = Arrays.copyOfRange(data,(nBlocks-1)*16 ,(nBlocks)*16);
				}
			}
			if(lastBlen != 0 || padding) {
				lastBData[lastBlen] = (byte) 128;
			}
			if(padding){
				for (int i = 0; i < 16; i++) {
					lastBData[i] = (byte) ((lastBData[i]) ^ ( key2[i]));
				}
			}else{
				for (int i = 0; i < 16; i++) {
					lastBData[i] = (byte) ((lastBData[i]) ^ ( key1[i]));
				}
			}

			SecretKeySpec aesKey= new SecretKeySpec(key,"AES");
			IvParameterSpec ivparam = new IvParameterSpec(lastState);
			Cipher aesMac = Cipher.getInstance(CIPHER_ALGORITHM_CBCNopadding);
			aesMac.init(Cipher.ENCRYPT_MODE,aesKey, ivparam);
			mic = Arrays.copyOfRange(aesMac.doFinal(lastBData), 0, 4);
			return mic;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt_ECB(byte[] data, byte[] key) {
		Key k = toKey(key);		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECBNopadding);
			cipher.init(Cipher.DECRYPT_MODE, k);
			return cipher.doFinal(data);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
		
	}
	
	/**
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] decrypt_CBC(byte[] data, byte[] key) {
		String ivParameter = "0123456123abcdef";
		Key k = toKey(key);		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBCNopadding);
			// CBC
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, k, iv);
			return cipher.doFinal(data);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
		
	}
	
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt_ECB(byte[] data, byte[] key){
		Key k = toKey(key);		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECBNopadding);
			cipher.init(Cipher.ENCRYPT_MODE, k);
			return cipher.doFinal(data);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	 * 
	 * @param data
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt_CBC(byte[] data, byte[] key){
		String ivParameter = "0123456789abcdef";
		Key k = toKey(key);		
		Cipher cipher;
		try {
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBCNopadding);
			// CBC
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, k, iv);
			return cipher.doFinal(data);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; 
	}
	
	/**
	 * 
	 * @return
	 */
	public static byte[] initKey(){
		KeyGenerator kg;
		try {
			kg = KeyGenerator.getInstance(KEY_ALGORITHM);
			kg.init(128);
			SecretKey secretKey = kg.generateKey();
			return secretKey.getEncoded();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	private static Key toKey(byte[] key){
		SecretKey secretKey = new SecretKeySpec(key, KEY_ALGORITHM);
		return secretKey;
	}

	
	/**
	 * 
	 * @param key
	 * @param content
	 * @return
	 */
	public static byte[] encrypt_Test(String key, byte[] content){
		int cnt = 0;
		byte[] outputbyte = new byte[(content.length / 16 + 1) * 16];
		base64__.myprintHex(content);
		byte[] byte16 = new byte[16];
		System.out.println("aes content length " + content.length);
		for(int i = 0; i < content.length / 16 + 1; i++){
			System.out.println("aes----------");
			System.arraycopy(content, i * 16, byte16, 0, 13);
			base64__.myprintHex(byte16);
			byte16 = LoRaMacCrypto.encrypt_ECB(byte16, key.getBytes());
			System.arraycopy(byte16, 0, outputbyte, i * 16, 16);
		}
		base64__.myprintHex(outputbyte);
		System.out.println("aes success!");
		return outputbyte;
	}
	
	/**
	 * 
	 * @param inputData
	 */
	public static void myprintHex(byte[] inputData){
		for (int i = 0; i < inputData.length; i++) {   
            String hex = Integer.toHexString(inputData[i] & 0xFF);   
            if (hex.length() == 1) {   
                    hex = '0' + hex;   
            }   
            System.out.print("0x" + hex + " "); 
            if( (i + 1) % 16 == 0)
            	System.out.println();
		}   
		System.out.println();
	}
	
	public static void main(String[] args) throws Exception{	
//		byte[] aaa = {0x11,0x22,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x10,0x11,0x12,0x13,0x14,0x15,0x16};
//		byte[] AppSKey = {
//				0x2B, 0x7e, 0x15, 0x16,
//				0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6,
//				(byte) 0xab, (byte) 0xf7, 0x15, (byte) 0x88,
//				0x09, (byte) 0xcf, 0x4f, 0x3c};
//		byte[] mac = {
//				(byte) 0x80,						/*mhdr*/
//				(byte) 0x8D,(byte) 0xC0,0x79,0x00,	/*devaddr*/
//				0x00,								/*fctrl*/
//				0x01,0x00,							/*fcnt*/
//				0x03,								/*fport*/
//				0x7F,(byte) 0x90,					/*frm*/
//			//	(byte) 0xB0,0x78,0x75,(byte) 0x8B	/*mic*/
//				};
//
//		byte[] dd = {0x60,
//				(byte)0xca, 0x67, 0x73, 0x02,
//				(byte)0x80,
//				0x00, 0x00,
//				0x03,
//				(byte)0xf9, (byte)0xd5, (byte)0xb1, 0x65};
//		byte[] add = {(byte)0xca,0x67,0x73, 0x02};
//		byte[] sequence = {0x00 ,0x00};
//		byte[] nk = {0x01, 0x20, 0x29, 0x02, 0x5d, (byte)0xbe, 0x2f ,
//				(byte)0xc5,(byte)0xe2, (byte)0xe6 ,(byte)0xa6 ,(byte)0xe7,
//				(byte)0xbb,(byte) 0xc3, (byte)0xc0 ,0x67};
//		System.out.println("data trans");
//		base64__.myprintHex(LoRaMacCrypto.LoRaMacComputeMic(dd, 13, nk	, add, (byte) 0x01, sequence));
//		System.out.println("join");
//		base64__.myprintHex(LoRaMacCrypto.LoRaMacJoinAcceptComputeMic(dd, dd.length, aaa));
//
//
		
//
//		byte[] address = {(byte) 0x8d,(byte) 0xc0,0x79,0x00};
//		byte dir = 0x00;
//		byte[] sequenceCounter = {0x01,0x00};
//		base64__.myprintHex(LoRaMacCrypto.LoRaMacComputeMic(mac, mac.length, AppSKey, address, dir, sequenceCounter));

		
		int accept = 1;
		
		if(accept == 1) {
			// 数据帧的加解密检验, 上行
			byte[] frmdata = {(byte) 0x0f, 0x0f , (byte) 0x0f, (byte) 0x0f};
			byte[] appsk = {0x73, 0x5f, 0x4f, (byte) 0xf8, (byte) 0xe2,
					(byte) 0xb5, 0x53, (byte) 0xc8, (byte) 0xbf, (byte) 0x87, 0x5b, 0x37 , (byte) 0x97, 0x09, 0x7f , (byte) 0x85};
			byte[] ad = {(byte) 0xca, 0x67, 0x73, 0x02};
			byte[] sq = {0x00, 0x00,0x00,0x00};
			byte dr = 0x01;
			byte[] outputaccept = LoRaMacCrypto.LoRaMacPayloadEncrypt(frmdata, frmdata.length, appsk, ad, dr, sq);
			System.out.println("数据帧：AppSKey");
			LoRaMacCrypto.myprintHex(outputaccept);
			System.out.println("accept：AppSKey");
			LoRaMacCrypto.myprintHex(LoRaMacCrypto.LoRaMacPayloadDecrypt(outputaccept, outputaccept.length, appsk, ad, dr, sq));
		} 
		if(accept == 2){
//			// 入网过程的加解密检验
//			byte[] frmdata = {0x01,0x02,0x03,0x00,
//			0x00,0x65,(byte) 0xca,(byte) 0x67,
//			0x73,0x02,0x12,0x01,
//			(byte) 0xb5,(byte) 0x4d,(byte) 0x8b,(byte) 0x52};
//			byte[] outputaccept = LoRaMacCrypto.LoRaMacAcceptEncrypt(frmdata, frmdata.length, AppSKey);
//			System.out.println("数据帧：AppSKey");
//			LoRaMacCrypto.myprintHex(outputaccept);
//			System.out.println("accept：AppSKey");
//			LoRaMacCrypto.myprintHex(LoRaMacCrypto.encrypt_ECB(outputaccept, AppSKey));
		}
		
	}

	
	
} 