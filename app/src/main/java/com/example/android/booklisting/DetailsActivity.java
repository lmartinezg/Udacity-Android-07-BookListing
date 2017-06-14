package com.example.android.booklisting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Show detailed info about a book, and provide a button
 * to navigate to the info web page for the book.
 */
public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title");
        String printableAuthors = extras.getString("printableAuthors");
        String publisher = extras.getString("publisher");
        String publishedDate = extras.getString("publishedDate");
        String printableIdentifiers = extras.getString("printableIdentifiers");
        final String infoLink = extras.getString("infoLink");

        // Find Views
        TextView titleView = (TextView) findViewById(R.id.title_detail);
        TextView authorsLabelView = (TextView) findViewById(R.id.authors_label);
        TextView authorsView = (TextView) findViewById(R.id.authors);
        TextView publisherLabelView = (TextView) findViewById(R.id.publisher_label);
        TextView publisherView = (TextView) findViewById(R.id.publisher);
        TextView publishedDateView = (TextView) findViewById(R.id.published_date);
        TextView identifiersView = (TextView) findViewById(R.id.identifiers);

        // Set texts
        titleView.setText(title);

        // If there are no authors, hide this views
        authorsView.setText(printableAuthors);
        if (printableAuthors != null && !printableAuthors.equals("")) {
            authorsLabelView.setVisibility(View.VISIBLE);
            authorsView.setVisibility(View.VISIBLE);
        } else {
            authorsLabelView.setVisibility(View.GONE);
            authorsView.setVisibility(View.GONE);
        }

        // If there is no publisher, hide this views
        publisherView.setText(publisher);
        if (publisher != null && !publisher.equals("")) {
            publisherLabelView.setVisibility(View.VISIBLE);
            publisherView.setVisibility(View.VISIBLE);
        } else {
            publisherLabelView.setVisibility(View.GONE);
            publisherView.setVisibility(View.GONE);
        }

        publishedDateView.setText(publishedDate);
        identifiersView.setText(printableIdentifiers);

        // Find the button and set a click listener on it,
        // to open the web page specified in infoLink
        Button button = (Button) findViewById(R.id.more_info);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(infoLink);

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }
}
