package com.example.demo11.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public class BaseEntity {

    @CreationTimestamp
    @Column(name="created_at",nullable = false, unique = true, updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name="edited_at" , nullable = false , unique = false , updatable = true)
    private Date editedAt;



}
