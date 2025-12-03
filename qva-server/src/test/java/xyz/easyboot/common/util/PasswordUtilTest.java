package xyz.easyboot.common.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilTest {

    @Test
    public void encrypt() {
        String password = "888888";
        String encode = PasswordUtil.encrypt(password);
        System.out.println(encode);
    }
    
}
