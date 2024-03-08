package org.example.model;

public class Participant {

    private int participantId;
    private String participantName;
    private String email;

    public int getParticipantId() {
        return participantId;
    }

    public void setParticipantId(int participantId) {
        this.participantId = participantId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }


    public Participant(int participantId, String participantName, String email) {
        this.participantId = participantId;
        this.participantName = participantName;
        this.email = email;

    }

}
