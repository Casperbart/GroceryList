package com.printz.guano.shoppingassistant.share_list;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.printz.guano.shoppingassistant.BaseActivity;
import com.printz.guano.shoppingassistant.R;
import com.printz.guano.shoppingassistant.misc.DummyClass;

import java.util.List;

public class ShareActivity extends BaseActivity {

    private final static String LOG_TAG = ShareActivity.class.getSimpleName();
    private ListView mFriendsList;
    private Button mSendInvitationButton;
    private FragmentManager mFragmentManager;
    private EditText mSendInvitationText;
    private List<Friend> mFriends;
    private ShareFriendAdapter mShareFriendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        activateToolbarWithHomeEnabled();
        mFragmentManager = getSupportFragmentManager();

        mShareFriendAdapter = new ShareFriendAdapter(this);
        mFriendsList = (ListView) findViewById(R.id.listViewFriends);
        mSendInvitationButton = (Button) findViewById(R.id.buttonSendInvitation);
        mSendInvitationText = (EditText) findViewById(R.id.editTextSendInviation);

        mFriendsList.setAdapter(mShareFriendAdapter);

        mSendInvitationText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                mSendInvitationText.setCursorVisible(false);
                // hitting green checkmark button on keyboard
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handled = true;
                }
                return handled;
            }
        });

        mSendInvitationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendInvitationText.setCursorVisible(true);
            }
        });

        mSendInvitationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ShareActivity.this, "Not yet implemented", Toast.LENGTH_SHORT).show();
            }
        });

        DummyClass.setDummyFriends(mShareFriendAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
