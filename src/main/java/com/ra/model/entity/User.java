package com.ra.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false, length = 100)
    private String fullName;

    private Boolean status;

    @Column(nullable = false)
    private String password;

//    @Column(columnDefinition = "TEXT DEFAULT 'https://th.bing.com/th/id/OIP.kQyrx9VbuWXWxCVxoreXOgHaHN?rs=1&pid=ImgDetMain'")
    private String avatar;

    private String phone;

    private String address;

    @Column(nullable = false)
    private Date createdAt;

    private Date updatedAt;

    private Boolean isDeleted;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roles;
}
