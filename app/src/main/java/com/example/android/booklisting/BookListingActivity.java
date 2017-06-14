package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListingActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    static final String EXTRA_QUERY_URL = "com.example.android.booklisting.EXTRA_QUERY_URL";
    private static final String LOG_TAG = BookListingActivity.class.getName();
    /**
     * Constant value for the BOOK loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;
    private String mQuery = null;
    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_listing);

        Intent intent = getIntent();
        mQuery = intent.getStringExtra(EXTRA_QUERY_URL);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which opens  a details activity
        // with more information about the selected book.
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the book that was clicked on
                Book currentBook = mAdapter.getItem(position);

                Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

                intent.putExtra("title", currentBook.getTitle());

                ArrayList<String> authors = currentBook.getAuthors();
                String printableAuthors = "";
                for (int i = 0; i < authors.size(); i++) {
                    if (i != 0) {
                        printableAuthors += ", ";
                    }
                    printableAuthors += authors.get(i);
                }
                intent.putExtra("printableAuthors", printableAuthors);

                intent.putExtra("publisher", currentBook.getPublisher());

                intent.putExtra("publishedDate", currentBook.getPublishedDate());

                ArrayList<IndustryIdentifier> industryIdentifiers = currentBook.getIndustryidentifiers();
                String printableIdentifiers = "";
                for (int i = 0; i < industryIdentifiers.size(); i++) {
                    if (i != 0) {
                        printableIdentifiers += "\n";
                    }
                    printableIdentifiers += industryIdentifiers.get(i).getIdentifier();
                }
                intent.putExtra("printableIdentifiers", printableIdentifiers);

                intent.putExtra("infoLink", currentBook.getInfoLink());

                startActivity(intent);

            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BookLoader(this, mQuery);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Books}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}
