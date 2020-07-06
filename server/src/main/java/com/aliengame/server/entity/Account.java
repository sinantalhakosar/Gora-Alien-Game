package com.aliengame.server.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "account", schema = "database_group13")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id", unique = true, nullable = false)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email", nullable = false)
    @Email(message = "Please provide a valid email")
    @NotNull(message = "Please provide a valid email")
    private String email;

    @Column(name = "username", unique = true, nullable = false)
    @NotNull(message = "Please enter unique username")
    private String username;

    @Column(name = "password", nullable = false)
    @NotNull(message = "Please enter password")
    @Length(min = 5, message = "Please Enter Your Password")
    private String password;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Status status = Status.valueOf("OFFLINE");

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany
    @JoinColumn(name = "account_id")
    private List<Score> scoreList = new ArrayList<>();

}
