package com.example.skeletonsecurity.taskmanagement.domain;

import com.example.skeletonsecurity.base.domain.AbstractEntity;
import com.example.skeletonsecurity.security.domain.UserId;
import com.example.skeletonsecurity.security.domain.jpa.UserIdAttributeConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task extends AbstractEntity<Long> {

    public static final int DESCRIPTION_MAX_LENGTH = 255;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "description", nullable = false, length = DESCRIPTION_MAX_LENGTH)
    @Size(max = DESCRIPTION_MAX_LENGTH)
    private String description;

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    private Instant createdDate;

    @Column(name = "created_by")
    @Convert(converter = UserIdAttributeConverter.class)
    @CreatedBy
    private UserId createdBy;

    @Column(name = "due_date")
    @Nullable
    private LocalDate dueDate;

    @Override
    public @Nullable Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public @Nullable UserId getCreatedBy() {
        return createdBy;
    }

    public @Nullable LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(@Nullable LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
