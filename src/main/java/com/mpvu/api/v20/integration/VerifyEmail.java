package com.mpvu.api.v20.integration;

import org.springframework.stereotype.Service;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class VerifyEmail {
    public boolean verify(String email) {
        return true;
//        String regex = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$";
//        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(email);
//        if (!matcher.matches()) {
//            return false;
//        }
//
//        String domain = email.split("@")[1];
//
//        try {
//            Lookup lookup = new Lookup(domain, Type.MX);
//            Record[] records = lookup.run();
//            if (records == null || records.length == 0) {
//                return false;
//            }
//            return true;
//        } catch (Exception e) {
//            System.err.println("Error in verify domain: " + e.getMessage());
//            return false;
//        }
    }
}