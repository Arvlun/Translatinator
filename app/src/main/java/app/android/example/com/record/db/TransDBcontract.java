package app.android.example.com.record.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Arvid on 2016-12-01.
 */

public class TransDBcontract {

    //ÄNDRA NÄR DU LÄGGER IN I GITHUBEN kanske, spelar nog ingen roll
    public static final String CONTENT_AUTHORITY = "app.android.example.com.record";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Mest för ifall vi läggar till mer funktioner
    public static final String PATH_PHRASE = "phrase";

    public static final class PhrasesDefs implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PHRASE).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PHRASE;

        public static final String TABLE_NAME = "translations";

        public static final String PHRASE_COL = "phrases";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

//        public static Uri buildPhraseUri() {
//            //return CONTENT_URI.buildUpon().appendPath(langsetting).build();
//        }
    }

}
