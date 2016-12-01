package app.android.example.com.record.data;

/**
 * Created by Arvid on 2016-12-01.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;

import java.util.HashSet;

import app.android.example.com.record.db.TransDBcontract;
import app.android.example.com.record.db.TransDBhelper;

public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {
        mContext.deleteDatabase(TransDBhelper.DATABASE_NAME);
    }

    /*
        This function gets called before each test is executed to delete the database.  This makes
        sure that we always have a clean test.
     */
    public void setUp() {
        deleteTheDatabase();
    }

    /*
        Students: Uncomment this test once you've written the code to create the Location
        table.  Note that you will have to have chosen the same column names that I did in
        my solution for this test to compile, so if you haven't yet done that, this is
        a good time to change your column names to match mine.

        Note that this only tests that the Location table has the correct columns, since we
        give you the code for the weather table.  This test does not look at the
     */
    public void testCreateDb() throws Throwable {
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(TransDBcontract.PhrasesDefs.TABLE_NAME);
        //tableNameHashSet.add(WeatherContract.WeatherEntry.TABLE_NAME);

        mContext.deleteDatabase(TransDBhelper.DATABASE_NAME);
        SQLiteDatabase db = new TransDBhelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: This means that the database has not been created correctly",
                c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertTrue("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + TransDBcontract.PhrasesDefs.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

//        // Build a HashSet of all of the column names we want to look for
//        final HashSet<String> locationColumnHashSet = new HashSet<String>();
//        locationColumnHashSet.add(TransDBcontract.PhrasesDefs._ID);
//        locationColumnHashSet.add(TransDBcontract.PhrasesDefs.PHRASE_COL);
////        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LAT);
////        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_COORD_LONG);
////        locationColumnHashSet.add(WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING);
//
//        int columnNameIndex = c.getColumnIndex("name");
//        do {
//            String columnName = c.getString(columnNameIndex);
//            locationColumnHashSet.remove(columnName);
//        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
//        assertTrue("Error: The database doesn't contain all of the required location entry columns",
//                locationColumnHashSet.isEmpty());
        db.close();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        location database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can uncomment out the "createNorthPoleLocationValues" function.  You can
        also make use of the ValidateCurrentRecord function from within TestUtilities.
    */
    public void testLocationTable() {
        insertLocation();
    }

    /*
        Students:  Here is where you will build code to test that we can insert and query the
        database.  We've done a lot of work for you.  You'll want to look in TestUtilities
        where you can use the "createWeatherValues" function.  You can
        also make use of the validateCurrentRecord function from within TestUtilities.
     */
//    public void testWeatherTable() {
//        // First insert the location, and then use the locationRowId to insert
//        // the weather. Make sure to cover as many failure cases as you can.
//
//        // Instead of rewriting all of the code we've already written in testLocationTable
//        // we can move this code to insertLocation and then call insertLocation from both
//        // tests. Why move it? We need the code to return the ID of the inserted location
//        // and our testLocationTable can only return void because it's a test.
//
//        long locationRowId = insertLocation();
//
//        assertFalse("Error: Location Not Inserted correctly", locationRowId == -1L);
//
//        // First step: Get reference to writable database
//
//        WeatherDbHelper dbHelper = new WeatherDbHelper(mContext);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//
//        // Create ContentValues of what you want to insert
//        // (you can use the createWeatherValues TestUtilities function if you wish)
//
//        ContentValues weatherValues = TestUtilities.createWeatherValues(locationRowId);
//
//        // Insert ContentValues into database and get a row ID back
//
//        long weatherRowId = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, weatherValues);
//        assertTrue(weatherRowId != -1);
//
//        // Query the database and receive a Cursor back
//
//        Cursor weatherCursor = db.query(
//                WeatherContract.WeatherEntry.TABLE_NAME,  // Table to Query
//                null, // leaving "columns" null just returns all the columns.
//                null, // cols for "where" clause
//                null, // values for "where" clause
//                null, // columns to group by
//                null, // columns to filter by row groups
//                null  // sort order
//        );
//
//        // Move the cursor to a valid database row
//        assertTrue( "Error: No Records returned from location query", weatherCursor.moveToFirst() );
//
//        // Fifth Step: Validate the location Query
//        TestUtilities.validateCurrentRecord("testInsertReadDb weatherEntry failed to validate",
//                weatherCursor, weatherValues);
//
//        // Move the cursor to demonstrate that there is only one record in the database
//        assertFalse( "Error: More than one record returned from weather query",
//                weatherCursor.moveToNext() );
//
//
//        weatherCursor.close();
//        dbHelper.close();
//    }


    /*
        Students: This is a helper method for the testWeatherTable quiz. You can move your
        code from testLocationTable to here so that you can call this code from both
        testWeatherTable and testLocationTable.
     */

    public long insertLocation() {
        // First step: Get reference to writable database

        TransDBhelper dbHelper = new TransDBhelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        // Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)

        //ContentValues testValues = TestUtilities.createNorthPoleLocationValues();

        ContentValues phraseValues = new ContentValues();

        phraseValues.put(TransDBcontract.PhrasesDefs.PHRASE_COL, "Test Phrase");

        // Insert ContentValues into database and get a row ID back

        long locationRowId;
        locationRowId = db.insert(TransDBcontract.PhrasesDefs.TABLE_NAME, null, phraseValues);
        assertTrue(locationRowId != -1);

        // Query the database and receive a Cursor back
        Cursor cursor = db.query(
                TransDBcontract.PhrasesDefs.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        Log.v("INSERT TEST: ", DatabaseUtils.dumpCursorToString(cursor));
        // Move the cursor to a valid database row

        assertTrue("Error: No Records return from location query", cursor.moveToFirst());

        // Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in TestUtilities to validate the
        // query if you like)

//        TestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
//                cursor, testValues);


        assertFalse("Error: More than one record return from location entry",
                cursor.moveToNext());
        // Finally, close the cursor and database
        cursor.close();
        db.close();
        return locationRowId;
    }
}
