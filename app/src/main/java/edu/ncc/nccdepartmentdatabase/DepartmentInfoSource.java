package edu.ncc.nccdepartmentdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DepartmentInfoSource {

    private SQLiteDatabase database;
    private DepartmentInfoHelper deptHelper;

    private String[] allColumns = {DepartmentInfoHelper._ID, DepartmentInfoHelper.NAME,
            DepartmentInfoHelper.LOCATION, DepartmentInfoHelper.PHONE,};

    public DepartmentInfoSource(Context context)
    {
        deptHelper = new DepartmentInfoHelper(context);
    }

    public void open() throws SQLException {
        database = deptHelper.getWritableDatabase();
    }

    public void close()
    {
        deptHelper.close();
    }

    public void addDept(String name, String phone, String location) {
        ContentValues values = new ContentValues();

        values.put(DepartmentInfoHelper.NAME, name);
        values.put(DepartmentInfoHelper.LOCATION, location);
        values.put(DepartmentInfoHelper.PHONE, phone);

        long insertId = database.insert(DepartmentInfoHelper.TABLE_NAME, null, values);
    }

    public List<DepartmentEntry> getAllDepartments() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        return dpts;
    }

    public List<DepartmentEntry> findDepartments() {
        List<DepartmentEntry> dpts = new ArrayList<>();
        DepartmentEntry entry;
        Cursor cursor = database.query(DepartmentInfoHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            entry = cursorToEntry(cursor);
            dpts.add(entry);
            cursor.moveToNext();
        }
        cursor.close();

        return dpts;
    }

    private DepartmentEntry cursorToEntry(Cursor cursor) {
        DepartmentEntry entry = new DepartmentEntry();
        entry.setId(cursor.getLong(0));
        entry.setName(cursor.getString(1));
        entry.setLocation(cursor.getString(2));
        entry.setPhone(cursor.getString(3));
        return entry;
    }
}