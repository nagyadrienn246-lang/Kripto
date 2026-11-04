package org.example;

import java.util.Arrays;

public class CipherEngine {
    private final ReadConfig.Config config;
    private final BlockCipher cipherAlgo;
    private final byte[] key;
    private final byte[] iv;
    private final int blockSize;

    public CipherEngine(ReadConfig.Config config) {
        this.config = config;
        this.cipherAlgo = CipherFactory.createCipher(config);

        this.key = config.getKeyBytes();
        this.iv = config.getIvBytes();
        this.blockSize = config.blocksize;

        if (!"ECB".equalsIgnoreCase(config.mode) && (this.iv == null || this.iv.length != this.blockSize)) {
            throw new IllegalArgumentException("Az IV megadása kötelező ehhez a módhoz (" + config.mode + ")!");
        }
    }

    public byte[] encrypt(byte[] plaintext) throws Exception {
        byte[] padded = applyPadding(plaintext);
        int blockCount = padded.length / blockSize;
        byte[] ciphertext = new byte[padded.length];
        byte[] currentIV = (iv != null) ? Arrays.copyOf(iv, iv.length) : null;

        for (int i = 0; i < blockCount; i++) {
            byte[] block = Arrays.copyOfRange(padded, i * blockSize, (i + 1) * blockSize);
            byte[] encryptedBlock = new byte[blockSize];

            switch (config.mode.toUpperCase()) {
                case "ECB":
                    encryptedBlock = cipherAlgo.processBlock(block, key, true);
                    break;
                case "CBC":
                    byte[] xoredCbc = xor(block, currentIV);
                    encryptedBlock = cipherAlgo.processBlock(xoredCbc, key, true);
                    currentIV = encryptedBlock;
                    break;
                case "CFB":
                    byte[] cipherIvCfb = cipherAlgo.processBlock(currentIV, key, true);
                    encryptedBlock = xor(block, cipherIvCfb);
                    currentIV = encryptedBlock;
                    break;
                case "OFB":
                    currentIV = cipherAlgo.processBlock(currentIV, key, true);
                    encryptedBlock = xor(block, currentIV);
                    break;
                case "CTR":
                    byte[] cipherIvCtr = cipherAlgo.processBlock(currentIV, key, true);
                    encryptedBlock = xor(block, cipherIvCtr);
                    incrementCounter(currentIV);
                    break;
                default:
                    throw new UnsupportedOperationException("Nem támogatott működési mód: " + config.mode);
            }
            System.arraycopy(encryptedBlock, 0, ciphertext, i * blockSize, blockSize);
        }
        return ciphertext;
    }

    public byte[] decrypt(byte[] ciphertext) throws Exception {
        if (ciphertext.length % blockSize != 0) {
            throw new IllegalArgumentException("A titkosított adat hossza nem a blokkméret többszöröse.");
        }

        int blockCount = ciphertext.length / blockSize;
        byte[] decryptedPadded = new byte[ciphertext.length];
        byte[] currentIV = (iv != null) ? Arrays.copyOf(iv, iv.length) : null;

        for (int i = 0; i < blockCount; i++) {
            byte[] block = Arrays.copyOfRange(ciphertext, i * blockSize, (i + 1) * blockSize);
            byte[] decryptedBlock = new byte[blockSize];

            switch (config.mode.toUpperCase()) {
                case "ECB":
                    decryptedBlock = cipherAlgo.processBlock(block, key, false);
                    break;
                case "CBC":
                    byte[] decCbc = cipherAlgo.processBlock(block, key, false);
                    decryptedBlock = xor(decCbc, currentIV);
                    currentIV = block;
                    break;
                case "CFB":
                    byte[] cipherIvCfb = cipherAlgo.processBlock(currentIV, key, true);
                    decryptedBlock = xor(block, cipherIvCfb);
                    currentIV = block;
                    break;
                case "OFB":
                    currentIV = cipherAlgo.processBlock(currentIV, key, true);
                    decryptedBlock = xor(block, currentIV);
                    break;
                case "CTR":
                    byte[] cipherIvCtr = cipherAlgo.processBlock(currentIV, key, true);
                    decryptedBlock = xor(block, cipherIvCtr);
                    incrementCounter(currentIV);
                    break;
                default:
                    throw new UnsupportedOperationException("Nem támogatott működési mód: " + config.mode);
            }
            System.arraycopy(decryptedBlock, 0, decryptedPadded, i * blockSize, blockSize);
        }
        return removePadding(decryptedPadded);
    }

    private byte[] applyPadding(byte[] input) {
        String padType = config.padding.toUpperCase();

        int remainder = input.length % blockSize;
        int padLen;

        if (remainder == 0) {
            padLen = "ZERO".equals(padType) ? 0 : blockSize;
        } else {
            padLen = blockSize - remainder;
        }

        if (padLen == 0) {
            return input;
        }

        byte[] padded = Arrays.copyOf(input, input.length + padLen);
        switch (padType) {
            case "ZERO":
                break;
            case "DES":
                padded[input.length] = (byte) 0x80;
                break;
            case "SF":
                for (int i = input.length; i < padded.length; i++) {
                    padded[i] = (byte) padLen;
                }
                break;
            default:
                throw new IllegalArgumentException("Ismeretlen padding: " + config.padding);
        }
        return padded;
    }

    private byte[] removePadding(byte[] input) {
        String padType = config.padding.toUpperCase();
        switch (padType) {
            case "ZERO":
                int lastNonZero = input.length - 1;
                while (lastNonZero >= 0 && input[lastNonZero] == 0) {
                    lastNonZero--;
                }
                return Arrays.copyOf(input, lastNonZero + 1);
            case "DES":
                int index = input.length - 1;
                while (index >= 0 && input[index] == 0) {
                    index--;
                }
                if (index >= 0 && (input[index] & 0xFF) == 0x80) {
                    return Arrays.copyOf(input, index);
                }
                throw new IllegalArgumentException("Hibás DES padding!");
            case "SF":
                int padLen = input[input.length - 1] & 0xFF;
                if (padLen <= 0 || padLen > blockSize) {
                    throw new IllegalArgumentException("Hibás Schneier-Ferguson padding!");
                }
                return Arrays.copyOf(input, input.length - padLen);
            default:
                return input;
        }
    }

    private byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    private void incrementCounter(byte[] counter) {
        for (int i = counter.length - 1; i >= 0; i--) {
            counter[i]++;
            if (counter[i] != 0) break;
        }
    }
}