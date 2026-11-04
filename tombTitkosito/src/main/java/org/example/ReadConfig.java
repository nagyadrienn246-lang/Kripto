package org.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

public class ReadConfig {
    public static class Config {
        @JsonProperty("blocksize")
        public int blocksize;

        @JsonProperty("cipher")
        public String cipher;

        @JsonProperty("key")
        public String keyBase64;

        @JsonProperty("mode")
        public String mode;

        @JsonProperty("iv")
        public String ivBase64;

        @JsonProperty("padding")
        public String padding;

        public byte[] getKeyBytes() {
            return keyBase64 != null ? Base64.getDecoder().decode(keyBase64) : null;
        }

        public byte[] getIvBytes() {
            return ivBase64 != null ? Base64.getDecoder().decode(ivBase64) : null;
        }
    }

    public static Config loadConfig(String resourceFileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = ReadConfig.class.getClassLoader().getResourceAsStream(resourceFileName)) {
            if (is == null) {
                throw new IllegalArgumentException("A fájl nem található a resources mappában: " + resourceFileName);
            }
            return mapper.readValue(is, Config.class);
        }
    }

}
