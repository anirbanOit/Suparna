package com.oit.test.demodemoproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main3Activity extends AppCompatActivity {

    AutoCompleteTextView email;
    EditText password;
    Pattern p, p2;
    Matcher m, m2;
    Button signin;
    LinearLayout keyboard;
    private ImageView caps,small,no,special,length;
    String checkemail;
    String checkpass;
    HashMap<String, String> h;
    boolean loginflag;
    public static final String regEx = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    final String PASSWORD_PATTERN = "^((?=.*[a-z])(?=.*[A-Z])(?=.*\\\\d)|(?=.*\\\\d)(?=.*[A-Z])(?=.*?[#!@$%&])|(?=.*\\\\d)(?=.*[a-z])(?=.*?[#!@$%&])|(?=.*[a-z])(?=.*[A-Z])(?=.*?[#!@$%&])).+$*";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        h = new HashMap<String, String>();
        h.put("jewel25m@gmail.com","Master@25m");
        h.put("sarthak14@gmail.com","Master@26m");
        keyboard = (LinearLayout)findViewById(R.id.mainlayoutforlogin);
        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
        signin = (Button) findViewById(R.id.signin);
        email = (AutoCompleteTextView) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        caps = (ImageView) findViewById(R.id.caps);
        small = (ImageView) findViewById(R.id.small);
        no = (ImageView) findViewById(R.id.no);
        special = (ImageView) findViewById(R.id.spec);
        length = (ImageView) findViewById(R.id.len);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                caps.setBackgroundResource(0);
                no.setBackgroundResource(0);
                small.setBackgroundResource(0);
                length.setBackgroundResource(0);
                special.setBackgroundResource(0);
                loginflag =false;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                passChecker();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginflag=true;
                passChecker();
                log();
            }

        });
    }
    public void log() {
        p = Pattern.compile(regEx);
        m = p.matcher(email.getText().toString());
        p2 = Pattern.compile(PASSWORD_PATTERN);
        m2 = p2.matcher(password.getText().toString());
        if (m.matches()) {
            if (m2.matches()) {
                checkemail = email.getText().toString();
                checkpass = password.getText().toString();
            Iterator myVeryOwnIterator = h.keySet().iterator();
            while(myVeryOwnIterator.hasNext()) {
                String key=(String)myVeryOwnIterator.next();
                String value=(String)h.get(key);
                    if (key.equals(checkemail) && value.equals(checkpass)) {
                        Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                   break;
                    } else {
                        passChecker();
                        Toast.makeText(getApplicationContext(), "Wrong Credentials", Toast.LENGTH_SHORT).show();
                    }
            }
            } else {
                Toast.makeText(Main3Activity.this, " Password must contain minimum 8 characters at least 1 Alphabet, 1 Number and 1 Special Character ", Toast.LENGTH_LONG).show();
                loginflag=false;
            }
        } else {
            Toast.makeText(Main3Activity.this, "Email format missing ", Toast.LENGTH_LONG).show();
        }
    }
    private void passChecker() {
        String pass = password.getText().toString();
        Pattern pc = Pattern.compile(".*[A-Z].*");
        Matcher mc = pc.matcher(pass);
        if (!mc.matches()) {
            if(loginflag==true){caps.setBackgroundResource(R.mipmap.cross);}
        }else{
            caps.setBackgroundResource(R.mipmap.tickmark);
        }
        Pattern ps = Pattern.compile(".*[a-z].*");
        Matcher ms = ps.matcher(pass);
        if (!ms.matches()) {
            if(loginflag==true){small.setBackgroundResource(R.mipmap.cross);}
        }
        else{
            small.setBackgroundResource(R.mipmap.tickmark);
        }
        Pattern pn = Pattern.compile(".*[0-9].*");
        Matcher mn = pn.matcher(pass);
        if (!mn.matches()) {
            if(loginflag==true){no.setBackgroundResource(R.mipmap.cross);}
        }
        else{
            no.setBackgroundResource(R.mipmap.tickmark);
        }
        Pattern psp = Pattern.compile(".*[#!@$%&].*");
        Matcher msp = psp.matcher(password.getText().toString());
        if (!msp.matches()) {
            if(loginflag==true){special.setBackgroundResource(R.mipmap.cross);}
        }
        else{
            special.setBackgroundResource(R.mipmap.tickmark);
        }
        Pattern pl = Pattern.compile(".{8,}$");
        Matcher ml = pl.matcher(password.getText().toString());
        if (!ml.matches()) {
            if(loginflag==true){length.setBackgroundResource(R.mipmap.cross);}
        }
        else{
            length.setBackgroundResource(R.mipmap.tickmark);
        }
    }
}
