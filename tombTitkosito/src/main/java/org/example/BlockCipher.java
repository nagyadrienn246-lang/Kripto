package org.example;

public interface BlockCipher {
    byte[] processBlock(byte[] block, byte[] key, boolean encrypt) throws Exception;
}
