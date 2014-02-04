package com.meizhuo.etips.common;

import android.util.Log;
/**
 * wrapper of Log , log easier!<br>
 * you can use <h1>disableLogging()<h1>,no logs will be passed to LogCat, all log methods will do nothing
 * @author Jayin Ton
 *
 */
public final class L {
      //only Log pre-two
	 // private static final String LOG_FORMAT = "%1$s\n%2$s";
	  /** disable Logging **/
      private static volatile boolean DISABLED = false;  
      private static String TAG = "debug";

      private L() {
      }

      /** Enables logger (if {@link #disableLogging()} was called before) */
      public static void enableLogging() {
              DISABLED = false;
      }

      /** Disables logger, no logs will be passed to LogCat, all log methods will do nothing */
      public static void disableLogging() {
              DISABLED = true;
      }

      public static void d(String message) {
              log(Log.DEBUG, null, message);
      }

      public static void i(String message) {
              log(Log.INFO, null, message);
      }

      public static void w(String message) {
              log(Log.WARN, null, message);
      }

      public static void e(Throwable ex) {
              log(Log.ERROR, ex, null);
      }

      public static void e(String message) {
              log(Log.ERROR, null, message);
     }

      public static void e(Throwable ex,String message) {
              log(Log.ERROR, ex, message);
      }
      
      private static final String LOG_FORMAT = "%1$s\t\n%2$s";
      
      private static void log(int priority, Throwable ex, String message) {
              if (DISABLED) return;
              String log;
              if (ex == null) {
                      log = message;
              } else {
                      String logMessage = message == null ? ex.getMessage() : message;
                      String logBody = Log.getStackTraceString(ex);
                      log = String.format(LOG_FORMAT, logMessage, logBody);
              }
              StackTraceElement[] s = Thread.currentThread().getStackTrace();
              Log.println(priority, TAG, s[s.length-1].getClassName()+":"+s[s.length-1].getMethodName()+"()");
              Log.println(priority, TAG, log);
      }

}
