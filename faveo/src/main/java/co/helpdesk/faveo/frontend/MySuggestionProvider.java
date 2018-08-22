package co.helpdesk.faveo.frontend;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by narendra on 20/02/17.
 */

public class MySuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "co.helpdesk.faveo.frontend.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
