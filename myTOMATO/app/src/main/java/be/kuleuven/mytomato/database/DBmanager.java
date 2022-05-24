package be.kuleuven.mytomato.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBmanager {

    private static SQLiteDatabase db;

    public static void initDB(Context context){
        openHelper helper = new openHelper(context);
        db = helper.getWritableDatabase();
    }


}
