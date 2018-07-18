package org.hjc.httplibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextPaint;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 通用方法集
 * Modified by 黄坚琮 on 2017/4/11.
 */

public class Tool {

    /*px转dp*/
    public static int px2dip(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /*px转sp*/
    public static int px2sp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (px / scale + 0.5f);
    }

    /*dp转px*/
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /*sp转px*/
    public static int sp2px(Context context, float sp) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 两个时间之间隔了几天几小时几分几秒
     *
     * @param str1   时间1
     * @param str2   时间2
     * @param format 时间格式 范例"yyyy/MM/dd HH:mm:ss"
     * @return long[4] 0为天， 1为小时， 2为分钟， 3为秒
     */
    public static long[] splitTime(String str1, String str2, String format) throws Exception {
        long[] result = new long[4];
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        long date1 = dateFormat.parse(str1).getTime();
        long date2 = dateFormat.parse(str2).getTime();
        long split = Math.abs(date1 - date2);
        result[0] = split / (24 * 60 * 60 * 1000);
        result[1] = split / (60 * 60 * 1000) - result[0] * 24;
        result[2] = split / (60 * 1000) - result[0] * 24 * 60 - result[1] * 60;
        result[3] = split / (1000) - result[0] * 24 * 60 * 60 - result[1] * 60 * 60 - result[2] * 60;
        return result;
    }

    /**
     * 检查手机格式
     *
     * @param mobile
     * @return
     */
    public static boolean checkMobile(String mobile) {
        Pattern pattern = Pattern
                .compile("^[1][3,4,5,7,8][0-9]{9}$");
        return pattern.matcher(mobile).matches();
    }

