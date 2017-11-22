package cn.fuyoushuo.pushlib.register;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import com.huawei.android.pushagent.PushManager;
import com.taobao.accs.utl.UtilityImpl;
import java.util.Collections;
import java.util.Map;

/**
 * Created by QA on 2017/9/20.
 */

public class HuaweiPushRegister {

  private static final String TAG = "HuaweiPushRegister";
  private static String phoneBrand;

  public HuaweiPushRegister() {
  }

  public static boolean checkDevice(Context var0) {
    boolean var1 = false;
    if(phoneBrand.equalsIgnoreCase("huawei") || phoneBrand.equalsIgnoreCase("honor")) {
      var1 = true;
    }

    return var1;
  }

  private static PushCallback mPushCallback;

  public interface PushCallback{
    void onMessage(String message);
  }

  public static void registerMessageCallback(PushCallback xiaomiPushCallback){
    mPushCallback = xiaomiPushCallback;
  }

  public static boolean isMessageCbExist(){
    return mPushCallback != null;
  }

  public static void excuteMessageCallback(String message){
    if(mPushCallback != null){
      mPushCallback.onMessage(message);
    }
  }
  public static void register(final Context var0, final String tagKey) {
    try {
      if(!UtilityImpl.isMainProcess(var0)) {
        Log.e(TAG,"register not in main process, return");
        return;
      }
      if(checkDevice(var0)) {
        Runnable var1 = new Runnable() {
          public void run() {
            Log.i(TAG, "huawei register begin");
            HuaweiPushRegister.registerHuawei(var0,tagKey);
          }
        };
        (new Thread(var1)).start();
      } else {
        Log.e(TAG, "checkDevice false");
      }
    } catch (Throwable var2) {
      Log.e(TAG, var2.getMessage());
    }
  }

  private static void registerHuawei(Context context,String tagKey){
    try{
      PushManager.requestToken(context);
      PushManager.requestPushState(context);
      PushManager.enableReceiveNormalMsg(context,true);
      PushManager.enableReceiveNotifyMsg(context,true);
      com.huawei.android.pushagent.api.PushManager.enableFeature(context,
          com.huawei.android.pushagent.api.PushManager.PushFeature.LOCATION_BASED_MESSAGE, true);
      Map<String, String> tagmap = Collections.singletonMap(tagKey,tagKey+"-all");
      PushManager.setTags(context,tagmap);
    }catch (Exception e){
      Log.e(TAG,e.getMessage());
    }
  }

  static {
    phoneBrand = Build.BRAND;
  }



}
