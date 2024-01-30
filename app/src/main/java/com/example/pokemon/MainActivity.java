package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import kotlin.collections.ArrayDeque;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText searchField;
    LinearLayout searchBar;
    ImageButton searchButton;
    Spinner searchResultSpinner;
    RequestQueue requestQueue;
    List<PokemonCard> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();

        requestQueue = Volley.newRequestQueue(this);
        searchButton.setOnClickListener(this);
        getAllCards();
    }

    private void initGui(){
        searchField = findViewById(R.id.et_search);
        searchButton = findViewById(R.id.btn_search);
        searchResultSpinner = findViewById(R.id.sp_search_result);
        searchBar = findViewById(R.id.ll_searchbar);
    }

    @Override
    public void onClick(View v) {
        if (cards == null) return;

        List<PokemonCard> searchList = new ArrayList<>();
        for (PokemonCard pc : cards) {
            if (pc.name.toLowerCase().contains(searchField.getText().toString().toLowerCase())) {
                searchList.add(pc);
            }
        }
        createSpinner(searchList);
    }

    private void createSpinner(List<PokemonCard> pcList){
        List<String> pcNames = pcList.stream().map((pokemonCard) ->
                        pokemonCard.name + " " + pokemonCard.id).collect(Collectors.toList());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, pcNames);
        searchResultSpinner.setAdapter(adapter);
    }
    private void getAllCards() {
        String url = "https://api.tcgdex.net/v2/en/cards";

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            cards = new Gson().fromJson(response, new TypeToken<List<PokemonCard>>(){}.getType());
            searchBar.setVisibility(View.VISIBLE);
            Log.d("Cards", String.valueOf(cards.size()));
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        });
        requestQueue.add(request);
    }
}