package com.example.pokemon;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PokemonPlayer {
    public int index = 0;
    public String playerName;
    public ImageView cardImage;
    public TextView pokemonName;
    public TextView pokemonHp;
    public Button pokemonAttack;
    public Button drawCard;
    public Card currentCard;
    public ArrayList<EveryCards> playerCards = new ArrayList<EveryCards>();
}
