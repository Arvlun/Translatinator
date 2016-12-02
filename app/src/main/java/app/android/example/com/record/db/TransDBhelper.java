package app.android.example.com.record.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.android.example.com.record.db.TransDBcontract;

/**
 * Created by Arvid on 2016-11-28.
 */

public class TransDBhelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    public static final String DATABASE_NAME = "trans.db";

    public TransDBhelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_TOPLIST_TABLE = "CREATE TABLE " + TransDBcontract.PhrasesDefs.TABLE_NAME + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + TransDBcontract.PhrasesDefs.PHRASE_COL + " TEXT NOT NULL" +
                " );";
        Log.v("DB LOG", "onCreate- sqlcreateSTR = " + SQL_CREATE_TOPLIST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TOPLIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Borde ändra när funktionaliteten är klar så att det inte tar bort allt vid uppdatering
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TransDBcontract.PhrasesDefs.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
