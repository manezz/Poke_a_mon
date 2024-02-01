package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class PlayPokemonActivity extends AppCompatActivity {

    PokemonPlayer player1 = new PokemonPlayer();
    PokemonPlayer player2 = new PokemonPlayer();
    Button player1Btn;
    Button player2Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pokemon);

        player1Btn = findViewById(R.id.player1_pokemon_btn);
        player2Btn = findViewById(R.id.player1_pokemon_btn);

        player1Btn.setOnClickListener(v -> {
            onPlayer1BtnClick(v);
        });
        player2Btn.setOnClickListener(v -> {
            onPlayer2BtnClick(v);
        });

        setupPlayerCards(player1);
    }

    public void onPlayer1BtnClick(View v) {
        if (player1.index < 10) {
            player1Btn.setText(String.valueOf(player1.index+1));
            getCard(player1);
        }
    }

    public void onPlayer2BtnClick(View v) {
        if (player1.index < 10) {
            player1Btn.setText(String.valueOf(player1.index+1));
            getCard(player2);
        }
    }

    private void setupPlayerCards(PokemonPlayer player) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(MainActivity.cards.size());
            player.playerCards.add(MainActivity.cards.get(randomIndex));
        }
    }

    private void getNewPlayerCard(PokemonPlayer player) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(MainActivity.cards.size());
        player.playerCards.set(player.index, MainActivity.cards.get(randomIndex));
        getCard(player);
    }

    private void getCard(PokemonPlayer player) {
        String url = "https://api.tcgdex.net/v2/en/cards/" + player.playerCards.get(player.index).id;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Card card = new Gson().fromJson(response, Card.class);
                ((TextView) findViewById(R.id.player1_pokemon_name)).setText(card.name);
                getImage(card);
                player.index++;
            } catch (JsonSyntaxException e) {
                getNewPlayerCard(player);
            }
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        }
        );
        MainActivity.requestQueue.add(request);
    }

    private void getImage(Card card) {
        ImageView cardImage = findViewById(R.id.player1_pokemon_img);
        Picasso.get().load(card.image + "/high.jpg").into(cardImage);
    }
}