package com.example.community;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CommunityGathering {
    private String roomName;
    private String category;
    private String region;


    @Getter
    private List<CRoom> rooms;

    public CommunityGathering() {
        this.rooms = new ArrayList<>();
    }
    public void setRoomName(String roomName) {this.roomName =roomName;}

    public void setCategory(String category) {
        this.category =category;
    }
    public void setRegion(String region) {
        this.region =region;
    }

    public void createNewSharingRoom(CRoom room, Representative representative) {
        rooms.add(room);
    }



}