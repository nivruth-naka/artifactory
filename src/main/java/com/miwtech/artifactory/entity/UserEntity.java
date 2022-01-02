package com.miwtech.artifactory.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity {

    @Id
    private String username;

    private String password;

}
