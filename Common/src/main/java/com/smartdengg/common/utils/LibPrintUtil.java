package com.smartdengg.common.utils;

import java.util.Arrays;
import java.util.List;

/**
 * 创建时间:  2016/10/31 14:18 <br>
 * 作者:  dengwei <br>
 * 描述:  打印工具类
 */
class LibPrintUtil {

  /** Drawing toolbox */
  private static final char TOP_LEFT_CORNER = '╔';
  private static final char BOTTOM_LEFT_CORNER = '╚';
  private static final char MIDDLE_CORNER = '╟';
  private static final char HORIZONTAL_DOUBLE_LINE = '║';
  private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
  private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
  private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
  private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;

  private static final String SEPARATOR = System.getProperty("line.separator");

  private LibPrintUtil() {
    throw new AssertionError("no instance");
  }

  static String translate(String... messages) {

    if (messages == null) return "null";

    String[] copyFromArray = Arrays.copyOf(messages, messages.length, String[].class);

    return getString(copyFromArray);
  }

  static String translate(List<String> messages) {

    if (messages == null) return "null";

    String[] copyFromList = messages.toArray(new String[messages.size()]);

    return getString(copyFromList);
  }

  private static String getString(String[] messageAsArray) {

    int maxLength = messageAsArray.length - 1;
    if (maxLength == -1) return "[]";

    StringBuilder result = new StringBuilder();

    /*Top border*/
    result.append(TOP_BORDER).append(SEPARATOR);
    for (int i = 0; ; i++) {

      String[] lines = messageAsArray[i].split(SEPARATOR);

      for (String chunk : lines) {
        result.append(HORIZONTAL_DOUBLE_LINE).append('\t').append(chunk).append(SEPARATOR);
      }

       /*Bottom border*/
      if (i == maxLength) return result.append(BOTTOM_BORDER).toString();

       /*Middle border*/
      result.append(MIDDLE_BORDER).append(SEPARATOR);
    }
  }

  static synchronized String translate(String title, String message) {

    StringBuilder result = new StringBuilder();

    /*box title*/
    result.append(TOP_BORDER).append(SEPARATOR);
    result.append("║").append("\t").append(title).append(SEPARATOR);
    result.append(MIDDLE_BORDER).append(SEPARATOR);

    /*box message*/
    String[] lines = message.split(SEPARATOR);
    for (String chunk : lines) {
      result.append(HORIZONTAL_DOUBLE_LINE).append("\t").append(chunk).append(SEPARATOR);
    }
    result.append(BOTTOM_BORDER);

    return result.toString();
  }
}
