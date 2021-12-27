package com.ccwlab.main.mapper;

import org.apache.ibatis.annotations.*;

import javax.annotation.PostConstruct;

@Mapper
public interface RepositoryMapper {
    @Insert("create table IF NOT EXISTS repository (\n" +
            " id integer NOT NULL,\n" +
            " ownerId integer NOT NULL,\n" +
            " enabled boolean NOT NULL,\n" +
            " primary key (id, ownerId)\n" +
            ");")
    public void createTable();

    @Insert("insert into repository values(#{id},#{ownerId},#{enabled}) \n" +
            "ON CONFLICT(id, ownerId)" +
            "DO Update set enabled = #{enabled}")
    public long insert(@Param("id") long id, @Param("ownerId") long ownerId, @Param("enabled") Boolean enabled);

    @Select("select * from repository where ownerId = #{ownerId}")
    public RepositoryWithEnabled[] select(@Param("ownerId") long ownerId);

    @Update("update repository set enabled = #{enabled} where id=#{id} and ownerId=#{ownerId}")
    public long update(@Param("id") long id, @Param("ownerId") long ownerId, @Param("enabled") Boolean enabled);

    @Delete("delete from repository where id=#{id} and ownerId=#{ownerId}")
    public void delete(@Param("id") long id, @Param("ownerId") long ownerId);
}