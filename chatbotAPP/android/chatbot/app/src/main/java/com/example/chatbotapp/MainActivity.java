package com.example.chatbotapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Message;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String channelID = "CHANNEL_ID_FROM_YOUR_SCALEDRONE_DASHBOARD";
    private String roomName = "observable-room";
    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private Dialog tabledlg;
    private String studentName;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 값 받기
        Intent intent = getIntent();
        studentName = intent.getExtras().getString("name");


        // This is where we write the mesage
        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        MemberData data = new MemberData(getRandomName(), getRandomColor());

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                //scaledrone.subscribe(roomName, MainActivity.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });

        String startMsg = "안녕하세요 " + studentName + "님 " + "무엇이든 물어봇!\n\n<도움말>\n말풍선을 누르면 도움을 얻을 수 있어요";
        com.example.chatbotapp.Message msg = new com.example.chatbotapp.Message(startMsg, new MemberData("무엇이든물어봇", null), false);
        messageAdapter.add(msg);
        messagesView.setSelection(messagesView.getCount() - 1);
        editText.getText().clear();

        // 표 다이얼로그 초기화
        tabledlg = new Dialog(MainActivity.this);
        tabledlg.setContentView(R.layout.table_view);

        // 표 동적 추가 해보자
        // TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);
        // TableRow row = (TableRow)findViewById(R.id.table_row);
        TableLayout tableLayout = new TableLayout(this);
        tableLayout.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        TextView text = new TextView(MainActivity.this);
        text.setText("전필");
        text.setWidth(1000/4);
        text.setGravity(Gravity.CENTER);
        TextView text2 = new TextView(MainActivity.this);
        text2.setText("전선");
        text2.setWidth(1000/4);
        text2.setGravity(Gravity.CENTER);
        TextView text3 = new TextView(MainActivity.this);
        text3.setText("중필");
        text3.setWidth(1000/4);
        text3.setGravity(Gravity.CENTER);
        TextView text4 = new TextView(MainActivity.this);
        text4.setText("중선");
        text4.setWidth(1000/4);
        text4.setGravity(Gravity.CENTER);

        row.addView(text);
        row.addView(text2);
        row.addView(text3);
        row.addView(text4);

        tableLayout.addView(row);

        tabledlg.addContentView(tableLayout, new TableRow.LayoutParams( TableRow.LayoutParams.FILL_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

        messagesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tabledlg.show();
            }
        });

        System.out.println("before!!!!!!!!!!!!!!!!!!!!!!!!!!!!");


        new Thread() {
            public void run() {
                ChatRequest chat = new ChatRequest();
                chat.doInBackground("http://jonathan97son.iptime.org:8080/");
            }
        }.start();
        System.out.println("After!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
    }

//    // Successfully connected to Scaledrone room
//    @Override
//    public void onOpen(Room room) {
//        System.out.println("Conneted to room");
//    }
//
//    // Connecting to Scaledrone room failed
//    @Override
//    public void onOpenFailure(Room room, Exception ex) {
//        System.err.println(ex);
//    }
//
//    // Received a message from Scaledrone room
//    @Override
//    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
//        final ObjectMapper mapper = new ObjectMapper();
//        try {
//            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
//            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
//            final com.example.chatbotapp.Message message = new com.example.chatbotapp.Message(receivedMessage.getData().asText(), data, belongsToCurrentUser);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    messageAdapter.add(message);
//                    messagesView.setSelection(messagesView.getCount() - 1);
//                }
//            });
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//    }

    private String getRandomName() {
        String[] adjs = {"autumn", "hidden", "bitter", "misty", "silent", "empty", "dry", "dark", "summer", "icy", "delicate", "quiet", "white", "cool", "spring", "winter", "patient", "twilight", "dawn", "crimson", "wispy", "weathered", "blue", "billowing", "broken", "cold", "damp", "falling", "frosty", "green", "long", "late", "lingering", "bold", "little", "morning", "muddy", "old", "red", "rough", "still", "small", "sparkling", "throbbing", "shy", "wandering", "withered", "wild", "black", "young", "holy", "solitary", "fragrant", "aged", "snowy", "proud", "floral", "restless", "divine", "polished", "ancient", "purple", "lively", "nameless"};
        String[] nouns = {"waterfall", "river", "breeze", "moon", "rain", "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf", "dawn", "glitter", "forest", "hill", "cloud", "meadow", "sun", "glade", "bird", "brook", "butterfly", "bush", "dew", "dust", "field", "fire", "flower", "firefly", "feather", "grass", "haze", "mountain", "night", "pond", "darkness", "snowflake", "silence", "sound", "sky", "shape", "surf", "thunder", "violet", "water", "wildflower", "wave", "water", "resonance", "sun", "wood", "dream", "cherry", "tree", "fog", "frost", "voice", "paper", "frog", "smoke", "star"};
        return (
                adjs[(int) Math.floor(Math.random() * adjs.length)] +
                        "_" +
                        nouns[(int) Math.floor(Math.random() * nouns.length)]
        );
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            com.example.chatbotapp.Message msg = new com.example.chatbotapp.Message(message, new MemberData("병신", null), true);
            messageAdapter.add(msg);
            messagesView.setSelection(messagesView.getCount() - 1);
            //scaledrone.publish("observable-room", message);
            editText.getText().clear();
        }
    }

    @Override
    public void onClick(View v) {

    }
}

class MemberData {
    private String name;
    private String color;

    public MemberData(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public MemberData() {
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "MemberData{" +
                "name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
}