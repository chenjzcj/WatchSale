package com.ran.watchsale;

import android.os.Environment;

/**
 * Created by MZIA(527633405@qq.com) on 2016/8/31 0031 11:26
 * 常量值
 */
public class Constants {

    //销量统计接口
    //彩虹桥服务接口
    //public static final String WATCHSALE_URL = "http://bg.gulaike.com:8889/watch/saleStat.mo";
    //关爱未来服务接口
    public static final String WATCHSALE_URL = "http://60.205.149.120:8889/watch/saleStat.mo";

    public static final String FILE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/watchsales";
    public static final String FILE_PATH = FILE_DIR + "/sales.txt";
}
