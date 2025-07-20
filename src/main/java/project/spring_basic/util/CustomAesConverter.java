package project.spring_basic.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;



@Converter
@RequiredArgsConstructor
public class CustomAesConverter implements AttributeConverter<String, String> {
    
    private final JasyptEncryptor jasyptEncryptor;


    @Override
    public String convertToDatabaseColumn(String originalData){
        return originalData == null ? null : jasyptEncryptor.encrypt(originalData);
    }

    @Override
    public String convertToEntityAttribute(String encodedData){
        return encodedData == null ? null : jasyptEncryptor.decrypt(encodedData);
    }
}
