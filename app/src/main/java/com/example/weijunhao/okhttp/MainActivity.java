package com.example.weijunhao.okhttp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button mOKHttpGetBtn, mOKHttpPostBtn, mOKHttpUploadBtn;
    private OkHttpClient mOkHttpClient;
    private TextView mTextView;

    //文件上传类型
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOKHttpGetBtn = (Button) findViewById(R.id.btn_okhttp_get);
        mOKHttpPostBtn = (Button) findViewById(R.id.btn_okhttp_post);
        mOKHttpUploadBtn = (Button) findViewById(R.id.btn_okhttp_file_upload);
        mTextView = (TextView) findViewById(R.id.textView);


        mOKHttpGetBtn.setOnClickListener(this);
        mOKHttpPostBtn.setOnClickListener(this);
        mOKHttpUploadBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_okhttp_get:
                getAsynHttp();
                break;
            case R.id.btn_okhttp_post:
                postAsynHttp();
                break;
            case R.id.btn_okhttp_file_upload:
                break;
        }
    }

    public void getAsynHttp() {
        mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("https://www.baidu.com")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str;
                if (null != response.cacheResponse()) {
                    str = response.cacheResponse().toString();
                    Log.i("wjh", "cache---" + str);
                } else {
                    response.body().string();
                    str = response.networkResponse().toString();
                    Log.i("wjh", "network---" + str);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        mTextView.setText(str + "");
                    }
                });
            }
        });
    }

    public void postAsynHttp() {
        mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("city", "北京")
                .build();
        Request request = new Request.Builder()
                .url("http://wthrcdn.etouch.cn/weather_mini")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String str = response.body().string();
                Log.i("wjh", str);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "请求成功", Toast.LENGTH_SHORT).show();
                        mTextView.setText(str);
                    }
                });
            }
        });
    }


    /**
     * 异步上传文件
     */
    public void postAsynFile() {
        mOkHttpClient = new OkHttpClient();
        File file = new File("/sdcard/wangshu.txt");
        Request request = new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wjh",response.body().string());
            }
        });
    }

    /**
     * 异步下载文件
     */
    private void downAsynFile() {
        mOkHttpClient = new OkHttpClient();
        String url = "http://img.my.csdn.net/uploads/201603/26/1458988468_5804.jpg";
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;

                fileOutputStream = new FileOutputStream(new File("/sdcard/wangshu.jpg"));
                byte[] buffer = new byte[2048];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, len);
                }
                fileOutputStream.flush();
                Log.d("wjh", "文件下载成功");
            }
        });
    }

    //异步上传Multipart文件
    private void sendMultipart() {

    }

    //设置超时时间和缓冲

    //关于取消请求和封装
}
