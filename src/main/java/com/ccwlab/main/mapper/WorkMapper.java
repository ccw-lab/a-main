package com.ccwlab.main.mapper;

import com.ccwlab.main.work.Work;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface WorkMapper {
    @Insert("create table IF NOT EXISTS work (\n" +
            "    repositoryId integer NOT NULL,\n" +
            "    repositoryName varchar(100) NOT NULL,\n" +
            "    commitId varchar(100) NOT NULL,\n" +
            "    commitMessage varchar(100) NOT NULL,\n" +
            "    startedTime TIMESTAMPTZ,\n" +
            "    stoppedTime TIMESTAMPTZ,\n" +
            "    completedTime TIMESTAMPTZ,\n" +
            "    failedTime TIMESTAMPTZ,\n" +
            "    status varchar(100) NOT NULL,\n" +
            "    workId integer NOT NULL CONSTRAINT primary_constraint PRIMARY KEY,\n" +
            "    workProgressURI varchar(100) NOT NULL\n" +
            ");")
    public void createTable();

    @Insert("INSERT INTO WORK\n" +
            "(repositoryId,\n" +
            "    repositoryName,\n" +
            "    commitId,\n" +
            "    commitMessage,\n" +
            "    startedTime,\n" +
            "    stoppedTime,\n" +
            "    completedTime,\n" +
            "    failedTime,\n" +
            "    status,\n" +
            "    workId,\n" +
            "    workProgressURI) \n" +
            "VALUES (\n" +
            "    #{repositoryId},\n" +
            "    #{repositoryName},\n" +
            "    #{commitId},\n" +
            "    #{commitMessage},\n" +
            "    #{startedTime},\n" +
            "    #{stoppedTime},\n" +
            "    #{completedTime},\n" +
            "    #{failedTime},\n" +
            "    #{status},\n" +
            "    #{workId},\n" +
            "    #{workProgressURI})\n" +
            "ON CONFLICT(workId)\n" +
            "DO UPDATE SET \n" +
            "    startedTime = #{startedTime},\n" +
            "    completedTime = #{completedTime},\n" +
            "    stoppedTime = #{stoppedTime},\n" +
            "    failedTime = #{failedTime},\n" +
            "    status = #{status}")
    public void insert(Work work);

    @Select("select " +
            "repositoryId,\n" +
            "repositoryName,\n" +
            "commitId,\n" +
            "commitMessage,\n" +
            "startedTime,\n" +
            "stoppedTime,\n" +
            "completedTime,\n" +
            "failedTime,\n" +
            "status,\n" +
            "workId,\n" +
            "workProgressURI from work where workId = #{workId}")
    public List<Work> selectByWorkId(long workId);


    @Select("select " +
            "repositoryId,\n" +
            "repositoryName,\n" +
            "commitId,\n" +
            "commitMessage,\n" +
            "startedTime,\n" +
            "stoppedTime,\n" +
            "completedTime,\n" +
            "failedTime,\n" +
            "status,\n" +
            "workId,\n" +
            "workProgressURI from work where repositoryId = #{repositoryId}")
    public List<Work> selectByWorkRepositoryId(long repositoryId);

    @Select("select " +
            "repositoryId,\n" +
            "repositoryName,\n" +
            "commitId,\n" +
            "commitMessage,\n" +
            "startedTime,\n" +
            "stoppedTime,\n" +
            "completedTime,\n" +
            "failedTime,\n" +
            "status,\n" +
            "workId,\n" +
            "workProgressURI from work where repositoryName = #{repoName} and commitId = #{commitId} and status = 'STARTED'")
    public List<Work> selectByRepoNameAndCommitId(String repoName, String commitId);
}
