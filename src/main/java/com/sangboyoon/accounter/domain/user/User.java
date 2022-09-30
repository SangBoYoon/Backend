package com.sangboyoon.accounter.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sangboyoon.accounter.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "User")
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = true)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = true)
    private String role;

    public void update(String password, String nickName) {
        this.password = password;
        this.nickName = nickName;
    }
}
