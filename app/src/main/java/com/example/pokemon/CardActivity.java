package com.example.pokemon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

public class CardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");
        getCard(id);
    }

    private void getCard(String id) {
        String url = "https://api.tcgdex.net/v2/en/cards/" + id;

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            Card card = new Gson().fromJson(response, Card.class);
            ((TextView)findViewById(R.id.tv_name)).setText(card.name);
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        );
        MainActivity.requestQueue.add(request);
    }
}