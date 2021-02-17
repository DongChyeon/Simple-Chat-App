package com.dongchyeon.simplechatapp.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("result")
    @Expose
    private Integer result;

    @SerializedName("imageUri")
    @Expose
    private String imageUri;

    public Integer getResult() { return result; }

    public void setResult(Integer result) { this.result = result; }

    public String getImageUri() { return imageUri; }

    public void setImageUri(String imageUri) { this.imageUri = imageUri; }
}
