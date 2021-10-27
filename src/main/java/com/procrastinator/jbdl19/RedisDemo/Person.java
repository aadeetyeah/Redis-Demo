package com.procrastinator.jbdl19.RedisDemo;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person implements Serializable {
    private long Id;
    private String name;
    private double credit_score;
    private int age;
    private boolean seniorCitizen;
}
