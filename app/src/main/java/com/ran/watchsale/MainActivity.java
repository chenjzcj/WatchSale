package com.ran.watchsale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

import com.ran.watchsale.adapter.WatchAdapter;
import com.ran.watchsale.bean.SmsInfo;
import com.ran.watchsale.bean.Watch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity implements MainService.ServiceNotify {

    public static final String KEY_CREATE = "KEY_CREATE";
    private final List<Watch> watches = new ArrayList<>();
    private WatchAdapter watchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, MainService.class);
        intent.putExtra(KEY_CREATE, true);
        startService(intent);

        ListView lvWatchs = (ListView) findViewById(R.id.lv_watchs);
        watchAdapter = new WatchAdapter(this, watches);
        lvWatchs.setAdapter(watchAdapter);

        readLogToView();
        MainService.addServiceNofity(this);
    }

    /**
     * 从文件中读取数据
     */
    private void readLogToView() {
        FileReader fileReader = null;
        BufferedReader reader = null;
        try {
            watches.clear();
            fileReader = new FileReader(new File(Constants.FILE_PATH));
            reader = new BufferedReader(fileReader);
            int line = 1;
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = reader.readLine()) != null && line < 100) {
                sb.append(str).append("<br/>");
                parseStr(str);
                line++;
            }
            watchAdapter.setWatches(watches);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
                if (fileReader != null)
                    fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void parseStr(String str) {
        int i = str.indexOf("WatchId:");
        int i1 = str.indexOf(" PhoneNumber:");
        int i2 = str.indexOf(" Date:");
        int i3 = str.indexOf(" Upload state:");
        String id = str.substring(i + 8, i1);
        String number = str.substring(i1 + 13, i2);
        String time = str.substring(i2 + 6, i3);
        String state = str.substring(i3 + 14, str.length());
        watches.add(new Watch(Long.parseLong(id), number, time, Boolean.parseBoolean(state)));
        watchAdapter.setWatches(watches);
        Log.i("aaaaaaaaaa", id + ":" + number + ":" + time + ":" + state);
    }

    /**
     * 当按下返回键的时候,回到桌面而不退出应用
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            this.startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onMainServiceUploadSuccess(SmsInfo info, String text) {
        readLogToView();
    }

    @Override
    protected void onDestroy() {
        Log.w("MainActivity", "onDestroy");
        // 保证service能在后台运行，不被杀死
        startService(new Intent(this, MainService.class));
        MainService.removeServiceNofity();
        super.onDestroy();
    }
}