    /**
     * 检查邮箱格式
     *
     * @param mailbox
     * @return
     */
    public static boolean checkMailbox(String mailbox) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        return pattern.matcher(mailbox).matches();
    }

    /**
     * 检查身份证格式
     *
     * @param idcard
     * @return
     */
    public static boolean checkIdcard(String idcard) {
        Pattern pattern = Pattern.compile("/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/");
        return pattern.matcher(idcard).matches();
    }

    /**
     * 检查车牌号格式
     * @param carNo
     * @return
     */
    public static boolean checkCarNo(String carNo){
        Pattern pattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$");
        return pattern.matcher(carNo).matches();
    }

    /**
     * 加密
     *
     * @param source
     * @param password
     * @return
     */
    public static byte[] encrypt(byte[] source, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            /*将指定字节数组转换为秘钥*/
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            /*进行加密*/
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
            return cipher.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /*获取缩放比例*/
    public static int getZoom(Activity context, double length, int flag) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x;
        if (flag == 0) {
            x = dm.widthPixels / dm.xdpi;
        } else {
            x = dm.heightPixels / dm.ydpi;
        }
        int resultBoom = 19;
        if (length <= 20) {

        } else if (length <= 50 * x && length > 20 * x) {
            resultBoom = 18;
        } else if (length <= 100 * x && length > 50 * x) {
            resultBoom = 17;
        } else if (length <= 200 * x && length > 100 * x) {
            resultBoom = 16;
        } else if (length <= 500 * x && length > 200 * x) {
            resultBoom = 15;
        } else if (length <= 1000 * x && length > 500 * x) {
            resultBoom = 14;
        } else if (length <= 2000 * x && length > 1000 * x) {
            resultBoom = 13;
        } else if (length <= 5000 * x && length > 2000 * x) {
            resultBoom = 12;
        } else if (length <= 10000 * x && length > 5000 * x) {
            resultBoom = 11;
        } else if (length <= 25000 * x && length > 10000 * x) {
            resultBoom = 10;
        } else if (length <= 50000 * x && length > 25000 * x) {
            resultBoom = 9;
        } else if (length <= 100000 * x && length > 50000 * x) {
            resultBoom = 8;
        } else if (length <= 200000 * x && length > 100000 * x) {
            resultBoom = 7;
        } else if (length <= 500000 * x && length > 200000 * x) {
            resultBoom = 16;
        } else {
            resultBoom = 15;
        }

        return resultBoom;
    }

    /**
     * 解密
     *
     * @param src
     * @param password
     * @return
     */
    public static byte[] decrypt(byte[] src, String password) {
        try {
            SecureRandom random = new SecureRandom();
            DESKeySpec desKey = new DESKeySpec(password.getBytes());
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKey);
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
            return cipher.doFinal(src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes) {
        StringBuilder str = new StringBuilder();
        if (bytes == null || bytes.length < 0) {
            return null;
        }
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            str.append(hv).append(" ");
        }
        return str.toString();
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes
     * @return
     */
    public static String byteToHexString(byte[] bytes, int len) {
        StringBuilder str = new StringBuilder();
        if (bytes == null || bytes.length < 0) {
            return null;
        }
        for (int i = 0; i < len; i++) {
            int v = bytes[i] & 0xFF;
            String hv = Integer.toHexString(v);
            str.append(hv).append(" ");
        }
        return str.toString();
    }

    /**
     * 将指定路径的图片，按照指定的宽高进行压缩后返回
     *
     * @param path
     * @param image_maxWidth
     * @param image_maxHeight
     * @return
     */
    public static Bitmap getBitmap(String path, int image_maxWidth, int image_maxHeight) {
        /* 获得图片的宽高*/
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        /* 计算缩放比例*/
        int width = options.outWidth;
        int height = options.outHeight;
        // 定义预转换成的图片的宽度和高度
        if (width > height) {
            if (width > image_maxWidth) {
                //计算缩放比例
                options.inSampleSize = (int) (width / ((float) image_maxWidth));
            }
        } else {
            if (height > image_maxHeight) {
                //计算缩放比例
                options.inSampleSize = (int) (height / ((float) image_maxHeight));
            }
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(path, options);
        return bitmap;
    }

    /**
     * 将一个bitmap对象保存至指定文件中
     *
     * @param bitmap
     * @param file
     * @throws {@link IOException}
     */
    public static void saveBitmap(Bitmap bitmap, File file) throws IOException {
        Log.w("ico_", "file==" + file.getAbsolutePath());
        file.getParentFile().mkdirs();
        file.createNewFile();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
    }

    /**
     * 16进制字符串转byte数组
     */
    public static byte[] HexString2Bytes(String str) {
        int length = (str.length() >> 1) + str.length() % 2;
        byte[] bytes = new byte[length];
        int j = 0;
        for (int i = 0; i < length; i++) {
            char c1 = str.charAt(j++);
            if (j < str.length()) {
                char c2 = str.charAt(j++);
                bytes[i] = (byte) ((parse(c1) << 4) + parse(c2));
            } else {
                bytes[i] = (byte) ((parse(c1)));
            }
        }
        return bytes;
    }

    /**
     * 将对象转换为map
     *
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map<String, Object> objectToMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }

        Map<String, Object> map = new HashMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                map.put(field.getName(), field.get(obj));
            }
        }

        return map;
    }

    /**
     * 字符真值获取
     *
     * @param c
     */
    private static int parse(char c) {
        int result = 0;
        if (c >= 'a' && c <= 'z') {
            result = c - 'a' + 10;
        } else if (c >= 'A' && c <= 'Z') {
            result = c - 'A' + 10;
        } else if (c >= '0' && c <= '9') {
            result = c - '0';
        }
        return result;
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return Point x为宽， y为高
     */
    public static Point getWindowSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        return point;
    }

    /**
     * 从相册获取图片的意图
     * data.getData()
     *
     * @return
     */
    public static Intent getIntentByPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        return intent;
    }

    /**
     * 计算文字宽度
     *
     * @return
     */
    public static float getTextWidth(String str) {
        TextPaint paint = new TextPaint();
        return  paint.measureText(str);
    }

    /**
     * 将int类型转化为字节组
     *
     * @param i
     * @return buffer
     */
    public static byte[] int2Bytes(int i) {
        byte[] buffer = new byte[4];
        buffer[0] = (byte) (i & 0xFF);
        buffer[1] = (byte) ((i & 0xFF00) >> 8);
        buffer[2] = (byte) ((i & 0xFF0000) >> 16);
        buffer[3] = (byte) ((i & 0xFF000000) >> 24);
        return buffer;
    }

    /**
     * 从拍照获取图片的意图
     *
     * @param context
     * @param file    存储路径，若文件夹无法创建则会报该错误
     * @return
     */
    public static Intent getIntentByCamera(Context context, File file) throws IOException {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断输出目录有效性
        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                Toast.makeText(context, "文件夹创建失败，请检查SD卡！", Toast.LENGTH_LONG).show();
                throw new IOException("无法创建文件夹，file：" + file.getAbsolutePath());
            }
        }
        //存入参数
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /*将文件转为Base64字符串*/
    public static String decodeFileIntoBase64(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputStream.read(buffer);
        inputStream.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 获取当前时间的毫秒类型
     *
     * @return Long
     */
    public static Long getCurrentTimes() {
        Timestamp time = new Timestamp(new Date().getTime());
        return time.getTime();
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 将数值类型转换为IP地址
     *
     * @param ip
     * @return String
     */
    public static String int2Ip(int ip) {
        return (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + ((ip >> 24) & 0xFF);
    }

    /*将整型数组转换为字节*/
    public static byte getByte(List<Integer> selections) {
        int result = 0;
        for (int i = 0; i < selections.size(); i++) {
            result = result | (1 << selections.get(i));
        }
        return (byte) result;
    }

    /**
     * 将字节拆分为2进制数组
     *
     * @param num
     * @return
     */
    public static int[] getBinaryArray(byte num) {
        int[] nums = new int[8];
        nums[0] = num & 1;
        nums[1] = (num & 2) >> 1;
        nums[2] = (num & 4) >> 2;
        nums[3] = (num & 8) >> 3;
        nums[4] = (num & 16) >> 4;
        nums[5] = (num & 32) >> 5;
        nums[6] = (num & 64) >> 6;

        return nums;
    }


    /**
     * 将字节组转换为mac地址
     *
     * @param bytes
     * @return
     */
    public static String mqttBytes2Mac(byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byte2Int16(bytes[bytes.length - 1 - i]));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 将单字节转化为16进制
     *
     * @param buffer
     * @return
     */
    public static String byte2Int16(byte buffer) {
        String str = Integer.toString(buffer & 0xff, 16).toString().toUpperCase();
        return str.length() == 1 ? 0 + str : str;
    }

    /**
     * 将mac地址转换为字节组
     *
     * @param mac
     * @return
     */
    public static byte[] mqttMac2Bytes(String mac) {
        if (mac.equals("")) {
            return new byte[0];
        }
        byte[] buffer = new byte[4];
        for (int i = 0; i < 4; i++) {
            buffer[3 - i] = hexstr2Byte(mac.substring(i * 2, i * 2 + 2));
        }
        return buffer;
    }

    public static byte[] hexStr2Bytes(String hexStr) {
        if (hexStr == null || hexStr.equals("")) {
            return new byte[0];
        }
        if (hexStr.length() / 2 != 0) {
            hexStr = "0" + hexStr;
        }
        byte[] bytes = new byte[hexStr.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = hexstr2Byte(hexStr.substring(i * 2, i * 2 + 2));
        }
        return bytes;
    }

    /**
     * 将16进制字符串转换为byte
     *
     * @param hexStr
     * @return
     */
    public static byte hexstr2Byte(String hexStr) {
        char[] chars = hexStr.toUpperCase().toCharArray();
        byte[] bytes = new byte[chars.length];
        for (int i = 0; i < chars.length; i++) {
            bytes[i] = (byte) "0123456789ABCDEF".indexOf(chars[i]);
        }
        byte buffer = 0;
        if (bytes.length == 2) {
            buffer = (byte) (((bytes[0] << 4) & 0xf0) | (bytes[1]));
        } else {
            buffer = bytes[0];
        }
        return buffer;
    }

    /**
     * 将mac地址转换为字节组
     *
     * @param mac
     * @return
     */
    public static byte[] mac2Bytes(String mac) {
        if (mac.equals("")) {
            return new byte[0];
        }
        String[] _mac = mac.split(":");
        byte[] buffer = new byte[_mac.length];
        for (int i = 0; i < _mac.length; i++) {
            buffer[i] = hexstr2Byte(_mac[i]);
        }
        return buffer;
    }


    /**
     * 生成一个小于等于X,大于0的值
     *
     * @param x
     * @return
     */
    public static int clacLe(int x) {
        Random random = new Random();
        random.setSeed(new Date().getTime());
        int num = random.nextInt(x) + 1;
        return num;
    }

    /**
     * 将字节组转换为mac地址
     *
     * @param bytes
     * @return
     */
    public static String bytes2Mac(byte... bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byte2Int16(bytes[i]));
            if (i != bytes.length - 1) {
                sb.append(":");
            }
        }
        return sb.toString().toUpperCase();
    }

}
