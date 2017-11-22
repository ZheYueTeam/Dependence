package cn.fuyoushuo.pushlib.register;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import cn.fuyoushuo.pushlib.ext.Base64;
import cn.fuyoushuo.pushlib.receiver.BringToFrontReceiver;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.utl.UtilityImpl;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by QA on 2017/9/21.
 */

public class UmengPushRegister {

   public static final String TAG = "UmengPushRegister";

   private static UmengMessageHandler umengMessageHandler;

   private static PushAgent pushAgent;

   private static UmengPushCallback mUmengPushCallback;

   public interface UmengPushCallback{
     void onMessage(String message);
   }

   public static void setUmengMessageHandler(UmengMessageHandler umengMessageHandler) {
     UmengPushRegister.umengMessageHandler = umengMessageHandler;
   }

  public static void register(final Context context){
    try{
        pushAgent = PushAgent.getInstance(context);
        if(umengMessageHandler != null){
          pushAgent.setMessageHandler(umengMessageHandler);
        }
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
          @Override
          public void dealWithCustomAction(Context context, UMessage msg) {
              String packageName = context.getPackageName();
              String content = msg.custom;
              try{
                if(mUmengPushCallback != null){
                 Intent broadCast = new Intent();
                 broadCast.setAction(BringToFrontReceiver.ACTION_BRING_TO_FRONT);
                 context.sendBroadcast(broadCast);
                 mUmengPushCallback.onMessage(content);
                }else{
                  if(!TextUtils.isEmpty(packageName)){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vm://"+packageName+"/messageDetail?content="+ Base64.encodeToString(content.getBytes("utf-8"),false)));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                  }
               }
              }catch (Exception e){
                Log.e(TAG,"excute error:" + e.getMessage());
              }
          }
        };
        pushAgent.setNotificationClickHandler(notificationClickHandler);
        pushAgent.register(new IUmengRegisterCallback() {
          @Override
          public void onSuccess(String deviceToken) {
            Log.d(TAG, "umeng device token: " + deviceToken);
          }

          @Override
          public void onFailure(String s, String s1) {
            Log.d(TAG, "umeng register failed: " + s + " " +s1);
          }
        });
    }catch (Exception e){}
  }

  public static void registerMessageCallback(UmengPushCallback umengPushCallback){
    mUmengPushCallback = umengPushCallback;
  }

  public static void excuteMessageCallback(String message){
    if(mUmengPushCallback != null){
      mUmengPushCallback.onMessage(message);
    }
  }
}
