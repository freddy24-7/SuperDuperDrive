package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
public class CredentialsService {

    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;
    private final UserMapper userMapper;
    private List<Credentials> credentials;

    public void InsertCredentials(Credentials credentials, String userName) {
        Credentials newCredentials = createCredentialsWithEncryptedPassword(credentials, userName);
        credentialsMapper.insert(newCredentials);
    }

    public void updateCredentials(Credentials credentials, String userName) {
        Credentials existingCredentials = credentialsMapper.getCredentialById(credentials.getCredentialId());

        if (existingCredentials != null) {
            String encodedKey = existingCredentials.getKey();
            String decryptedPassword = encryptionService.decryptValue(existingCredentials.getPassword(), encodedKey);

            existingCredentials.setUrl(credentials.getUrl());
            existingCredentials.setUsername(credentials.getUsername());

            if (!credentials.getPassword().isEmpty() && !credentials.getPassword().equals(decryptedPassword)) {
                String newEncryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);
                existingCredentials.setPassword(newEncryptedPassword);
            }

            credentialsMapper.update(existingCredentials);
        }
    }

    public void deleteCredential(Integer credentialId) {
        credentialsMapper.delete(credentialId);
    }

    public List<Credentials> getAllCredentials(String userName) {
        credentials = credentialsMapper.getAllCredentials(userMapper.getUser(userName).getUserId());
        return credentialsMapper.getAllCredentials(userMapper.getUser(userName).getUserId());
    }

    public Credentials createCredentialsWithEncryptedPassword(Credentials credentials, String userName) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credentials.getPassword(), encodedKey);

        return new Credentials(credentials.getCredentialId(), credentials.getUrl(),
                credentials.getUsername(), encodedKey, encryptedPassword,
                userMapper.getUser(userName).getUserId());
    }

    public String retrieveEncodedKeyForCredential(Integer credentialId) {
        Credentials credential = credentialsMapper.getCredentialById(credentialId);
        return credential != null ? credential.getKey() : null;
    }
}
