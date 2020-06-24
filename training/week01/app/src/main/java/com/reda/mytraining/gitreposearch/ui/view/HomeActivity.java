package com.reda.mytraining.gitreposearch.ui.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

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

    @BindView(R.id.repository_recyclerview)
    RecyclerView repositoryRecyclerView;

    @BindView(R.id.git_repo_edittext)
    EditText gitRepoEditText;

    @BindView(R.id.git_search_button)
    Button gitSearchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        repositoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repositoryRecyclerView.setAdapter(gitAdapter);

        gitViewModel = ViewModelProviders.of(this).get(GitViewModel.class);

        repositoryObserver = this::updateRepositories;

        gitViewModel.getRepos().observe(this, repositoryObserver);


        gitRepoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                DO nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //gitViewModel.getResults(s.toString().trim()).observe(HomeActivity.this, repositoryObserver);
            }

            @Override
            public void afterTextChanged(Editable s) {
//              Do nothing
            }
        });

        gitSearchButton.setOnClickListener(v -> {
                String gitUserName = gitRepoEditText.getText().toString();
                if( !gitUserName.isEmpty()) {
                    gitViewModel.getResults(gitUserName.trim()).observe(HomeActivity.this, repositoryObserver);
                }
        });
    }
    private void updateRepositories(List<Repository> repositories) {
        gitAdapter.setRepositoryList(repositories);
    }
}
