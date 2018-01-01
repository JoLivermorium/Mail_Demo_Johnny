package com.example.neo.maildemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button; //button widget
import android.util.Log;
import android.widget.TextView;// text widget
import android.os.Handler;// thread UI control handler
import javax.mail.Message;
import javax.mail.Folder;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Connecting to server...");
        try {
            GMailSender sender = new GMailSender("johnny.zy.lyu@gmail.com",
                    "LZY84267193lzy");
        } catch (Exception e) {
            System.out.println("Fail to connect sender server!");
        }
        try {
            GMailReader reader = new GMailReader("johnny.zy.lyu@gmail.com",
                    "LZY84267193lzy");
        } catch (Exception e) {
            System.out.println("Fail to connect receiver server!");
        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // give text a reference
        final TextView text1 = (TextView) findViewById(R.id.text1);

        //set new buttons, give this button a reference, which is the button I put in the activity_main.xml
        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);

        // add listener to button1
        button1.setOnClickListener(
                //start a new object of listeners
                new Button.OnClickListener(){
                    // override the callback function; press control+I to see
                    @Override
                    public void onClick(View v) {
                        // do something when clicking the button
                        final Handler handler = new Handler(); // need a handler to change the UI(TextView) in thread
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GMailSender sender = new GMailSender("johnny.zy.lyu@gmail.com",
                                            "LZY84267193lzy");
                                    sender.sendMail("Johnny Rocks!", "Hello world! form johnny",
                                            "johnny.zy.lyu@gmail.com", "johnny.zy.lyu@gmail.com");
                                    // if scceed, change the text into succeed
                                    handler.post(new Runnable(){
                                        public void run() {
                                            text1.setText("SENT!");
                                        }
                                    });
                                } catch (Exception e) {
                                    Log.e("SendMail", e.getMessage(), e);
                                    // if fail, change the text into fail
                                    handler.post(new Runnable(){
                                        public void run() {
                                            text1.setText("SEND FAIL!");
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
        );
        // add listener to button2
        button2.setOnClickListener(
                new Button.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        text1.setText("Checking for new messages...");
                        final Handler handler = new Handler();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    GMailReader reader = new GMailReader("johnny.zy.lyu@gmail.com", "LZY84267193lzy");
                                    Message[] messages = reader.readMail();
                                    System.out.println("No of Messages : " + reader.getNumberOfMessages());
                                    System.out.println("No of Unread Messages : " + reader.getNumberOfUnreadMessages());
                                    handler.post(new Runnable(){
                                        public void run() {
                                            text1.setText("RECEIVE SUCCEED!");
                                        }
                                    });
                                    //System.out.println(msg.getSubject());

                                } catch (Exception e) {
                                    Log.e("ReadMail", e.getMessage(), e);
                                    text1.setText("");
                                    handler.post(new Runnable(){
                                        public void run() {
                                            text1.setText("RECEIVE FAIL!");
                                        }
                                    });
                                }
                            }
                        }).start();
                    }
                }
        );

    }
}
