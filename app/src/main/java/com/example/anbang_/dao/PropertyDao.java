package com.example.anbang_.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.anbang_.dto.PropertyDto;

import java.util.List;

@Dao
public interface PropertyDao {
    @Query("SELECT * FROM property")
    List<PropertyDto> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PropertyDto propertyDto);

    @Update
    void update(PropertyDto propertyDto);

    @Delete()
    void delete(PropertyDto propertyDto);

    @Query("DELETE from property")
    void deleteAll();
}


