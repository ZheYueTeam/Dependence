package cn.fuyoushuo.pushlib.receiver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import cn.fuyoushuo.pushlib.ext.Base64;
import cn.fuyoushuo.pushlib.register.XiaomiPushRegister;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.json.JSONObject;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 MiPushMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.MiPushMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、MiPushMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、MiPushMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、MiPushMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、MiPushMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、MiPushMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author mayixiang
 */
public class MiPushMessageReceiver extends PushMessageReceiver {

    private static final String TAG = "MiPushMessageReceiver";
    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        Log.e(TAG, "onReceivePassThroughMessage is called. " + message.toString());
        Log.e(TAG, "onReceivePassThroughMessage: " + message.getContent() );

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        Log.e(TAG, "onNotificationMessageClicked is called. " + message.toString());
        Log.e(TAG, "onNotificationMessageClicked: " + message.getContent() );
        String packageName = context.getPackageName();
        Log.e(TAG, "packageName: " + packageName);
        String content = message.getContent();
        boolean isMessageCbExist = XiaomiPushRegister.isMessageCbExist();
        try{
          if(!isMessageCbExist){
            Log.e(TAG, "packageName: " + packageName);
            if(!TextUtils.isEmpty(packageName)){
              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vm://"+packageName+"/messageDetail?content="+ Base64.encodeToString(content.getBytes("utf-8"),false)));
              intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(intent);
             }
            }else{
              Intent broadCast = new Intent();
              broadCast.setAction(BringToFrontReceiver.ACTION_BRING_TO_FRONT);
              context.sendBroadcast(broadCast);
              XiaomiPushRegister.excuteMessageCallback(content);
            }
        }catch (Exception e){
            Log.e(TAG, "excute_error: " + e.getMessage());
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        Log.e(TAG, "onNotificationMessageArrived is called. " + message.toString());
        Log.e(TAG, "onNotificationMessageArrived: " + message.getContent() );

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        //Log.v(DemoApplication.TAG,
        //        "onCommandResult is called. " + message.toString());
        //String command = message.getCommand();
        //List<String> arguments = message.getCommandArguments();
        //String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        //String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        //String log;
        //if (MiPushClient.COMMAND_REGISTER.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mRegId = cmdArg1;
        //        log = context.getString(R.string.register_success);
        //    } else {
        //        log = context.getString(R.string.register_fail);
        //    }
        //} else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mAlias = cmdArg1;
        //        log = context.getString(R.string.set_alias_success, mAlias);
        //    } else {
        //        log = context.getString(R.string.set_alias_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mAlias = cmdArg1;
        //        log = context.getString(R.string.unset_alias_success, mAlias);
        //    } else {
        //        log = context.getString(R.string.unset_alias_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mAccount = cmdArg1;
        //        log = context.getString(R.string.set_account_success, mAccount);
        //    } else {
        //        log = context.getString(R.string.set_account_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mAccount = cmdArg1;
        //        log = context.getString(R.string.unset_account_success, mAccount);
        //    } else {
        //        log = context.getString(R.string.unset_account_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mTopic = cmdArg1;
        //        log = context.getString(R.string.subscribe_topic_success, mTopic);
        //    } else {
        //        log = context.getString(R.string.subscribe_topic_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mTopic = cmdArg1;
        //        log = context.getString(R.string.unsubscribe_topic_success, mTopic);
        //    } else {
        //        log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
        //    }
        //} else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
        //    if (message.getResultCode() == ErrorCode.SUCCESS) {
        //        mStartTime = cmdArg1;
        //        mEndTime = cmdArg2;
        //        log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
        //    } else {
        //        log = context.getString(R.string.set_accept_time_fail, message.getReason());
        //    }
        //} else {
        //    log = message.getReason();
        //}
        //MainActivity.logList.add(0, getSimpleDate() + "    " + log);
        //
        //Message msg = Message.obtain();
        //msg.obj = log;
        //DemoApplication.getHandler().sendMessage(msg);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.e(TAG, "onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = "Register push success.";
            } else {
                log = "Register push fail.";
            }
        } else {
            log = message.getReason();
        }
        Log.e(TAG, "onReceiveRegisterResult:  log " + log );
    }

    @SuppressLint("SimpleDateFormat")
    private static String getSimpleDate() {
        return new SimpleDateFormat("MM-dd hh:mm:ss").format(new Date());
    }

}
