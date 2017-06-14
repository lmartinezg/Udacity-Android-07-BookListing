package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods
 */

public class QueryUtils {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query Google Books and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl) {

        // Simluates a slow internet connection
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractBookFromJson(jsonResponse);

        // Return the list of {@link Book}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the results from the web service.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing the given JSON response.
     */
    private static List<Book> extractBookFromJson(String bookJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<Book> books = new ArrayList<Book>();

        Book book = null;

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of books.
            JSONArray bookArray = baseJsonResponse.getJSONArray("items");

            // For each book in the bookArray, create an {@link Book} object
            for (int i = 0; i < bookArray.length(); i++) {

                // Get a single book at position i within the list of earthquakes
                JSONObject currentBookJSON = bookArray.getJSONObject(i);

                // ID
                String id = currentBookJSON.getString("id");

                // Volume Info
                JSONObject volumeInfoJSON = currentBookJSON.getJSONObject("volumeInfo");

                // Title
                String title = volumeInfoJSON.getString("title");

                // Authors
                ArrayList<String> authors = new ArrayList<String>();
                JSONArray authorJSONArray = volumeInfoJSON.optJSONArray("authors");
                if (authorJSONArray != null) {
                    for (int j = 0; j < authorJSONArray.length(); j++) {
                        authors.add(authorJSONArray.getString(j));
                    }
                }

                // Publisher
                String publisher = volumeInfoJSON.optString("publisher");

                // Published Date
                String publishedDate = volumeInfoJSON.optString("publishedDate");

                // Industry identifiers
                ArrayList<IndustryIdentifier> industryIdentifiers = new ArrayList<IndustryIdentifier>();
                JSONArray industryIdentifierJSONArray = volumeInfoJSON.optJSONArray("industryIdentifiers");
                if (industryIdentifierJSONArray != null) {
                    for (int j = 0; j < industryIdentifierJSONArray.length(); j++) {
                        JSONObject industryIdentifierJSON = industryIdentifierJSONArray.getJSONObject(j);
                        String type = industryIdentifierJSON.getString("type");
                        String identifier = industryIdentifierJSON.getString("identifier");
                        IndustryIdentifier industryIdentifier = new IndustryIdentifier(type, identifier);
                        industryIdentifiers.add(industryIdentifier);
                    }
                }

                // Page count
                int pageCount = volumeInfoJSON.optInt("pageCount");

                // Image Links
                ArrayList<ImageLink> imageLinks = new ArrayList<ImageLink>();
                String type;
                String url;
                ImageLink imageLink;

                JSONObject imagelinkJSONObject = volumeInfoJSON.optJSONObject("imageLinks");
                if (imagelinkJSONObject != null) {

                    type = "smallThumbnail";
                    url = imagelinkJSONObject.optString(type);
                    if (url != null) {
                        imageLink = new ImageLink(type, url);
                        imageLinks.add(imageLink);
                    }

                    type = "thumbnail";
                    url = imagelinkJSONObject.optString(type);
                    if (url != null) {
                        imageLink = new ImageLink(type, url);
                        imageLinks.add(imageLink);
                    }
                }

                // Language
                String language = volumeInfoJSON.optString("language");

                // Info Link
                String infoLink = volumeInfoJSON.optString("infoLink");


                // Create a new {@link Book} object with the parsed data from the JSON response.
                book = new Book(id, title, authors, publisher, publishedDate,
                        industryIdentifiers, pageCount, imageLinks, language, infoLink);

                // Add the new {@link Book} to the list of books.
                books.add(book);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * Return a Book object from a JSONObject that represents a book in JSON format
     *
     * @param currentBookJSON a JSON Object containing info about a book
     * @return the Book object
     */
    private static Book JSONtoBook(JSONObject currentBookJSON) {

        Book book = null;

        // For a given book, extract its data
        try {
            // ID
            String id = currentBookJSON.getString("id");

            // Volume Info
            JSONObject volumeInfoJSON = currentBookJSON.getJSONObject("volumeInfo");

            // Title
            String title = volumeInfoJSON.getString("title");

            // Authors
            ArrayList<String> authors = new ArrayList<String>();
            JSONArray authorJSONArray = volumeInfoJSON.getJSONArray("authors");
            for (int j = 0; j < authorJSONArray.length(); j++) {
                authors.add(authorJSONArray.getString(j));
            }

            // Publisher
            String publisher = volumeInfoJSON.getString("publisher");

            // Published Date
            String publishedDate = volumeInfoJSON.getString("publishedDate");

            // Industry identifiers
            ArrayList<IndustryIdentifier> industryIdentifiers = new ArrayList<IndustryIdentifier>();
            JSONArray industryIdentifierJSONArray = volumeInfoJSON.getJSONArray("industryIdentifiers");
            for (int j = 0; j < industryIdentifierJSONArray.length(); j++) {
                JSONObject industryIdentifierJSON = industryIdentifierJSONArray.getJSONObject(j);
                String type = industryIdentifierJSON.getString("type");
                String identifier = industryIdentifierJSON.getString("identifier");
                IndustryIdentifier industryIdentifier = new IndustryIdentifier(type, identifier);
                industryIdentifiers.add(industryIdentifier);
            }

            // Page count
            int pageCount = volumeInfoJSON.getInt("pageCount");

            // Image Links
            ArrayList<ImageLink> imageLinks = new ArrayList<ImageLink>();
            String type;
            String url;
            ImageLink imageLink;

            JSONObject imagelinkJSONObject = volumeInfoJSON.getJSONObject("imageLinks");

            type = "smallThumbnail";
            url = imagelinkJSONObject.getString(type);
            imageLink = new ImageLink(type, url);
            imageLinks.add(imageLink);

            type = "thumbnail";
            url = imagelinkJSONObject.getString(type);
            imageLink = new ImageLink(type, url);
            imageLinks.add(imageLink);

            // Language
            String language = volumeInfoJSON.getString("language");

            // Info Link
            String infoLink = volumeInfoJSON.getString("infoLink");


            // Create a new {@link Book} object with the parsed data from the JSON response.
            book = new Book(id, title, authors, publisher, publishedDate,
                    industryIdentifiers, pageCount, imageLinks, language, infoLink);

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        return book;

    }
}
