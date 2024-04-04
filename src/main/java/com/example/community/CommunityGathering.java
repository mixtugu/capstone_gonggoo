package com.example.community;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


@Component
public class CommunityGathering {
    private String name;
    private String category;
    private String region;


    @Getter
    private List<Room> rooms;

    public CommunityGathering() {
        this.rooms = new ArrayList<>();
    }
    public void setName(String name) {this.name =name;}

    public void setCategory(String category) {
        this.category =category;
    }
    public void setRegion(String region) {
        this.region =region;
    }

    public void createNewSharingRoom(Room room, Representative representative) {
        rooms.add(room);
    }



}