package com.developer.pritampc.imageuploader;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pritamPC on 2/9/2018.
 */

public class ApiClient {
    private static final String base_url="http://10.0.2.2/uploadimageretrofit/";

    private static Retrofit retrofit=null;

    public static Retrofit getClient()
    {
        if(retrofit==null)
        {
            retrofit=new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
