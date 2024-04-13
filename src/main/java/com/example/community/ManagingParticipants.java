package com.example.community;

import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class ManagingParticipants {
    private CommunityGathering communityGathering;

    public ManagingParticipants(CommunityGathering communityGathering) {
        this.communityGathering = communityGathering;
    }

    public void manageParticipantRooms(CRoom selectedRoom) {
        Scanner scanner = new Scanner(System.in);
    }
}