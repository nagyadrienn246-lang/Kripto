package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            ReadConfig.Config config = ReadConfig.loadConfig("config.json");

            CipherEngine engine = new CipherEngine(config);

            String originalText = "almafa";
            byte[] plaintext = originalText.getBytes();

            byte[] ciphertext = engine.encrypt(plaintext);
            byte[] decryptedBytes = engine.decrypt(ciphertext);

            System.out.println("Eredeti: " + originalText);
            System.out.println("Visszafejtett: " + new String(decryptedBytes));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
