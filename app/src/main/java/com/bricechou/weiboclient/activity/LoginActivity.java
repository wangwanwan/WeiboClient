package com.bricechou.weiboclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bricechou.weiboclient.R;
import com.bricechou.weiboclient.config.Constants;
import com.bricechou.weiboclient.db.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LoginActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";

    // 微博实例
    private AuthInfo mAuthInfo;

    // 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
    private Oauth2AccessToken mAccessToken;

    // 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
    private SsoHandler mSsoHandler;

    private Button mButtonSubmit;  // 成员变量登录按钮

    // 初始化View界面中出现的id 并将view与相应变量进行赋值
    private void initView(){
        mButtonSubmit = (Button)findViewById(R.id.login_submit);
    }

    // 初始化微博操作
    private void initWeibo(){

        // 初始化微博实例
        mAuthInfo = new AuthInfo(this,Constants.APP_KEY,Constants.REDIRECT_URL,Constants.SCOPE);
        // 将微博实例绑定到SSO授权操作中
        mSsoHandler = new SsoHandler(LoginActivity.this,mAuthInfo);
        // 仅通过使用SSO WEB的方式进行登录
        mSsoHandler.authorize(new AuthListener());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mButtonSubmit.setOnClickListener(this);
    }

    /**
     * @description 监听用户的按钮点击事件
     * @author BriceChou
     * @TODO 当用户点击非登录按钮时,进行相应信息提示操作!如:提示输入怎样的用户名或者密码!
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_submit:
                initWeibo();
                break;
            default:
                break;
        }
    }

    /**
     * @description 当 SSO 授权 Activity 退出时，该函数被调用。
     * @author BriceChou
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * @description:微博ＡＰＩ规定必须添加回调页面,没有这个函数覆写将不能调用回调页面
         * 当用户取消授权登录时,SSO 授权回调
         * 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
         */
        if(mSsoHandler != null){
            Log.i("新浪微博登陆返回","返回");
            mSsoHandler.authorizeCallBack(requestCode,resultCode,data);
        }
    }

    /**
     * @description 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     * @author BriceChou
     * @datetime 16-5-30 上午11:12
     * @TODO 更改提示框的样式,现在使用的时默认样式
     */
    class AuthListener implements WeiboAuthListener{
        /**
         * 当用户取消登录,弹出取消登录提示!
         * @author BriceChou
         */
        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.toast_login_auth_canceled, Toast.LENGTH_LONG).show();
        }

       /**
        * @description 授权认证结束后将调用此方法。
        * @author BriceChou
        * @datetime 16-5-30 上午11:22
        * @TODO 我表示不能理解这个函数Bundle的操作?
        */
        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            // 从这里获取用户输入的 电话号码信息
            // String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(LoginActivity.this, mAccessToken);
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                Toast.makeText(LoginActivity.this,
                        R.string.toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        /**
         * @description 提示登录错误信息
         * @author BriceChou
         * @datetime 16-5-30 上午11:31
         * @TODO 考虑登录错误后,进行其他操作
         * @param e
         */

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}

