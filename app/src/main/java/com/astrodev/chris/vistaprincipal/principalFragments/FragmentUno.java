package com.astrodev.chris.vistaprincipal.principalFragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.support.design.widget.FloatingActionButton;

import com.astrodev.chris.vistaprincipal.VistaPrincipal;
import com.astrodev.chris.vistaprincipal.retroFit2.MoviesAdapter;
import com.astrodev.chris.vistaprincipal.retroFit2.Movie;
import com.astrodev.chris.vistaprincipal.retroFit2.MovieResponse;
import com.astrodev.chris.vistaprincipal.retroFit2.ApiClient;
import com.astrodev.chris.vistaprincipal.retroFit2.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

import com.astrodev.chris.vistaprincipal.R;

public class FragmentUno extends Fragment {


    public FragmentUno() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_uno, container, false);
        // ((VistaPrincipal)getActivity())

        final String TAG = VistaPrincipal.class.getSimpleName();
        final String API_KEY = "fee78edd7a9feab91c1cc5d9d49ddf61";

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Recargando ...", Toast.LENGTH_LONG).show();
            }
        });

        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Fallo en la ApiKey", Toast.LENGTH_LONG).show();
        }

        final RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.movies_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.item_movie, getContext()));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });


        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
