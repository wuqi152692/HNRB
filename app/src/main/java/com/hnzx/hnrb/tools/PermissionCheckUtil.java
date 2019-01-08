package com.hnzx.hnrb.tools;

import android.Manifest;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

/**
 * <br>Android 6.0以上权限检查</>
 * <p>混淆设置</>
 * -keepclassmembers class ** {
 *
 * @com.yanzhenjie.permission.PermissionYes <methods>;
 * }
 * -keepclassmembers class ** {
 * @com.yanzhenjie.permission.PermissionNo <methods>;
 * }
 * Created by FoAng on 17/5/11 下午2:10;
 */

public class PermissionCheckUtil implements PermissionListener {

    public static final int PERMISSION_REQUEST_CODE = 0x09 << 1;

    private static PermissionCheckUtil checkUtil;

    private static Object mContext;

    private CheckListener checkListener;

    private PermissionCheckUtil(Object mContext) {
        if (mContext instanceof Activity || mContext instanceof Fragment) {
            this.mContext = mContext;
        } else {
            throw new IllegalArgumentException("context type is not correct, must be activity or fragment");
        }
    }

    public static PermissionCheckUtil getInstance(Object mContext) {
        if (checkUtil == null) {
            synchronized (PermissionCheckUtil.class) {
                checkUtil = new PermissionCheckUtil(mContext);
            }
        }
        return checkUtil;
    }

    /**
     * 相机权限
     */
    public static final String[] PERMISSION_CAMERA = new String[]{Manifest.permission.CAMERA};

    /**
     * 存储权限
     */
    public static final String[] PERMISSION_SD_STORAGE = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * 定位权限
     */
    public static final String[] PERMISSION_LOCATION = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * 获取手机状态权限
     */
    public static final String[] PERMISSION_PHONE_STATE = new String[]{Manifest.permission.READ_PHONE_STATE};
    /**
     * 音频权限
     */
    public static final String[] PERMISSION_AUDIO = new String[]{Manifest.permission.RECORD_AUDIO};

    /**
     * 程序一般请求权限
     */
    public static final String[] PERMISSION_COMMON = new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public interface CheckListener {
        /**
         * 权限开启
         */
        void isPermissionOn();

        /**
         * 权限关闭
         */
        void isPermissionNo();
    }


    /**
     * Activity 检测权限
     *
     * @param mActivity
     * @param listener
     * @param permissionType
     */
    public void checkPermission(final Activity mActivity, CheckListener listener, String... permissionType) {
        mContext = mActivity;
        this.checkListener = listener;
        AndPermission.with(mActivity)
                .requestCode(PERMISSION_REQUEST_CODE)
                .permission(permissionType)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(mActivity, rationale).show();
                    }
                })
                .callback(this)
                .start();
    }

    /**
     * fragment权限检查
     *
     * @param mFragment
     * @param listener
     * @param permissionType
     */
    public void checkPermission(final Fragment mFragment, CheckListener listener, String... permissionType) {
        mContext = mFragment;
        this.checkListener = listener;
        AndPermission.with(mFragment)
                .requestCode(PERMISSION_REQUEST_CODE)
                .permission(permissionType)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(mFragment.getContext(), rationale).show();
                    }
                })
                .callback(this)
                .start();
    }

    @Override
    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (checkListener != null) checkListener.isPermissionOn();
        }
    }

    @Override
    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
        // 授权失败 回调此方法
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (mContext instanceof Activity) {
                if (AndPermission.hasAlwaysDeniedPermission((Activity) mContext, deniedPermissions)) {
                    AndPermission.defaultSettingDialog((Activity) mContext, PERMISSION_REQUEST_CODE).show();
                } else {
                    if (checkListener != null) checkListener.isPermissionNo();
                }
            } else if (mContext instanceof Fragment) {
                if (AndPermission.hasAlwaysDeniedPermission((Fragment) mContext, deniedPermissions)) {
                    AndPermission.defaultSettingDialog((Fragment) mContext, PERMISSION_REQUEST_CODE).show();
                } else {
                    if (checkListener != null) checkListener.isPermissionNo();
                }
            }
        }
    }


}
