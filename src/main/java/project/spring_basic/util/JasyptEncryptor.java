package project.spring_basic.util;

import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JasyptEncryptor {
    
    private final StringEncryptor stringEncryptor;


    public String encrypt(String originalData){
        return stringEncryptor.encrypt(originalData);
    }

    public String decrypt(String encodedData){
        return stringEncryptor.decrypt(encodedData);
    }
}
