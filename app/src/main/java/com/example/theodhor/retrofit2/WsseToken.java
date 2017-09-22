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
        //we need the user object because we need his username
        this.user = user;
        this.createdAt = generateTimestamp();
        this.nonceBytes = generateNonce();
       /* try {

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        this.digest = generateDigest();
    }

    private byte[] generateNonce() {
       /* SecureRandom random = new SecureRandom();
        byte seed[] = random.generateSeed(10);
        return bytesToHex(seed);*/
        SecureRandom rand = null;
        try {
            rand = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        rand.setSeed(System.currentTimeMillis());
        byte[] nonceBytes = new byte[16];
        rand.nextBytes(nonceBytes);
        // String nonceB64 = Base64.encodeToString(nonceBytes, Base64.NO_WRAP);
        return nonceBytes;

    }
  /*  private static String generateNonce() throws NoSuchAlgorithmException, NoSuchProviderException, UnsupportedEncodingException
    {
        String dateTimeString = Long.toString(new Date().getTime());
        byte[] nonceByte = dateTimeString.getBytes();
        return Base64.decode(nonceByte.toString(),Base64.NO_WRAP).toString();
    }*/

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private String generateTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
       /* Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();*/
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(new Date());
    }

    private String generateDigest() {
        String digest = null;
        /*try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            StringBuilder sb = new StringBuilder();
            sb.append(this.nonce);
            sb.append(this.createdAt);
            sb.append(this.user.getPassword());
            byte sha[] = md.digest(sb.toString().getBytes());
            digest = Base64.encodeToString(sha, Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }*/
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
        //return  "UsernameToken Username=\"admin\", PasswordDigest=\"Aywa/w6Ew8bf/MKifJiUPpMxjM8=\", Nonce=\"ODZlYzg4YjRhMzMwNDM2MA==\", Created=\"2017-09-22T17:52:24+07:00\"" ;
       return header.toString();
    }

    public String getAuthorizationHeader() {
        return "WSSE profile=\"UsernameToken\"";
    }
}
