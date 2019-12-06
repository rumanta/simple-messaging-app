package id.ac.ui.cs.williamrumanta.directnotification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    protected ChatNotificationManager notificationManager;
    protected MessageListAdapter adapter;
    private RecyclerView mMessageRecycler;

    ArrayList<UserMessage> messageHistory = new ArrayList<>();
    private TextView chatBoxMessage;
    private ImageButton sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        initData();

        mMessageRecycler = findViewById(R.id.reyclerview_message_list);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MessageListAdapter(MainActivity.this, messageHistory);
        mMessageRecycler.setAdapter(adapter);

        chatBoxMessage = findViewById(R.id.edittext_chatbox);
        sendBtn = findViewById(R.id.button_chatbox_send);

        notificationManager = new ChatNotificationManager(this);

        notificationManager.clearExistingNotifications(getIntent());

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = chatBoxMessage.getText().toString();
                messageHistory.add(createMessageUser(message));
                notificationManager.sendNotification(message);

                chatBoxMessage.setText("");
                hideSoftKeyboard(MainActivity.this, chatBoxMessage);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        notificationManager.handleIntent(intent, messageHistory);
        adapter.notifyDataSetChanged();
    }

    public UserMessage createMessageUser(String msg) {
        return new UserMessage("William", msg);
    }

    public static void hideSoftKeyboard (Activity activity, View view) {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    public void initData() {
        messageHistory.add(new UserMessage("Klaud", "Hai apa kabar?"));
    }
}
