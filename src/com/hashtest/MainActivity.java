package com.hashtest;

import android.app.Activity;
import android.os.Bundle;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toast.makeText(this, "HELLO!", Toast.LENGTH_LONG).show();
        System.out.println("ALEXLONG123 - START");
        try {
            String secret = "test";
            String message = "test";

            byte[] bytes = hash(message.getBytes(), secret);
            String hash = Base64.encodeToString(bytes, Base64.DEFAULT);
            String output = "";
            output = output + "base64 of hash: " + hash + "\n";
            output = output + "hex of hash: " + bytesToHex(bytes) + "\n";

            bytes = hash(bytes, secret);
            hash = Base64.encodeToString(bytes, Base64.DEFAULT);
            output = output + "base64 of 2nd hash: " + hash + "\n";
            output = output + "hex of 2nd hash: " + bytesToHex(bytes) + "\n";
            output = output + "ALEXLONG123" + "\n";
            ((TextView) findViewById(R.id.text_view)).setText(output);
            Toast.makeText(this, "hash is: " + hash, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "error: " + e, Toast.LENGTH_LONG).show();
            System.out.println("ALEXLONG123 - error");
            System.out.println(e);
            e.printStackTrace();

        }
    }

    public static byte[] hash(byte[] message, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] bytes = sha256_HMAC.doFinal(message);
        return bytes;
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
