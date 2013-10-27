package com.bpmco.tramitefacil.Database;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by smarin on 18/10/13.
 */
public class Validation {


    public boolean checkemail(String email)
    {
        Pattern pattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}
