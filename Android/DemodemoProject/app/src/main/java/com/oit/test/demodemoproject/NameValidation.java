package com.oit.test.demodemoproject;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameValidation {
    private  String firstName;
    private  String lastName;
    NameValidation(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
 final String expression = "^[A-Z+a-z]+$";
    public boolean validation (){
        Pattern p, p2;
        Matcher m, m2;

        p = Pattern.compile(expression);
        m = p.matcher(firstName);

        p2 = Pattern.compile(expression);
        m2 = p2.matcher(lastName);
        if(m.matches()&&m2.matches())
        {return true;
        }
        return false;
    }
}
