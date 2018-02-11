package com.developer.pritampc.imageuploader;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by pritamPC on 2/9/2018.
 */

public interface ApiInterface {

        @Multipart
        @POST("fileupload.php")
        Call<UploadStatus> upload_image(@Part MultipartBody.Part  image, @Part("name") RequestBody name);
}
