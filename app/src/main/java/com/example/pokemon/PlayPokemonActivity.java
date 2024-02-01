package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayPokemonActivity extends AppCompatActivity implements View.OnClickListener {

    PokemonPlayer player1 = new PokemonPlayer();
    //List<EveryCards> ecList = new ArrayList<EveryCards>();
    Button player1Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pokemon);

        player1Btn = findViewById(R.id.player1_pokemon_btn);

        player1Btn.setOnClickListener(this);

        setupPlayerCards(player1);
    }

    @Override
    public void onClick(View v) {
        if (player1.index < 10) {
//            getCard(player1.otherCards.get(player1.index).id);
            getCard(player1);
            //player1.index++;
            //player1Btn.setText(String.valueOf(player1.index));
        }
    }

    private void setupPlayerCards(PokemonPlayer player) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(MainActivity.cards.size());
            player.otherCards.add(MainActivity.cards.get(randomIndex));
        }
    }

    private void getNewPlayerCard(PokemonPlayer player) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(MainActivity.cards.size());
        player.otherCards.set(player.index, MainActivity.cards.get(randomIndex));
        Log.d("otherCards", String.valueOf(player.otherCards.size()));
        Log.d("otherCardsIndex", player.otherCards.get(player.index).name);
        Log.d("otherCardsNextIndex", player.otherCards.get(player.index).name);
        getCard(player);
    }

    private void getCard(PokemonPlayer player) {
        String url = "https://api.tcgdex.net/v2/en/cards/" + player.otherCards.get(player.index).id;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Card card = new Gson().fromJson(response, Card.class);
                ((TextView) findViewById(R.id.player1_pokemon_name)).setText(card.name);
                getImage(card);
                Log.d("player", String.valueOf(player.index) +" " + player.otherCards.get(player1.index).name);
                player1Btn.setText(String.valueOf(player.index+1));
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