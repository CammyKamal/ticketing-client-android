package com.example.theodhor.retrofit2;

import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kamal on 21/09/17.
 */

public class WsseToken {
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_WSSE = "X-WSSE";


    //DateTime dateTime=DateTime.now().formatGmt('yyyy-MM-dd\'T\'hh:mm:ss\'Z\'');
    //in our case, User is an entity (just a POJO) persisted into sqlite database
    private User user;
    private String nonce;
    private String createdAt;
    private String digest;
    private byte[] nonceBytes;

    public WsseToken(User user) {
        //we need the user object because we need his USERNAME
        this.user = user;
        this.createdAt = generateTimestamp();
        this.nonceBytes = generateNonce();
        this.digest = generateDigest();
    }

    private byte[] generateNonce() {
        SecureRandom rand = null;
        try {
            rand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        rand.setSeed(System.currentTimeMillis());
        byte[] nonceBytes = new byte[16];
        rand.nextBytes(nonceBytes);
        return nonceBytes;

    }


    private String generateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    private String generateDigest() {
        String digest = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.write(nonceBytes);
            baos.write(this.createdAt.getBytes("UTF-8"));
            baos.write(user.getPassword().getBytes());
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] digestedPassword = md.digest(baos.toByteArray());
            digest = Base64.encodeToString(digestedPassword, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {

        }
        return digest;
    }

    public String getWsseHeader() {

        StringBuilder header = new StringBuilder();
        header.append("UsernameToken Username=\"");
        header.append(this.user.getUsername());
        header.append("\", PasswordDigest=\"");
        header.append(this.digest);
        header.append("\", Nonce=\"");
        header.append(Base64.encodeToString(nonceBytes, Base64.NO_WRAP));
        header.append("\", Created=\"");
        header.append(this.createdAt);
        header.append("\"");
       return header.toString();
    }

    public String getAuthorizationHeader() {
        return "WSSE profile=\"UsernameToken\"";
    }
}
