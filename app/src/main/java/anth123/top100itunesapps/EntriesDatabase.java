package anth123.top100itunesapps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class EntriesDatabase extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "entries.db";
    private static final int DATABASE_VERSION = 1;

    public EntriesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<AppEntry> getEntries() {
        List<AppEntry> list = new ArrayList<AppEntry>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM entries", null);
        while (cursor.moveToNext()) {
            // create entry with name and price first
            AppEntry newEntry = new AppEntry(
                    cursor.getString(cursor.getColumnIndex("name")),
                    cursor.getString(cursor.getColumnIndex("price"))
            );

            // get the image
            byte[] imgByte = cursor.getBlob(cursor.getColumnIndex("image"));
            newEntry.setImage(BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length));

            // add entry to list
            list.add(newEntry);
        }
        cursor.close();
        return list;
    }

    public void replaceEntries(List<AppEntry> newEntries) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM entries");
        for (AppEntry entry : newEntries) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", entry.getName());
            contentValues.put("price", entry.getPrice());
            contentValues.put("image", getBitmapAsByteArray(entry.getImage()));
            db.insert("entries", null, contentValues);
        }
        db.close();
    }

    private static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}
