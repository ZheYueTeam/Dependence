package cn.fuyoushuo.share;

import android.content.Context;
import com.umeng.socialize.UMShareListener;

/**
 * 描述:                    <br>
 * 作者:Stark.Tony          <br>
 * 日期:2017/6/19             <br>
 */

public class ShareHelper {

  private UMShareListener mShareListener;
  private Context mContext;

  public ShareHelper with(Context context) {
    this.mContext = context;
    return this;
  }



  //private static class CustomShareListener implements UMShareListener {
  //
  //  private WeakReference<ShareHelper> mHelperWeakReference;
  //
  //  private CustomShareListener(ShareHelper shareHelper) {
  //    mHelperWeakReference = new WeakReference(shareHelper);
  //  }
  //
  //  @Override public void onStart(SHARE_MEDIA platform) {
  //
  //  }
  //
  //  @Override public void onResult(SHARE_MEDIA platform) {
  //
  //    if (platform.name().equals("WEIXIN_FAVORITE")) {
  //      Toast.makeText(mContext, platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
  //    } else {
  //      if (platform != SHARE_MEDIA.MORE
  //          && platform != SHARE_MEDIA.SMS
  //          && platform != SHARE_MEDIA.EMAIL
  //          && platform != SHARE_MEDIA.FLICKR
  //          && platform != SHARE_MEDIA.FOURSQUARE
  //          && platform != SHARE_MEDIA.TUMBLR
  //          && platform != SHARE_MEDIA.POCKET
  //          && platform != SHARE_MEDIA.PINTEREST
  //
  //          && platform != SHARE_MEDIA.INSTAGRAM
  //          && platform != SHARE_MEDIA.GOOGLEPLUS
  //          && platform != SHARE_MEDIA.YNOTE
  //          && platform != SHARE_MEDIA.EVERNOTE) {
  //        Toast.makeText(mContext, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
  //      }
  //    }
  //  }
  //
  //  @Override public void onError(SHARE_MEDIA platform, Throwable t) {
  //    if (platform != SHARE_MEDIA.MORE
  //        && platform != SHARE_MEDIA.SMS
  //        && platform != SHARE_MEDIA.EMAIL
  //        && platform != SHARE_MEDIA.FLICKR
  //        && platform != SHARE_MEDIA.FOURSQUARE
  //        && platform != SHARE_MEDIA.TUMBLR
  //        && platform != SHARE_MEDIA.POCKET
  //        && platform != SHARE_MEDIA.PINTEREST
  //
  //        && platform != SHARE_MEDIA.INSTAGRAM
  //        && platform != SHARE_MEDIA.GOOGLEPLUS
  //        && platform != SHARE_MEDIA.YNOTE
  //        && platform != SHARE_MEDIA.EVERNOTE) {
  //      Toast.makeText(mContext, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
  //      if (t != null) {
  //
  //      }
  //    }
  //  }
  //
  //  @Override public void onCancel(SHARE_MEDIA platform) {
  //    Toast.makeText(mContext, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
  //  }
  //}
}
