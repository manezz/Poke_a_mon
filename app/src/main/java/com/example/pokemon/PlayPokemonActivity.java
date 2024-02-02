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
    TextView vs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_pokemon);

        vs = findViewById(R.id.vs);

        player1.playerName = "Player 1";
        player2.playerName = "Player 2";

        player1.drawCard = findViewById(R.id.player1_pokemon_btn);
        player2.drawCard = findViewById(R.id.player2_pokemon_btn);

        player1.pokemonName = findViewById(R.id.player1_pokemon_name);
        player2.pokemonName = findViewById(R.id.player2_pokemon_name);

        player1.cardImage = findViewById(R.id.player1_pokemon_img);
        player2.cardImage = findViewById(R.id.player2_pokemon_img);

        player1.pokemonHp = findViewById(R.id.player1_pokemon_hp);
        player2.pokemonHp = findViewById(R.id.player2_pokemon_hp);

        player1.pokemonAttack = findViewById(R.id.player1_pokemon_Attack);
        player2.pokemonAttack = findViewById(R.id.player2_pokemon_Attack);

        player1.pokemonAttack.setOnClickListener(v -> {
            onPlayer1PokemonAttack(v);
        });
        player2.pokemonAttack.setOnClickListener(v -> {
            onPlayer2PokemonAttack(v);
        });

        player1.drawCard.setOnClickListener(v -> {
            onPlayerBtnClick(v, player1);
        });
        player2.drawCard.setOnClickListener(v -> {
            onPlayerBtnClick(v, player2);
        });

        setupPlayerCards(player1);
        setupPlayerCards(player2);
    }

    public void onPlayerBtnClick(View v, PokemonPlayer player) {
        playerIndexCheck(player);
    }

    // Checks if pokemon dead
    public void onPlayer1PokemonAttack(View v) {
        player2.currentCard.currentHp -= player1.currentCard.attacks.get(0).damage;
        getHp(player2);
        pokemonDeadCheck(player2);
    }

    public void onPlayer2PokemonAttack(View v) {
        player1.currentCard.currentHp -= player2.currentCard.attacks.get(0).damage;
        getHp(player1);
        pokemonDeadCheck(player1);
    }

    private void pokemonDeadCheck(PokemonPlayer player) {
        if (player.currentCard.currentHp <= 0) {
            playerIndexCheck(player);
        }
    }

    //Checks player index and if player have lost
    private void playerIndexCheck(PokemonPlayer player) {
        if (player.index < 10) {
            player.drawCard.setText("Draw " + (player.index+1) + "/10");
            getCard(player);
        } else {
            vs.setText(player.playerName + " LOSE");
        }
    }

    // gets random cards for at new deck
    private void setupPlayerCards(PokemonPlayer player) {
        Random rand = new Random();
        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(MainActivity.cards.size());
            player.playerCards.add(MainActivity.cards.get(randomIndex));
        }
    }

    // replaces a card with a new random one
    private void getNewPlayerCard(PokemonPlayer player) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(MainActivity.cards.size());
        player.playerCards.set(player.index, MainActivity.cards.get(randomIndex));
        getCard(player);
    }

    // gets new player card
    private void getCard(PokemonPlayer player) {
        String url = "https://api.tcgdex.net/v2/en/cards/" + player.playerCards.get(player.index).id;
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                Card card = new Gson().fromJson(response, Card.class);
                card.currentHp = card.hp;
                player.currentCard = card;
                getPokemonName(player);
                getAttack(player);
                getHp(player);
                getImage(player);
                player.index++;
//            } catch (JsonSyntaxException NullPointerException) {
            } catch (JsonSyntaxException e) {
                getNewPlayerCard(player);
            } catch (NullPointerException e) {
                getNewPlayerCard(player);
            }
        }, error -> {
            Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
        }
        );
        MainActivity.requestQueue.add(request);
    }

    private void getPokemonName(PokemonPlayer player) {
        player.pokemonName.setText(player.currentCard.name);
    }

    // get the attack and checks if both players have drawn their card
    private void getAttack(PokemonPlayer player) {
        player.pokemonAttack.setText("DMG " + player.currentCard.attacks.get(0).damage);
        if (player1.currentCard != null && player2.currentCard != null) {
            player1.pokemonAttack.setEnabled(true);
            player2.pokemonAttack.setEnabled(true);
        }
    }

    private void getHp(PokemonPlayer player) {
        player.pokemonHp.setText("HP " + player.currentCard.currentHp + "/" + player.currentCard.hp);
    }

    private void getImage(PokemonPlayer player) {
        Picasso.get().load(player.currentCard.image + "/high.jpg").into(player.cardImage);
    }
}