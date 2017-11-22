package cn.fuyoushuo.pushlib.receiver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import cn.fuyoushuo.pushlib.ext.Base64;
import cn.fuyoushuo.pushlib.register.HuaweiPushRegister;

/**
 * Created by Liuxl on 2017/9/22.
 */

public class HWReceiveActivity extends Activity {
    private final String TAG = "HWReceiveActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Log.d(TAG, "oncreate " + getIntent().getDataString());
            String packageName = getPackageName();
            String scheme = "hw://vb/ph?";
            String data = getIntent().getDataString();
            String content = data.substring(scheme.length());
            Log.d(TAG, "content: " + content);
            boolean isMessageCbExist = HuaweiPushRegister.isMessageCbExist();
            try {
                if (!isMessageCbExist) {
                    if (!TextUtils.isEmpty(packageName)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vm://" + packageName + "/messageDetail?content=" + Base64.encodeToString(content.getBytes("utf-8"), false)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                } else {
                    HuaweiPushRegister.excuteMessageCallback(content);
                }
            } catch (Exception e) {
                Log.d(TAG, "excute_error: " + e.getMessage());
            }
            finish();
        } catch (Exception igored) {
            Log.e(TAG, "Receive message error!!");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("HWReceiveActivity","onResume "+getIntent().getDataString());
    }
}
