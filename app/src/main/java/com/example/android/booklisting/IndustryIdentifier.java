package com.example.android.booklisting;

/**
 * An industry identifier, such as ISBN_13 (type) and 123456789D (code)
 */

public class IndustryIdentifier {

    private String type;
    private String code;

    /**
     * Construct a new Industry Identifier
     *
     * @param type The type for the identifier (ISBN_13...)
     * @param code The code for the identifier (the code)
     */
    public IndustryIdentifier(String type, String code) {
        this.type = type;
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public String getIdentifier() {
        return code;
    }
}
