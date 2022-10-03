package com.sangboyoon.accounter.domain.corporation;

import com.sangboyoon.accounter.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "likeEntity")
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likeId")
    private Long id;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(targetEntity = Corporation.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "corpCode")
    private Corporation corporation;

    public static LikeEntity toLikeEntity(User user, Corporation corporation) {
        LikeEntity likeEntity = new LikeEntity();
        likeEntity.setCorporation(corporation);
        likeEntity.setUser(user);

        return likeEntity;
    }
}
