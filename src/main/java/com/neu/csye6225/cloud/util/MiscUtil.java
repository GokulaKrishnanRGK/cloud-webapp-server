package com.neu.csye6225.cloud.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;

@Component
public class MiscUtil {

  private static final Pattern emailPattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]+$", Pattern.CASE_INSENSITIVE);

  public static boolean isValidEmail(String email) {
    Matcher emailMatcher = emailPattern.matcher(email);
    return emailMatcher.find();
  }

}
