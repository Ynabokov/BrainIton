package com.example.brainiton;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "taskManager";
    private static final String TABLE_TASKS = "tasks";
    private static final String KEY_NAME = "name";
    private static final String KEY_DUE_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "(" + KEY_NAME + "STRING PRIMARY KEY," + KEY_DUE_DATE + "TEXT)";
        db.execSQL(CREATE_TASKS_TABLE);
        //check INTEGER PRIMARY KEY
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("KEY_NAME", task.getName());
        values.put("KEY_DUE_DATE", task.getDueDate().toString());

        db.insert(TABLE_TASKS, null, values);
        db.close();
    }

    Task getTask(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_NAME, KEY_DUE_DATE}, KEY_NAME + "=?", new String[]{String.valueOf(name)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
//        String strDate = cursor.getString(1);
//        SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");
//        Date date = formattedDate.parse(strDate);
        Task task = new Task(cursor.getString(0), "date");
        return task;
    }

public List<Task> getAllTasks() {
    List<Task> taskList = new ArrayList<>();
    String selectQuery = "SELECT * FROM "+ TABLE_TASKS;
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery(selectQuery, null);

    if(cursor !=null) {
        do{

            Task task = new Task();
            task.setName(cursor.getString(0));
            //task.setDue_date(date);

            taskList.add(task);
        }
        while(cursor.moveToFirst());
    }
    return taskList;
}
public int updateTasks(Task task){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put(KEY_NAME, task.getName());
    values.put(KEY_DUE_DATE, task.getDueDate().toString());
    return db.update(TABLE_TASKS, values, KEY_NAME + "=?", new String[]{String.valueOf(task.getName())});
}

//DELETE function
public void deleteTask(Task task){
    SQLiteDatabase db = this.getWritableDatabase();
    db.delete(TABLE_TASKS, KEY_NAME + "=?", new String[]{String.valueOf(task.getName())});
    db.close();

}
public Calendar formatDate(Cursor cursor){
    String strDate = cursor.getString(1);
    SimpleDateFormat formattedDate = new SimpleDateFormat("dd-MM-yyyy");
    Date date = null;
    try {
        date = formattedDate.parse(strDate);
    } catch (ParseException e) {
        e.printStackTrace();
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    return cal;
}

public int getTasksCount(){
    String countQuery = "SELECT * FROM "+ TABLE_TASKS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    cursor.close();

    return cursor.getCount();
}
}

