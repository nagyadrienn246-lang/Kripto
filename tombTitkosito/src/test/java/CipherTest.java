import org.example.CipherEngine;
import org.example.ReadConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CipherTest {
    private CipherEngine createEngineFromResource(String resourceName) throws Exception {
        ReadConfig.Config config = ReadConfig.loadConfig(resourceName);
        return new CipherEngine(config);
    }

    // 1. AES-128 ECB Teszt (aes-128_ecb.json)
    @Test
    @DisplayName("AES-128 ECB ")
    void testAes128Ecb() throws Exception {
        CipherEngine engine = createEngineFromResource("aes-128_ecb.json");

        String plaintext = "1234567890abcdef";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        String actualBase64 = Base64.getEncoder().encodeToString(encrypted);

        String expectedBase64 = "Rhpf/Z33kXE1jp4Bd9hOqg==";
        assertEquals(expectedBase64, actualBase64, "A kódolt Base64 kimenet nem egyezik a várt értékkel!");
        byte[] decrypted = engine.decrypt(encrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 2. AES-192 CBC Teszt (aes-192_cbc.json)
    @Test
    @DisplayName("AES-192 CBC ")
    void testAes192Cbc() throws Exception {
        CipherEngine engine = createEngineFromResource("aes-192_cbc.json");

        String plaintext = "1234567890abcdef";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        String actualBase64 = Base64.getEncoder().encodeToString(encrypted);

        String expectedBase64 = "oL5A7WMCZJi1t5VBBnRz9g==";
        assertEquals(expectedBase64, actualBase64, "AES-192 CBC kódolási kimenet eltérés!");

        byte[] decrypted = engine.decrypt(encrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 3. AES-256 CFB
    @Test
    @DisplayName("AES-256 CFB")
    void testAes256Cfb() throws Exception {
        CipherEngine engine = createEngineFromResource("aes-256_cfb.json");

        String plaintext = "CFB Mód Tesztelése 256 Bites Kulccsal!";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);

        byte[] decrypted = engine.decrypt(encrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 4. AES-128 OFB
    @Test
    @DisplayName("AES-128 OFB ")
    void testAes128Ofb() throws Exception {
        CipherEngine engine = createEngineFromResource("aes-128_ofb.json");

        String plaintext = "OFB Mode Test Data";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);

        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 5. AES-256 CTR
    @Test
    @DisplayName("AES-256 CTR ")
    void testAes256Ctr() throws Exception {
        CipherEngine engine = createEngineFromResource("aes-256_ctr.json");

        String plaintext = "CTR mode streaming test";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);

        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 6.
    @Test
    @DisplayName("VIGENERE-BIN ECB")
    void testVigenereEcb() throws Exception {
        CipherEngine engine = createEngineFromResource("vigenere_ecb.json");

        String plaintext = "TesztÜzenet!";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        String actualBase64 = Base64.getEncoder().encodeToString(encrypted);

        byte[] decrypted = engine.decrypt(encrypted);
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8), "A visszafejtett szöveg nem egyezik az eredetivel!");
    }


    // 7. VIGENERE-BIN CBC
    @Test
    @DisplayName("VIGENERE-BIN CBC")
    void testVigenereCbc() throws Exception {
        CipherEngine engine = createEngineFromResource("vigenere_cbc.json");

        String plaintext = "Hello Vigenere CBC!";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);

        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 8. VIGENERE-BIN CFB
    @Test
    @DisplayName("VIGENERE-BIN CFB")
    void testVigenereCfb() throws Exception {
        CipherEngine engine = createEngineFromResource("vigenere_cfb.json");

        String plaintext = "Teszt";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);
        String actualBase64 = Base64.getEncoder().encodeToString(encrypted);

        String expectedBase64 = "goKvk5JK1d8=";
        assertEquals(expectedBase64, actualBase64, "A Vigenère CFB kódolt Base64 kimenet nem egyezik!");
        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 9. VIGENERE-BIN OFB
    @Test
    @DisplayName("VIGENERE-BIN OFB ")
    void testVigenereOfb() throws Exception {
        CipherEngine engine = createEngineFromResource("vigenere_ofb.json");

        String plaintext = "OFB Stream Cipher Test Data";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);

        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }

    // 10. VIGENERE-BIN CTR
    @Test
    @DisplayName("VIGENERE-BIN CTR ")
    void testVigenereCtr() throws Exception {
        CipherEngine engine = createEngineFromResource("vigenere_ctr.json");

        String plaintext = "CTR Counter Mode Test 123456";
        byte[] inputBytes = plaintext.getBytes(StandardCharsets.UTF_8);

        byte[] encrypted = engine.encrypt(inputBytes);
        byte[] decrypted = engine.decrypt(encrypted);

        assertEquals(plaintext, new String(decrypted, StandardCharsets.UTF_8));
    }
}
