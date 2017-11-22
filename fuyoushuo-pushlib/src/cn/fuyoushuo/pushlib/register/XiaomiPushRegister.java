package cn.fuyoushuo.pushlib.register;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.taobao.accs.utl.UtilityImpl;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by QA on 2017/9/20.
 */

public class XiaomiPushRegister {

   public static final String TAG = "xiaomiPushRegister";

   private static final String PACKAGE_XIAOMI = "com.xiaomi.xmsf";
   private static final String XIAOMI = "Xiaomi".toLowerCase();
   private static String phoneBrand;

  static {
    phoneBrand = Build.BRAND;
  }

   private static XiaomiPushCallback mXiaomiPushCallback;

   public interface XiaomiPushCallback{
     void onMessage(String message);
   }

  public static boolean checkDevice(Context var0) {
    boolean var1 = false;
    try {
      PackageManager var2 = var0.getPackageManager();
      PackageInfo var3 = null;
      if(TextUtils.equals(XIAOMI, phoneBrand.toLowerCase())) {
        var3 = var2.getPackageInfo("com.xiaomi.xmsf",4);
        if(var3 != null && var3.versionCode >= 105) {
          var1 = true;
        }
      }
    } catch (Throwable var4) {
      Log.e("MiPushRegistar", "checkDevice error: " + var4);
    }

    Log.e("MiPushRegistar", "checkDevice result: " + var1);
    return var1;
  }

  public static void register(final Context context, final String var1, final String var2) {
    try {
      if(!UtilityImpl.isMainProcess(context)) {
        Log.e("MiPushRegistar", "register not in main process, return");
        return;
      }

      if(checkDevice(context)) {
        Log.e("accs.MiPushRegistar", "register begin");
        Runnable var3 =
            new Runnable() {
          public void run() {
            XiaomiPushRegister.registerMiPush(var1, var2,context);
          }
        };
        (new Thread(var3)).start();
      }
    } catch (Throwable var4) {
      Log.d("accs.MiPushRegistar", "register error: " + var4.getMessage());
    }
  }

  public static void registerMessageCallback(XiaomiPushCallback xiaomiPushCallback){
     mXiaomiPushCallback = xiaomiPushCallback;
  }

  public static boolean isMessageCbExist(){
     return mXiaomiPushCallback != null;
  }

  public static void excuteMessageCallback(String message){
     if(mXiaomiPushCallback != null){
        mXiaomiPushCallback.onMessage(message);
     }
  }



  private static void registerMiPush(String var0, String var1, Context var2) {
    try {
      MiPushClient.registerPush(var2, var0, var1);
    } catch (Throwable var4) {
      Log.e("accs.MiPushRegistar", "registerMiPush handleRegistrar error=" + var4);
    }

  }

}
