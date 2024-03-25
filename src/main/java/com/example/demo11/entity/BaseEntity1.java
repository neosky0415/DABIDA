package com.example.demo11.entity;


import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AutoCloseable.class)
public class BaseEntity1 {
    @CreationTimestamp
    @Column(name="tested_at" ,nullable = false , unique = false ,updatable = false)
    private Date testedAt;
}
