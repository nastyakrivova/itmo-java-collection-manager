package com.myorg.lab5.utils;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class Password {
    public static String hashPassword(String password, String salt){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update((password + salt).getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(md.digest());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String generateSalt(){
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static boolean verifyPassword(String password, String salt, String expHash){
        return hashPassword(password, salt).equals(expHash);
    }
}
