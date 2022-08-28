package com.ran.watchsale;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.ran.watchsale.bean.SmsInfo;
import com.ran.watchsale.utils.FileUtil;
import com.ran.watchsale.utils.SMSUtils;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Administrator
 */
public class MainService extends Service {
    private static final String TAG = "MainService";
    public static SmsObserver mSmsObserver;

    private final SimpleDateFormat mFullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private final HttpUtils mHttpUtils = new HttpUtils();
    public static ServiceNotify mServiceNofity;

    @Override
    public void onCreate() {
        super.onCreate();
        boolean isCreate = new File(Constants.FILE_DIR).mkdirs();
        Log.w(TAG, "onCreate() FILE_PATH=" + Constants.FILE_DIR + " isCreate=" + isCreate);
        mSmsObserver = new SmsObserver(new Handler());
        getContentResolver().registerContentObserver(SMSUtils.URL_SMS, true, mSmsObserver);
    }

    public class SmsObserver extends ContentObserver {

        public SmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // 每当有新短信到来时，使用我们获取短消息的方法
            if (SMSUtils.getSmsFromPhone(MainService.this) != null)
                httpUploadSales(SMSUtils.getSmsFromPhone(MainService.this));
        }
    }

    /**
     * 上传短信信息到服务器
     *
     * @param infos 短信信息列表
     */
    private void httpUploadSales(final List<SmsInfo> infos) {
        if (infos == null || infos.size() == 0) {
            return;
        }
        Log.w(TAG, "httpUploadSales infos size=" + infos.size());
        for (int i = 0; i < infos.size(); i++) {
            final SmsInfo info = infos.get(i);
            RequestParams params = new RequestParams();
            params.addQueryStringParameter("watchId", String.valueOf(info.getWatchId()));
            params.addQueryStringParameter("number", info.getPhoneNumber());
            try {
                mHttpUtils.send(HttpMethod.GET, Constants.WATCHSALE_URL, params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseinfo) {
                        try {
                            if (responseinfo != null) {
                                JSONObject json = new JSONObject(responseinfo.result);
                                String str = "WatchId:" + info.getWatchId() + " PhoneNumber:" + info.getPhoneNumber() + " Date:" + mFullDateFormat.format(new Date(info.getDate())) + " Upload state:" + json.getBoolean("success");
                                FileUtil.insertStringInFile(new File(Constants.FILE_PATH), 0, str);
                                if (mServiceNofity != null) {
                                    mServiceNofity.onMainServiceUploadSuccess(info, str);
                                }
                                if (json.getBoolean("success")) {
                                    SMSUtils.deleteSmsById(MainService.this, info.getId());
                                }
                                Log.w(TAG, "httpUploadSales onSuccess info=" + info.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(HttpException httpexception, String s) {
                        Log.i("test", s);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addServiceNofity(ServiceNotify serviceNofity) {
        mServiceNofity = serviceNofity;
    }

    public static void removeServiceNofity() {
        mServiceNofity = null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.w(TAG, "onStartCommand()");
        httpUploadSales(SMSUtils.getSmsFromPhone(MainService.this));
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.w(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.w(TAG, "onBind()");
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w(TAG, "onDestroy(), action=startServiec");
        getContentResolver().unregisterContentObserver(mSmsObserver);
        startService(new Intent(this, MainService.class));
    }

    public interface ServiceNotify {
        /**
         * 上传成功回调
         *
         * @param info SmsInfo
         * @param text String
         */
        void onMainServiceUploadSuccess(SmsInfo info, String text);
    }
}
