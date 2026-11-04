package org.example.cipherAlgorithms;

import org.example.BlockCipher;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Aes implements BlockCipher {
    @Override
    public byte[] processBlock(byte[] block, byte[] key, boolean encrypt) {
        try {
            // ecb, nopadding, ezt megoldja az engine
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

            int mode = encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
            cipher.init(mode, keySpec);

            return cipher.doFinal(block);
        } catch (Exception e) {
            throw new RuntimeException("Hiba történt az AES blokk feldolgozása során: " + e.getMessage(), e);
        }
    }

}
