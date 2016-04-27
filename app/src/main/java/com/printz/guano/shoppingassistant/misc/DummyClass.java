package com.printz.guano.shoppingassistant.misc;

import com.printz.guano.shoppingassistant.sharing.Invitation;
import com.printz.guano.shoppingassistant.sharing.InvitationAdapter;

import java.util.ArrayList;
import java.util.List;

public class DummyClass {

    public static void setDummyFriends(InvitationAdapter adapter) {
        List<Invitation> dummyInvitations = new ArrayList<>();
        Invitation invitation1 = new Invitation(0, "1");
        Invitation invitation2 = new Invitation(2, "2");
        Invitation invitation3 = new Invitation(3, "3");
        Invitation invitation4 = new Invitation(4, "4");
        Invitation invitation5 = new Invitation(5, "5");

        dummyInvitations.add(invitation1);
        dummyInvitations.add(invitation2);
        dummyInvitations.add(invitation3);
        dummyInvitations.add(invitation4);
        dummyInvitations.add(invitation5);

        adapter.setData(dummyInvitations);
    }
}




