package com.printz.guano.shoppingassistant.share_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.printz.guano.shoppingassistant.R;

import java.util.List;

public class ShareFriendAdapter extends ArrayAdapter<Friend> {

    private static final String LOG_TAG = ShareFriendAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;

    public ShareFriendAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;
        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.item_friend, parent, false);
        } else {
            view = convertView;
        }

        final Friend friend = getItem(position);
        final String name = friend.getName();
        final InvitationStatus status = friend.getAnswer();

        TextView friendPrimaryText = (TextView) view.findViewById(R.id.textViewFriendPrimary);
        TextView friendSecondaryText = (TextView) view.findViewById(R.id.textViewFriendSecondary);
        ImageButton removeFriendButton = (ImageButton) view.findViewById(R.id.imageButtonRemoveFriend);

        friendPrimaryText.setText(name);
        friendSecondaryText.setText(status.toString());

        removeFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void setData(List<Friend> friends) {
        clear();
        if (friends != null) {
            for (Friend friend : friends) {
                add(friend);
            }
        }
    }
}
