package com.kaoyan.system.module.goal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("goal")
public class Goal {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private Integer examType;
    private String targetName;
    private String major;
    private String position;
    private String subjects;
    private String targetScore;
    private LocalDate examDate;
    private Integer status;
    private Integer deleted;
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getExamType() { return examType; }
    public void setExamType(Integer examType) { this.examType = examType; }
    public String getTargetName() { return targetName; }
    public void setTargetName(String targetName) { this.targetName = targetName; }
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getSubjects() { return subjects; }
    public void setSubjects(String subjects) { this.subjects = subjects; }
    public String getTargetScore() { return targetScore; }
    public void setTargetScore(String targetScore) { this.targetScore = targetScore; }
    public LocalDate getExamDate() { return examDate; }
    public void setExamDate(LocalDate examDate) { this.examDate = examDate; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getDeleted() { return deleted; }
    public void setDeleted(Integer deleted) { this.deleted = deleted; }
    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
