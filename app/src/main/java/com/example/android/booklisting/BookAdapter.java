package com.example.android.booklisting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.booklisting.R.id.authors;

/**
 * An {@link BookAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link Book} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    /**
     * Constructs a new {@link BookAdapter}.
     *
     * @param context of the app
     * @param books   is the list of books, which is the data source of the adapter
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        Book currentBook = getItem(position);

        // Find the TextView with view ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // Display the title of the current book in that TextView
        titleView.setText(currentBook.getTitle());

        // Find the TextView with view ID authors
        TextView authorsView = (TextView) listItemView.findViewById(authors);
        // Get the authors from the Book object.
        // As it is an array, each element will be concatenated to be shown together in the text view

        ArrayList<String> authors = currentBook.getAuthors();
        if (authors != null && authors.size()>0) {
            String concatenatedAuthorsString = "";
            for (String a : authors) {
                if (!concatenatedAuthorsString.equals("")) {
                    concatenatedAuthorsString += ", ";
                }
                concatenatedAuthorsString += a;
            }
            if (!concatenatedAuthorsString.equals("")) {
                concatenatedAuthorsString += ".";
            }

            // Display the authors of the current book in that TextView
            authorsView.setText(concatenatedAuthorsString);
            authorsView.setVisibility(View.VISIBLE);
        } else {
            authorsView.setVisibility(View.GONE);
        }

        // Find the TextView with view ID publisher
        TextView publisherView = (TextView) listItemView.findViewById(R.id.publisher);
        // Display the publisher of the current book in that TextView
        String publisher = currentBook.getPublisher();
        if (publisher != null && !publisher.equals("")) {
            publisherView.setText(currentBook.getPublisher());
            publisherView.setVisibility(View.VISIBLE);
        } else {
            publisherView.setVisibility(View.GONE);
        }

        // Find the TextView with view ID identifier
        LinearLayout identifierView = (LinearLayout) listItemView.findViewById(R.id.identifier);
        // Find the TextView with view ID identifier_type
        TextView identifierTypeView = (TextView) listItemView.findViewById(R.id.identifier_type);
        // Find the TextView with view ID identifier_code
        TextView identifierCodeView = (TextView) listItemView.findViewById(R.id.identifier_code);
        // Get the identifiers array from the Book object.
        ArrayList<IndustryIdentifier> identifiers = currentBook.getIndustryidentifiers();
        if (identifiers != null && identifiers.size()>0) {
            // Display the first identifier of the current book in that TextView
            identifierTypeView.setText(identifiers.get(0).getType());
            identifierCodeView.setText(identifiers.get(0).getIdentifier());
            identifierView.setVisibility(View.VISIBLE);
        } else {
            // Hides the layout if no identifiers
            identifierView.setVisibility(View.GONE);
        }

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }

}
