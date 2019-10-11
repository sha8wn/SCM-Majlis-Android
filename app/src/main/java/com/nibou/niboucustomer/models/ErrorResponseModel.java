package com.nibou.niboucustomer.models;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class ErrorResponseModel implements Serializable {

    @SerializedName("error")
    private int error;

    @SerializedName("error_text")
    private String error_text;

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getError_text() {
        return error_text;
    }

    public void setError_text(String error_text) {
        this.error_text = error_text;
    }
}
