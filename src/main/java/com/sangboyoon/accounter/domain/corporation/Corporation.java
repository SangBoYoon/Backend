package com.sangboyoon.accounter.domain.corporation;

import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Corporation")
public class Corporation {
    @Id
    @Column(name = "corpCode", nullable = false, unique = true)
    private String corpCode;

    @Column(name = "corpName")
    private String corpName;

    @Column(name = "corpCategory")
    private String corpCategory;

    @Column(name = "corpLike")
    private Long corpLike;
}
