package cn.fuyoushuo.pushlib.receiver;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.huawei.android.pushagent.api.PushEventReceiver;

public class HuaWeiPushReceiver extends PushEventReceiver {

  private static final String TAG = "HuaWeiPushReceiver";

  @Override public void onToken(Context context, String token, Bundle extras) {
    String belongId = extras.getString("belongId");
    String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
    Log.e(TAG, content);
  }

  @Override public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
    try {
      String content = "收到一条Push消息： " + new String(msg, "UTF-8");
      Log.e(TAG, content);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  public void onEvent(Context context, Event event, Bundle extras) {
    super.onEvent(context, event, extras);
    String content = "!!! onEvent 收到一条Push消息： " + extras.getString(BOUND_KEY.pushMsgKey);
    Log.e(TAG, content);
  }
}
