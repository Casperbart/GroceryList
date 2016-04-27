package com.printz.guano.shoppingassistant.sharing;

public class Invitation {

    private int mInvitationId;
    private String mName;
    private InvitationStatus mStatus;

    public Invitation(int invitationId, String name) {
        this.mInvitationId = invitationId;
        this.mName = name;
        this.mStatus = InvitationStatus.AWAITING;
    }

    public int getInvitationId() {
        return mInvitationId;
    }

    public void setInvitationId(int invitationId) {
        this.mInvitationId = invitationId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public InvitationStatus getStatus() {
        return mStatus;
    }

    public void setStatus(InvitationStatus status) {
        this.mStatus = status;
    }
}
