package com.example.community;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
public class Participant {
    private String loginId;
    private String password;
    private String name;
}
