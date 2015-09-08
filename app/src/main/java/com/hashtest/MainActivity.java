package com.pashword;

import android.database.Cursor;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.app.Activity;
import android.os.Bundle;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import android.util.Base64;
import java.util.ArrayList;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.text.TextWatcher;
import android.text.Editable;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;


public class MainActivity extends Activity
{
    EditText message_tv, secret_tv;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        message_tv = (EditText) findViewById(R.id.message);
        secret_tv = (EditText) findViewById(R.id.secret);

        secret_tv.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String secret = secret_tv.getText().toString();
                setHashTotem(secret);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
       });
    }

    private void setHashTotem(String secret) {
        int color_int = 0;
        if (secret.length() > 0) {
            try {
                byte[] bytes = generate_hash("Pashword".getBytes(), secret);
            } catch (Exception e) {
                // A lick and a promise
            }
            color_int = Color.parseColor("#" + bytesToHex(bytes).substring(0, 6));
        } else {
            color_int = Color.parseColor("#000000");
        }
        ((TextView) findViewById(R.id.color_int)).setText("Color int: " + color_int);
        View totem_view = findViewById(R.id.totem);
        GradientDrawable totem_drawable = (GradientDrawable)totem_view.getBackground();
        totem_drawable.setColor(color_int);
    }

    public void generate_and_show_hashes(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(secret_tv.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(message_tv.getWindowToken(), 0);

        try {
            String message = message_tv.getText().toString();
            String secret = secret_tv.getText().toString();
            String hash = "";

            ArrayList<String> hashes = new ArrayList<String>();
            byte[] bytes = generate_hash(message.getBytes(), secret);

            for (int i = 0; i < 10; i++) {
                bytes = generate_hash(bytes, secret);
                hash = Base64.encodeToString(bytes, Base64.DEFAULT);
                hashes.add(hash.substring(0, 10));
            }

            ArrayAdapter<String> hashListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, hashes);
            ListView hashListView = (ListView)findViewById(R.id.hashListView);
            hashListView.setAdapter(hashListAdapter);
            hashListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selected_hash = (String) parent.getItemAtPosition(position);

                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", selected_hash);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Hash copied to clipboard!", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "error: " + e, Toast.LENGTH_LONG).show();
            System.out.println("ALEXLONG123 - error");
            System.out.println(e);
            e.printStackTrace();

        }
    }

    public static byte[] generate_hash(byte[] message, String key) throws NoSuchAlgorithmException, InvalidKeyException {
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

    public static int byteArrayToInt(byte[] b) {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (b[i] & 0x000000FF) << shift;
        }
        return value;
    }

}
