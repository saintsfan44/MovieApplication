package com.example.movieapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.movieapplication.Adapters.MovieSearchAdapter;
import com.example.movieapplication.Adapters.PersonSearchAdapter;
import com.example.movieapplication.Client.RetrofitClient;
import com.example.movieapplication.Interface.RetrofitService;
import com.example.movieapplication.Model.MovieResponse;
import com.example.movieapplication.Model.MovieResponseResults;
import com.example.movieapplication.Model.PersonResponse;
import com.example.movieapplication.Model.PersonResponseResults;
import com.google.gson.Gson;

import org.angmarch.views.BuildConfig;
import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private NiceSpinner sourceSpinner;
    private EditText queryEditText;

    private Button querySearchButton;

    private RecyclerView resultRecyclerView;

    private String movie = "By Movie Title";
    private String actor = "By Actor";

    private RetrofitService retrofitService;

    private MovieSearchAdapter movieSearchAdapter;
    private PersonSearchAdapter personSearchAdapter;

    String TheMovieDBApiKey = "aa609b9862257ded0175f57d56a10208";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sourceSpinner = findViewById(R.id.source_spinner);
        queryEditText = findViewById(R.id.query_edit_text);
        querySearchButton = findViewById(R.id.query_search_button);
        resultRecyclerView = findViewById(R.id.results_recycler_view);

        resultRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        Paper.init(this);

        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);

        final ArrayList<String> category = new ArrayList<>();

        category.add(movie);
        category.add(actor);

        sourceSpinner.attachDataSource(category);

        if(Paper.book().read("position") != null){
            int position = Paper.book().read("position");

            sourceSpinner.setSelectedIndex(position);
        }

        int position = sourceSpinner.getSelectedIndex();

        if(position == 0){
            queryEditText.setHint("Enter any movie title...");
        }else{
            queryEditText.setHint("Enter any actor name...");
        }

        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(position == 0){
                    queryEditText.setHint("Enter any movie title...");
                }else{
                    queryEditText.setHint("Enter any actor name...");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(Paper.book().read("cachce") != null){
            String results = Paper.book().read("cachce");

            if(Paper.book().read("source") != null){
                String source = Paper.book().read("source");

                if(source.equals("movie")){
                    MovieResponse movieResponse = new Gson().fromJson(results, MovieResponse.class);

                    if(movieResponse != null){
                        List<MovieResponseResults> movieResponseResults = movieResponse.getResults();

                        movieSearchAdapter = new MovieSearchAdapter(MainActivity.this, movieResponseResults);

                        resultRecyclerView.setAdapter(movieSearchAdapter);

                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_right);

                        resultRecyclerView.setLayoutAnimation(controller);
                        resultRecyclerView.scheduleLayoutAnimation();

                        Paper.book().write("cache", new Gson().toJson(movieResponse));

                        Paper.book().write("source", "movie");



                    }
                }
                else
                {
                    PersonResponse personResponse = new Gson().fromJson(results, PersonResponse.class);

                    if(personResponse != null) {
                        List<PersonResponseResults> personResponseResults = personResponse.getResults();

                        personSearchAdapter = new PersonSearchAdapter(MainActivity.this, personResponseResults);

                        resultRecyclerView.setAdapter(personSearchAdapter);

                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_right);

                        resultRecyclerView.setLayoutAnimation(controller);
                        resultRecyclerView.scheduleLayoutAnimation();

                        Paper.book().write("cache", new Gson().toJson(personResponse));

                        Paper.book().write("source", "person");
                    }
                }
            }
        }

        querySearchButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(queryEditText.getText() != null){
                    String query = queryEditText.getText().toString();

                    if(query.equals("") || query.equals(" "))
                    {
                        Toast.makeText(MainActivity.this, "Please enter any text..", Toast.LENGTH_SHORT).show();
                    }else{
                        queryEditText.setText("");

                        String finalQuery = query.replaceAll(" ", "+");

                        if(category.size() > 0){
                            String categoryName = category.get(sourceSpinner.getSelectedIndex());

                            if(categoryName.equals(movie)){
                                Call<MovieResponse> movieResponseCall = retrofitService.getMovieByQuery(TheMovieDBApiKey, finalQuery);

                                movieResponseCall.enqueue(new Callback<MovieResponse>() {
                                    @Override
                                    public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                                        MovieResponse movieResponse = response.body();

                                        if(movieResponse != null){
                                            List<MovieResponseResults> movieResponseResults = movieResponse.getResults();

                                            movieSearchAdapter = new MovieSearchAdapter(MainActivity.this, movieResponseResults);

                                            resultRecyclerView.setAdapter(movieSearchAdapter);

                                            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_right);

                                            resultRecyclerView.setLayoutAnimation(controller);
                                            resultRecyclerView.scheduleLayoutAnimation();

                                            Paper.book().write("cache", new Gson().toJson(movieResponse));

                                            Paper.book().write("source", "movie");



                                        }else {
                                            Call<PersonResponse> personResponseCall = retrofitService.getActorByQuery(TheMovieDBApiKey, finalQuery);

                                            personResponseCall.enqueue(new Callback<PersonResponse>() {

                                                @Override
                                                public void onResponse(Call<PersonResponse> call, Response<PersonResponse> response) {
                                                    PersonResponse personResponse = response.body();

                                                    if(personResponse != null) {
                                                        List<PersonResponseResults> personResponseResults = personResponse.getResults();

                                                        personSearchAdapter = new PersonSearchAdapter(MainActivity.this, personResponseResults);

                                                        resultRecyclerView.setAdapter(personSearchAdapter);

                                                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this, R.anim.layout_slide_right);

                                                        resultRecyclerView.setLayoutAnimation(controller);
                                                        resultRecyclerView.scheduleLayoutAnimation();

                                                        Paper.book().write("cache", new Gson().toJson(personResponse));

                                                        Paper.book().write("source", "person");
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<PersonResponse> call, Throwable t) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

                                    }
                                });
                            }else{

                            }
                        }
                    }
                }

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();

        Paper.book().write("position", sourceSpinner.getSelectedIndex());
    }
}