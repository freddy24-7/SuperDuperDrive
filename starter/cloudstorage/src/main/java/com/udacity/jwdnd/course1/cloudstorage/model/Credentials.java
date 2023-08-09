package com.udacity.jwdnd.course1.cloudstorage.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Credentials {
    private Integer credentialId;
    private String url;
    private String username;
    private String key;
    private String password;
    private Integer userId;

    //Adding a specific method to read the encryption service code
    //from the model layer - to facilitate the thymeleaf view
    //An IDE may say "no usage" but the code is used in thymeleaf
    public String getDecryptedPassword(){
        byte[] decryptedValue = null;

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            decryptedValue = cipher.doFinal(Base64.getDecoder().decode(password));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException
                 | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println(e.getMessage());
        }

        assert decryptedValue != null;
        return new String(decryptedValue);
    }
}
