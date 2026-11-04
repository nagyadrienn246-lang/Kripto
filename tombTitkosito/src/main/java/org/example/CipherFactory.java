package org.example;

import org.example.cipherAlgorithms.Aes;
import org.example.cipherAlgorithms.VigenereBin;

public class CipherFactory {
    public static BlockCipher createCipher(ReadConfig.Config config) {
        if (config.cipher == null || config.cipher.trim().isEmpty()) {
            throw new IllegalArgumentException("A cipher név nem lehet üres!");
        }

        String normalized = config.cipher.trim().toUpperCase();

        if (normalized.startsWith("AES")) {
            validateAesConfig(config);
            return new Aes();
        } else if ("VIGENERE-BIN".equals(normalized)) {
            validateVigenere(config);
            return new VigenereBin();
        }

        throw new IllegalArgumentException("Nem támogatott algoritmus: " + config.cipher);
    }

    public static void validateVigenere(ReadConfig.Config config){
        int keyLength = config.getKeyBytes().length;
        if (keyLength != config.blocksize){
            throw new IllegalArgumentException(
                    "A Vigenere kulcs hossza egyenlo kell legyen a blocksize-al! (Megadva: " + keyLength + " byte)"
            );
        }
    }

    public static void validateAesConfig(ReadConfig.Config config) {
        String cipher = config.cipher.trim().toUpperCase();


        if (config.blocksize != 16) {
            throw new IllegalArgumentException(
                    "Hibas blokkmeret! Az AES blokkmerete  16 byte lehet (megadva: " + config.blocksize + ")."
            );
        }

        int keyLength = config.getKeyBytes().length;
        //kulcshossz
        switch (cipher) {
            case "AES-128":
                if (keyLength != 16) {
                    throw new IllegalArgumentException(
                            "Az AES-128 kulcs hossza 16 byte kell legyen! (Megadva: " + keyLength + " byte)"
                    );
                }
                break;

            case "AES-192":
                if (keyLength != 24) {
                    throw new IllegalArgumentException(
                            "Az AES-192 kulcs hossza 24 byte kell legyen! (Megadva: " + keyLength + " byte)"
                    );
                }
                break;

            case "AES-256":
                if (keyLength != 32) {
                    throw new IllegalArgumentException(
                            "Az AES-256 kulcs hossza 32 byte kell legyen! (Megadva: " + keyLength + " byte)"
                    );
                }
                break;

            default:

                throw new IllegalArgumentException(
                        "Lehetseges AES: AES-256, AES-192, AES-128"
                );

        }
    }
}

