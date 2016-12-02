package app.android.example.com.record.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import app.android.example.com.record.db.TransDBcontract;

/**
 * Created by Arvid on 2016-11-28.
 */

// TODO: fix this shiet, skapa kontrakt URI, kolla på den där kursen igen med URI skaerna
public class TransDBProvider extends ContentProvider {

    private static final UriMatcher matcher = buildUriMatcher();

    private TransDBhelper dbHelper;

    static final int PHRASE = 55;

    @Override
    public boolean onCreate() {
        dbHelper = new TransDBhelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = matcher.match(uri);

        switch (match) {
            case PHRASE:
                return TransDBcontract.PhrasesDefs.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri:" + uri);
        }
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String auth = TransDBcontract.CONTENT_AUTHORITY;

        matcher.addURI(auth, TransDBcontract.PATH_PHRASE, PHRASE);

        return matcher;
    }

    public long savePhrase(ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //ContentValues values = new ContentValues();
        //values.put("phrase", translation);

        long rowId = db.insert("translations", null, values);
        db.close();
        return rowId;
    }

    public Cursor getSavedPhrases() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String[] projection = {
                "_id",
                "phrases"
        };

        String sortOrder = "phrases DESC";

        String selection = "phrase" + " =?";
        //String[] selectionArgs = {
        Cursor res = db.query(
                "translations",                           // The table to query
                projection,                               // The columns to return
                null,                                       // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        Log.v("TAG", DatabaseUtils.dumpCursorToString(res));
        return res;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionsArgs, String sortOrder) {

        Cursor returnCursor;

        switch (matcher.match(uri)) {
            case PHRASE: {
                // Borde förmodligen ändra detta!
                returnCursor = getSavedPhrases();
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return returnCursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = matcher.match(uri);

        Uri returnUri;

        switch (match) {
            case PHRASE: {
                long id = savePhrase(values);
                Log.v("INSTERT" , "RED ID: " + id);
                if (id >= 0) {
                    returnUri = TransDBcontract.PhrasesDefs.buildUri(id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    //TODO: lägg till
    @Override
    public int delete(Uri uri, String selection, String[] selectionsArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted;

        switch (matcher.match(uri)) {
            case PHRASE: {
                Log.v("DELETE", "DELETE PHRASE");
                rowsDeleted = db.delete(TransDBcontract.PhrasesDefs.TABLE_NAME, selection, selectionsArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return 0;
    }

    //TODO: Fixa så det är möjligt att ändra
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionsArgs) {

        int res;

        switch (matcher.match(uri)) {
            case PHRASE: {
                Log.v("UPDATE", "UPDATE PHRASE");
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return 0;
    }
}
