package com.developer.pritampc.imageuploader;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pritamPC on 2/9/2018.
 */

public class UploadStatus {
    @SerializedName("response")
    String response;

    public String getResponse() {
        return response;
    }
}
