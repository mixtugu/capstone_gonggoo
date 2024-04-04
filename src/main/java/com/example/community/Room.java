package com.example.community;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String name;
    private String category;

    //추가
    private String detailCategory;

    private String region;

    private String detailRegion;

    private int numberOfParticipants;
    private double payment;
    private Representative representative;
    private List<Participant> participants;

    public Room() {
        this.name = name;
        this.category = category;

        this.detailCategory= detailCategory;//추가

        this.region = region;
        this.detailRegion = detailRegion;
        this.numberOfParticipants = numberOfParticipants;
        this.payment = payment;
        this.representative = representative;
        this.participants = new ArrayList<>();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    //추가
    public String getDetailCategory() {
        return detailCategory;
    }

    public void setDetailCategory(String detailCategory) {
        this.detailCategory = detailCategory;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDetailRegion() {
        return detailRegion;
    }

    public void setDetailRegion(String detailRegion) {
        this.detailRegion = detailRegion;
    }


    public Room(String name, String category,String detailCategory, String region, String detailRegion, int numberOfParticipants, double payment, Representative representative) {
        this.name = name;
        this.category = category;
        this.detailCategory = detailCategory;//추가
        this.region = region;
        this.detailRegion = detailRegion;
        this.numberOfParticipants = numberOfParticipants;
        this.payment = payment;
        this.representative = representative;
        this.participants = new ArrayList<>();
    }

    // Getter, setter methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }

    public Representative getRepresentative() {
        return representative;
    }

    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public boolean addParticipant(Participant participant) {
        if (participants.size() < numberOfParticipants) {
            participants.add(participant);
            return true;
        }
        return false;
    }

    public boolean isParticipantExists(String participantName) {
        return participants.stream()
                .anyMatch(participant -> participant.getName().equals(participantName));
    }

    public boolean removeParticipant(String participantName) {
        return participants.removeIf(participant -> participant.getName().equals(participantName));
    }
}