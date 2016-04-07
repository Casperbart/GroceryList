package com.printz.guano.shoppingassistant.misc;

import com.printz.guano.shoppingassistant.share_list.Friend;
import com.printz.guano.shoppingassistant.share_list.ShareFriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class DummyClass {

    public static void setDummyFriends(ShareFriendAdapter adapter) {
        List<Friend> dummyFriends = new ArrayList<>();
        Friend friend1 = new Friend("1");
        Friend friend2 = new Friend("2");
        Friend friend3 = new Friend("3");
        Friend friend4 = new Friend("4");
        Friend friend5 = new Friend("5");
        Friend friend6 = new Friend("6");
        Friend friend7 = new Friend("7");
        Friend friend8 = new Friend("8");
        Friend friend9 = new Friend("9");
        Friend friend10 = new Friend("10");
        Friend friend11 = new Friend("11");
        Friend friend12 = new Friend("12");
        Friend friend13 = new Friend("13");
        Friend friend14 = new Friend("14");
        Friend friend15 = new Friend("15");
        Friend friend16 = new Friend("16");
        Friend friend17 = new Friend("17");

        dummyFriends.add(friend1);
        dummyFriends.add(friend2);
        dummyFriends.add(friend3);
        dummyFriends.add(friend4);
        dummyFriends.add(friend5);
        dummyFriends.add(friend6);
        dummyFriends.add(friend7);
        dummyFriends.add(friend8);
        dummyFriends.add(friend9);
        dummyFriends.add(friend10);
        dummyFriends.add(friend11);
        dummyFriends.add(friend12);
        dummyFriends.add(friend13);
        dummyFriends.add(friend14);
        dummyFriends.add(friend15);
        dummyFriends.add(friend16);
        dummyFriends.add(friend17);

        adapter.setData(dummyFriends);
    }
}




