package com.integrationapi.security;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.PropertyValueEncryptionUtils;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeyUtil {

    /**
     * Decrypts a property value if it is encrypted.
     * <p>
     * Property values are considered encrypted if they are in the format
     * "ENC(..)"  where the inner value is the base64 encoded
     * encrypted value. If the value is not encrypted, it is simply returned.
     *
     * @param key
     *            The encryption key.
     * @param encryptedPropertyValue
     *            The property value to be decrypted.
     * @return The decrypted property value.
     */
    public String decryptPropertyValue(String key, String encryptedPropertyValue) {
        if (PropertyValueEncryptionUtils.isEncryptedValue(encryptedPropertyValue)) {
            return decryptPropertyValueUsingKey(key, encryptedPropertyValue);
        }
        throw new UnsupportedOperationException(
                "Encrypted String received without appropriate wrapper of ENC() to decrypt: "
                        + encryptedPropertyValue);
    }

    /**
     * Encrypts a property value if it is not already encrypted.
     * <p>
     * Encrypted values will be returned in the format"ENC(..)" where the inner
     * value is the base64 encoded encrypted value. If the value is already
     * encrypted, it is simply returned.
     *
     * @param key
     *            The encryption key.
     * @param propertyValue
     *            The property value to be encrypted.
     * @return The encrypted property value.
     */
    public String encryptPropertyValue(String key, String propertyValue) {
        return encryptUsingKey(key, propertyValue);
    }


    private String decryptPropertyValueUsingKey(String key, String encryptedPropertyValue) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(key);
        return PropertyValueEncryptionUtils.decrypt(encryptedPropertyValue, encryptor);
    }

    private String encryptUsingKey(String key, String propertyValue) {
        if (!PropertyValueEncryptionUtils.isEncryptedValue(propertyValue)) {
            StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
            encryptor.setPassword(key);
            return PropertyValueEncryptionUtils.encrypt(propertyValue, encryptor);
        }
        return propertyValue;
    }


}
