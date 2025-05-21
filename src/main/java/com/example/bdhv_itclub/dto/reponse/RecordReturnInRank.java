package com.example.bdhv_itclub.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecordReturnInRank {

    private String username;

    @JsonProperty("avatar_user")
    private String avatarUser;

    @JsonProperty("joined_at")
    private Instant joinedAt;

    private float grade;

    private int period;

}
