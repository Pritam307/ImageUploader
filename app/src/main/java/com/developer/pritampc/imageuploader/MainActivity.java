package com.developer.pritampc.imageuploader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.ipaulpro.afilechooser.utils.FileUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {

    private ImageView image;
    private Button choose,upload;
    private ApiInterface apiInterface;
    UploadStatus status;
    private EditText name;
    private File filepath;
    private static final int CODE=100;
    private static final String TAG="Main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        choose=findViewById(R.id.img_choose);
        upload=findViewById(R.id.img_upload);
        image=findViewById(R.id.imageView);
        name=findViewById(R.id.name);
    }


    public void OnImageChoose(View v)
    {
        RxPermissions permissions=new RxPermissions(MainActivity.this);
        permissions.request(Manifest.permission.INTERNET,Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted->{
                    if(granted)
                    {
                        Intent i=new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i,CODE);

                    }else {
                        Toast.makeText(getApplicationContext(),"Permission not granted!",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void OnUpload(View v)
    {
        String img_name=name.getText().toString();
        RequestBody requestBody=RequestBody.create(MediaType.parse("image/*"),filepath);
        MultipartBody.Part uploadimg=MultipartBody.Part.createFormData("image",img_name,requestBody);
        RequestBody file_name=RequestBody.create(MediaType.parse("text/plain"),img_name);

        apiInterface=ApiClient.getClient().create(ApiInterface.class);

        Call<UploadStatus> call= apiInterface.upload_image(uploadimg,file_name);
        call.enqueue(new Callback<UploadStatus>() {
            @Override
            public void onResponse(Call<UploadStatus> call, Response<UploadStatus> response) {
                status=response.body();
                Toast.makeText(getApplicationContext(), status.getResponse(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<UploadStatus> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"failed",Toast.LENGTH_LONG).show();
            }
        });

        image.setImageBitmap(null);
        name.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CODE && resultCode==RESULT_OK && data!=null)
        {
            Uri uri=data.getData();
            Log.d(TAG, "onActivityResult: uri"+uri);
            filepath= FileUtils.getFile(getApplicationContext(),uri);
            Log.d(TAG, "onActivityResult: file path "+filepath.getName());
           // filepath=new File(file_path);
            try {
                Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                image.setImageBitmap(bitmap);
                name.setVisibility(View.VISIBLE);
                upload.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
