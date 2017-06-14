package com.example.android.booklisting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Original query URL:
    // https://www.googleapis.com/books/v1/volumes?q=android&maxResults=1
    static final String queryUrlString1 = "https://www.googleapis.com/books/v1/volumes?q=";
    static final String queryUrlString2 = "&maxResults=10";

    static final String EXTRA_QUERY_URL = "com.example.android.booklisting.EXTRA_QUERY_URL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView searchButton = (TextView) findViewById(R.id.search_button);

        // Add a listener to the search button
        searchButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookListingActivity.class);
                EditText editText = (EditText) findViewById(R.id.queryText);
                String query = queryUrlString1 + editText.getText().toString() + queryUrlString2;
                intent.putExtra(EXTRA_QUERY_URL, query);
                startActivity(intent);
            }
        });
    }
}
