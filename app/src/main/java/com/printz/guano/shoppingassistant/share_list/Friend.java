package com.printz.guano.shoppingassistant.share_list;

public class Friend {

    private String mName;
    private InvitationStatus mAnswer;

    public Friend(String name) {
        this.mName = name;
        this.mAnswer = InvitationStatus.Awaiting;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public InvitationStatus getAnswer() {
        return mAnswer;
    }

    public void setmAnswer(InvitationStatus answer) {
        this.mAnswer = answer;
    }
}
