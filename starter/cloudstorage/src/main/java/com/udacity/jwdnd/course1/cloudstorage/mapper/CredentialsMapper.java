package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialsMapper {

    @Select("Select * from CREDENTIALS WHERE  userId =#{userId}")
    List<Credentials> getAllCredentials(Integer userId);

    @Insert("INSERT INTO CREDENTIALS (url, username, key, password, userId) " +
            "VALUES(#{url}, #{username}, #{key}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    void insert(Credentials credentials);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialId = #{credentialId}")
    Credentials getCredentialById(Integer credentialId);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialId = #{credentialId}" )
    void delete(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url=#{url}, username=#{username}, " +
            "key=#{key}, password=#{password}, userId=#{userId} WHERE credentialId=#{credentialId}")
    void update(Credentials credentials);
}
