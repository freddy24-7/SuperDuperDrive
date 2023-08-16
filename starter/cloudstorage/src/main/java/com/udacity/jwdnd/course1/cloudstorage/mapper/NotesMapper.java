package com.udacity.jwdnd.course1.cloudstorage.mapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Notes;
import org.apache.ibatis.annotations.*;
import java.util.List;

@Mapper
public interface NotesMapper {

    @Select("SELECT * FROM notes WHERE userid = #{userId}")
    List<Notes> getAllNotes(Integer userId);

    @Select("SELECT * FROM notes WHERE noteid = #{noteId}")
    Notes getNoteById(Integer noteId);

    @Insert("INSERT INTO notes (notetitle, notedescription, userid) " +
            "VALUES (#{notes.notetitle}, #{notes.notedescription}, #{notes.userid})")
    @Options(useGeneratedKeys = true, keyProperty = "notes.noteid")
    void insertNote(@Param("notes") Notes notes);

    @Update("UPDATE notes SET notetitle = #{notes.notetitle}, " +
            "notedescription = #{notes.notedescription} WHERE noteid = #{notes.noteid}")
    void updateNote(@Param("notes") Notes notes);

    @Delete("DELETE FROM notes WHERE noteid = #{noteId}")
    void deleteNote(Integer noteId);
}

