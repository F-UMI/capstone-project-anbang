/*
package com.example.anbang_.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.anbang_.temp.LocalDateTimeConverter;
import com.example.anbang_.dao.PropertyDao;
import com.example.anbang_.dto.PropertyDto;

@Database(entities = {PropertyDto.class}, version = 1)
@TypeConverters({LocalDateTimeConverter.class})
public abstract class PropertyDB extends RoomDatabase {
    public abstract PropertyDao propertyDao();

    private static PropertyDB INSTANCE;

    public static PropertyDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (PropertyDB.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                PropertyDB.class, "property.db")
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}



*/
