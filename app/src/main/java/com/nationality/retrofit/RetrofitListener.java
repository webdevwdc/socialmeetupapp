package com.nationality.retrofit;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by Uzibaba on 1/8/2017.
 */

public interface RetrofitListener {
    void onSuccess(Call call, Response response, String method);
    void onFailure(String errorMessage);
}
