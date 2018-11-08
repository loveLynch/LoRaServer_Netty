package com.lynch.util.aes;


import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class AesCmac {
  private static final byte CONSTANT = (byte) 0x87;
  private static final int BLOCK_SIZE = 16;

  private int macLength;
  public Cipher aesCipher;

  private byte[] buffer;
  private int bufferCount;

  public byte[] k1;
  public byte[] k2;

  public AesCmac() throws NoSuchAlgorithmException {
      this(BLOCK_SIZE);
  }

  public AesCmac(int length) throws NoSuchAlgorithmException {
      if (length > BLOCK_SIZE) {
          throw new NoSuchAlgorithmException("AES CMAC maximum length is " + BLOCK_SIZE);
      }

      try {
          macLength = length;
          aesCipher = Cipher.getInstance("AES/CBC/NOPADDING");
          buffer = new byte[BLOCK_SIZE];
      } catch (NoSuchPaddingException nspe) {
          nspe.printStackTrace();
      }
  }

  private byte[] doubleSubKey(byte[] k) {
      byte[] ret = new byte[k.length];

      boolean firstBitSet = ((k[0]&0x80) != 0);
      for (int i=0; i<k.length; i++) {
          ret[i] = (byte) (k[i] << 1);
          if (i+1 < k.length && ((k[i+1]&0x80) != 0)) {
              ret[i] |= 0x01;
          }
      }
      if (firstBitSet) {
          ret[ret.length-1] ^= CONSTANT;
      }
      return ret;
  }

  public void init(Key key) throws InvalidKeyException, Exception {
      if (!(key instanceof SecretKeySpec)) {
          throw new InvalidKeyException("Key is not of required type SecretKey.");
      }
      if (!((SecretKeySpec)key).getAlgorithm().equals("AES")) {
          throw new InvalidKeyException("Key is not an AES key.");
      }
      byte[] iv = DatatypeConverter.parseHexBinary("00000000000000000000000000000000");
      IvParameterSpec  ivparam = new IvParameterSpec(iv);
      aesCipher = Cipher.getInstance("AES/CBC/Nopadding");
      aesCipher.init(Cipher.ENCRYPT_MODE, key, ivparam);

      // First calculate k0 from zero bytes
      byte[] k0 = new byte[BLOCK_SIZE];
      try {
          aesCipher.update(k0, 0, k0.length, k0, 0);
      } catch (ShortBufferException sbe) {}

      // Calculate values for k1 and k2
      k1 = doubleSubKey(k0);
      k2 = doubleSubKey(k1);

      aesCipher.init(Cipher.ENCRYPT_MODE, key);
      bufferCount = 0;
  }
}
