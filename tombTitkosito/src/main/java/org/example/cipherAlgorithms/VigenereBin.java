package org.example.cipherAlgorithms;

import org.example.BlockCipher;

public class VigenereBin implements BlockCipher {

    @Override
    public byte[] processBlock(byte[] block, byte[] key, boolean encrypt) {
        byte[] result = new byte[block.length];

        for (int i = 0; i < block.length; i++) {
            byte keyByte = key[i % key.length];
            if (encrypt) {
                result[i] = (byte) (block[i] + keyByte);
            } else {
                result[i] = (byte) (block[i] - keyByte);
            }
        }

        return result;
    }
}
