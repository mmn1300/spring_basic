package project.spring_basic.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT를 설정
    private Long id;

    @Column(name = "user_id", length = 15, nullable = false, unique = true)
    private String userId;

    @Column(name = "password", length = 15, nullable = false)
    private String password;

    @Column(name = "nickname", length = 45, nullable = false)
    private String nickname;

    @Column(name = "email", length = 80)
    private String email;

    @Column(name = "phone_number", length = 13)
    private String phoneNumber;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "level", nullable = false)
    private Integer level;
}
