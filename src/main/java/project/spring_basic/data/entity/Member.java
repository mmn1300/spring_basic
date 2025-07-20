package project.spring_basic.data.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.spring_basic.util.CustomAesConverter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT를 설정
    private Long id;

    @Column(name = "user_id", length = 15, nullable = false, unique = true)
    private String userId;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "nickname", length = 45, nullable = false)
    private String nickname;

    @Convert(converter = CustomAesConverter.class)
    @Column(name = "email", length = 255)
    private String email;

    @Convert(converter = CustomAesConverter.class)
    @Column(name = "phone_number", length = 255)
    private String phoneNumber;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    @Column(name = "level", nullable = false)
    private Integer level;

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Member (String userId, String password, String nickname, String email, String phoneNumber,
                    LocalDateTime createAt, Integer level){
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.createAt = createAt;
        this.level = level;
    }
}
