package com.example.userservice.entity;

import com.example.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name="MEMBER")
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
    @Id
    private Long memberId;

    @NotEmpty
    @Column(unique = true)
    private String loginId;

    @NotEmpty
    private String password;


    private String memberType;

    private String sessionId;

    @Column(length = 20)
    private String queryType;

    @Column(length = 20)
    private String fiterType;

//    public Boolean checkPassword(String reqPssword, PasswordEncoder passwordEncoder) {
//        if (passwordEncoder.hashPassword(reqPssword).equals(this.password)) {
//            return true;
//        } else {
//            return false;
//        }
//    }
    public static Member register(Long id, String loginId, String password, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.memberId = id;
        member.loginId = loginId;
        member.password = passwordEncoder.hashPassword(password);
        return member;
    }

    public void modify(String password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.hashPassword(password);
    }

    public static Member registerForModify(String loginId, String password, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.loginId = loginId;
        member.password = passwordEncoder.hashPassword(password);
        member.setUpdated(LocalDateTime.now());
        return member;
    }


    public boolean checkPassword(String passwordReq, PasswordEncoder passwordEncoder) {
        return passwordEncoder.checkPassword(passwordReq, this.password);
    }
}
