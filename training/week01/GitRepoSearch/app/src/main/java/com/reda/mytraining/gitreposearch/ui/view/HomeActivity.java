package com.reda.mytraining.gitreposearch.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.reda.mytraining.gitreposearch.R;
import com.reda.mytraining.gitreposearch.model.data.Repository;
import com.reda.mytraining.gitreposearch.ui.adapter.GitRepositoryAdapter;
import com.reda.mytraining.gitreposearch.viewmodel.GitViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    private GitViewModel gitViewModel;
    private Observer<List<Repository>> repositoryObserver;
    private GitRepositoryAdapter gitAdapter = new GitRepositoryAdapter(new ArrayList<>());
    private static final String TAG = "HomeActivity";

    @BindView(R.id.repository_recyclerview)
    RecyclerView repositoryRecyclerView;

    @BindView(R.id.git_repo_edittext)
    EditText gitRepoEditText;

    @BindView(R.id.git_search_button)
    Button gitSearchButton;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        repositoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryRecyclerView.setAdapter(gitAdapter);

        gitViewModel = ViewModelProviders.of(this).get(GitViewModel.class);

        repositoryObserver = this::updateRepositories;

        gitViewModel.getResults("Facebook");

        gitViewModel.getReposLiveData().observe(HomeActivity.this, repositoryObserver);

        gitViewModel.showProgressBar().observe(this, isSearching -> {
            if (isSearching) {
                progressBar.setVisibility(View.VISIBLE);
            }
            else {
                progressBar.setVisibility(View.GONE);
            }
        });

        gitSearchButton.setOnClickListener(v -> {
            String gitUserName = gitRepoEditText.getText().toString();
            if (!gitUserName.trim().isEmpty()) {
                Log.i(TAG, "gitUserName: " + gitUserName);
                gitViewModel.getResults(gitUserName.trim());
            }
        });
    }

    private void updateRepositories(List<Repository> repositories) {
        gitAdapter.setRepositoryList(repositories);
        gitViewModel.doneSearching();
    }
}
