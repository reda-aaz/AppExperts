package com.reda.mytraining.gitreposearch.model.network;

import com.reda.mytraining.gitreposearch.model.data.Repository;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

import static com.reda.mytraining.gitreposearch.util.Constants.GET_REPOSITORIES;
import static com.reda.mytraining.gitreposearch.util.Constants.USER_PATH;

public interface GitServiceInterface {
    @GET(GET_REPOSITORIES)
    Observable<List<Repository>> getUserGitRepository(@Path(USER_PATH) String gitUserName);
}
