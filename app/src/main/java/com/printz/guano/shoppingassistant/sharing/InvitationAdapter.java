package com.printz.guano.shoppingassistant.sharing;

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

public class InvitationAdapter extends ArrayAdapter<Invitation> {

    private static final String LOG_TAG = InvitationAdapter.class.getSimpleName();
    private LayoutInflater mLayoutInflater;

    public InvitationAdapter(Context context) {
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

        final Invitation invitation = getItem(position);
        final String name = invitation.getName();
        final InvitationStatus status = invitation.getStatus();

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

    public void setData(List<Invitation> invitations) {
        clear();
        if (invitations != null) {
            for (Invitation invitation : invitations) {
                add(invitation);
            }
        }
    }
}
