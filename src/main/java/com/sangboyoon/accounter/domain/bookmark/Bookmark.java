package com.sangboyoon.accounter.domain.bookmark;

import com.sangboyoon.accounter.domain.corporation.Corporation;
import com.sangboyoon.accounter.domain.user.User;
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
@Table(name = "Bookmark")
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
    @JoinColumn(name = "userId")
    private User user;

    //공시 대상 회사의 고유 번호
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Corporation.class)
    @JoinColumn(name= "corpCode")
    private Corporation corporation;
}
