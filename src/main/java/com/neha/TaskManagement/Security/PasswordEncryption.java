package com.neha.TaskManagement.Security;

import com.neha.TaskManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PasswordEncryption {
    static int shift = 4;
    public static String encrypt(String password){
        String encryptedPassword = "";
        password = password.toLowerCase();
        for(int i = 0 ; i<password.length(); i++){
            char ch = password.charAt(i);
            if(Character.isLetter(ch)){
                ch = (char) (((int)password.charAt(i)+shift-97)%26+97);
            } else  {
                ch = (char)(((ch + shift -'0') % 10) + '0');
            }
            encryptedPassword+=ch;
        }
        return encryptedPassword;
    }
//    public static String decrypt(String password){
//        String decryptedPassword = "";
//        password = password.toLowerCase();
//        for(int i = 0 ; i<password.length(); i++){
//            char ch = password.charAt(i);
//            if(Character.isLetter(ch)){
//                ch = (char) (((int)password.charAt(i)-shift-97)%26+97);
//            } else  {
//                ch = (char)(((ch - shift -'0') % 10) + '0');
//            }
//            decryptedPassword+=ch;
//        }
//        return decryptedPassword;
//    }
}
