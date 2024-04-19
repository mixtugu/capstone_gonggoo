package com.example.taxi.dto;


import com.example.domain.Member;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String loginId;
    private String password;
    private String name;

    public Member toEntity() {
        Member build = Member.builder()
                .id(id)
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
        return build;
    }

    @Builder
    public MemberDto(Long id, String loginId, String password, String name) {
        this.id = id;
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }
}
