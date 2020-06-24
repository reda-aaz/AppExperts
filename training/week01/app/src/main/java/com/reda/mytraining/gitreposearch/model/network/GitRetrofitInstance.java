package com.reda.mytraining.gitreposearch.model.network;

import com.reda.mytraining.gitreposearch.model.data.Repository;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.reda.mytraining.gitreposearch.util.Constants.BASE_URL;

public class GitRetrofitInstance {
    private static GitRetrofitInstance gitRetrofitInstance= null;
    private static GitServiceInterface gitService ;

    private GitRetrofitInstance(){
        gitService = new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(GitServiceInterface.class);
    }

    public static GitRetrofitInstance getRetrofitInstance(){
        if(gitRetrofitInstance == null){
            gitRetrofitInstance = new GitRetrofitInstance();
        }
        return gitRetrofitInstance;
    }

    public static Observable<List<Repository>> getUserGitRepository(String userName){
        return gitService.getUserGitRepository(userName);
    }

}
