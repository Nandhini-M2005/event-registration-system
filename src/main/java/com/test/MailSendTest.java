package com.test;

import com.util.EmailUtil;

public class MailSendTest {
    public static void main(String[] args) {
        EmailUtil.sendEmail(
            "rakshitha.2305124@srec.ac.in",
            "Event Registration Test",
            "This is a test email from Event Registration System."
        );
    }
}
