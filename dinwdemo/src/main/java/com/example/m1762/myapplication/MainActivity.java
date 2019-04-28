package com.example.m1762.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;

import java.io.File;
import java.io.RandomAccessFile;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private MyLocationl myLocationl = new MyLocationl();
    LocationClientOption option = new LocationClientOption();
    private Button btn;
    private final int SDK_PERMISSION_REQUEST = 127;
    private String permissionInfo;
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    private NotifyLister mNotifyLister;
    private TextView viewById;
    private ListView lv;
    private Vibrator mVibrator;
    private double longtitude,latitude;
    private float radius;
    private AlarmBroadcastReceiver alarmBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button)findViewById(R.id.btn);
//        viewById = (TextView) findViewById(R.id.tv1);
//        viewById = (TextView) findViewById(R.id.tv3);
        lv = findViewById(R.id.lv);
        btn.setOnClickListener(this);
        mLocationClient = new LocationClient(getApplicationContext());
        mNotifyLister = new NotifyLister();
//        mLocationClient.registerNotify(mNotifyLister);
        //声明LocationClient类
        mLocationClient.registerLocationListener(myLocationl);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        /*
        bd09ll - 百度经纬度坐标；
        gcj02 - 国测局经纬度坐标；
        BD09 - 百度墨卡托坐标；
        WGS84 - 海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标
        * */
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);//180000
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        option.setWifiCacheTimeOut(5*60*1000);
        option.setEnableSimulateGps(false);
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
//        option.setOpenAutoNotifyMode(30000,20, LocationClientOption.LOC_SENSITIVITY_HIGHT);
//        option.setOpenAutoNotifyMode();
        mLocationClient.setLocOption(option);
        //通过时间唤醒
//        BootRece  iver receiver = new BootReceiver();
//        registerReceiver(receiver,new IntentFilter(Intent.ACTION_TIME_TICK));
        alarmBroadcastReceiver = new AlarmBroadcastReceiver();
        IntentFilter df = new IntentFilter();
        df.addAction("testalarm0");
        registerReceiver(alarmBroadcastReceiver,df);
        //通过闹钟唤醒
//        sendAlarmEveryday1(this);
        getPersimmions();
    }
    @Override
    protected void onResume() {
        super.onResume();
        ht();

    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn){
            mLocationClient.start();
//            initLocationOption();
        }
    }
    private void sendAlarmEveryday1(Context context){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar =Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Intent intent = new Intent();
        intent.setAction("testalarm0");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,0, intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),180000,pendingIntent);
    }
    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            /***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             */
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            /*
             * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
             */
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onPause() {
        super.onPause();
//        ht();
    }
    //后台定位
    public void ht(){
        Log.e("luosuihan", "后台定位");
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("适配android 8限制后台定位功能", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(MainActivity.this.getApplicationContext());
            Intent nfIntent = new Intent(MainActivity.this.getApplicationContext(), MainActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(MainActivity.this.getApplicationContext(), 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        mLocationClient.enableLocInForeground(1, notification);
    }
    private MyAdapter myAdapter;
    class MyLocationl extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location){
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                final StringBuffer sb = new StringBuffer(256);
//                longtitude = location.getLongitude();
//                latitude = location.getLatitude();
//                radius = location.getRadius();
//                location.setRadius(5);
                sb.append("时间 : ");
                sb.append(location.getTime());
                sb.append("\n状态码 : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\n说明 : ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\n纬度 : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\n经度 : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\n半径 ：");
                sb.append(location.getRadius());
//                sb.append("\nCountry : ");// 国家名称
//                sb.append(location.getCountry());
//                sb.append("\ncitycode : ");// 城市编码
//                sb.append(location.getCityCode());
//                sb.append("\ncity : ");// 城市
//                sb.append(location.getCity());
//                sb.append("\nDistrict : ");// 区
//                sb.append(location.getDistrict());
//                sb.append("\nStreet : ");// 街道
//                sb.append(location.getStreet());
                sb.append("\n地址 : ");// 地址信息
                sb.append(location.getAddrStr());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n速度 : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
//                    sb.append("\n卫星 : ");
//                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\n海拔 : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\n海波 : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                Log.e("luosuihan11", "onReceiveLocation: "+sb.toString() );
//                viewById.append(sb.toString());
                 aa.add(sb.toString());
                myAdapter = new MyAdapter(MainActivity.this,aa);
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lv.setAdapter(myAdapter);
                        scrollMyListViewToBottom();
                        writeData("\n"+sb.append("\n")+"\n"+sb.toString());
                    }
                });
            }
        }
    }
    List aa = new ArrayList();
    Calendar c = Calendar.getInstance();
    private void writeData(String sj) {
//        mYear，mMonth
        int i = c.get(Calendar.DAY_OF_MONTH);//日
        int j = c.get(Calendar.YEAR);//年
        int k = c.get(Calendar.MONTH)+1;//月
        String mData = String.valueOf(i);
        String mYear = String.valueOf(j);
        String mMonth = String.valueOf(k);
        String filePath = "/sdcard/Gyt/";
        String fileName = mYear+"-"+mMonth+"-"+mData+".txt";
        writeTxtToFile(sj, filePath, fileName);

    }
    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);

        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("luosuihan", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }
    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//生成文件夹

    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }
    class NotifyLister extends BDNotifyListener {
        public void onNotify(BDLocation mlocation, float distance){
            mVibrator.vibrate(1000);//振动提醒已到设定位置附近
        }
    }
    private void scrollMyListViewToBottom() {
        lv.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                lv.setSelection(myAdapter.getCount() - 1);
            }
        });
    }
   class AlarmBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("testalarm0".equals(intent.getAction())) {
                Log.e("luosuihanzl", "onReceive: 第一个闹钟");
//                Vibrator vibrator = (Vibrator)MainActivity.this.getSystemService(MainActivity.this.VIBRATOR_SERVICE);
//                vibrator.vibrate(1000);
//                mLocationClient.start();
                //点亮屏幕
                PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
                PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
                wl.acquire();
                KeyguardManager km= (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
                kl.disableKeyguard();
                kl.reenableKeyguard();
                wl.release();
                mLocationClient.start();
            }
        }
    }
}
