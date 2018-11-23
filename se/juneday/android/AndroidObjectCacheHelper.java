package se.juneday.android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import se.juneday.ObjectCache;

public class AndroidObjectCacheHelper  {

  private final static String LOG_TAG =
    AndroidObjectCacheHelper.class.getName();
  
  public static String objectCacheFileName(Context context, Class clazz) throws AndroidObjectCacheHelperException {
    
    PackageManager m = context.getPackageManager();
    String s = context.getPackageName();
    try {
      PackageInfo p = m.getPackageInfo(s, 0);
      s = p.applicationInfo.dataDir;
    } catch (PackageManager.NameNotFoundException e) {
      Log.d(LOG_TAG, "Error, could not build file name for serialization", e);
      throw new AndroidObjectCacheHelperException(e, "could not build file name for serialization");
    }
    String fileName = s +
      "/" + clazz.getName();

    return fileName;
  }

  public static class AndroidObjectCacheHelperException extends Exception {
    public AndroidObjectCacheHelperException(Exception e, String msg) {
      super(msg);
    }
  }

}
