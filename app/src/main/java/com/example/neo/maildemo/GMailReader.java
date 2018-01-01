package com.example.neo.maildemo;

import android.util.Log;
import javax.mail.Message;
import javax.mail.Session;
import java.util.Properties;
import javax.mail.Store;
import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;

/**
 * Created by Neo on 31/12/2017.
 */

public class GMailReader extends javax.mail.Authenticator {
    private static final String TAG = "GMailReader";

    private String mailhost = "imap.gmail.com";
    private Session session;
    private Store store;

    public GMailReader(String user, String password) {

        Properties props = System.getProperties();
        if (props == null){
            Log.e(TAG, "Properties are null !!");
        }else{
            props.setProperty("mail.store.protocol", "imaps");

            Log.d(TAG, "Transport: "+props.getProperty("mail.transport.protocol"));
            Log.d(TAG, "Store: "+props.getProperty("mail.store.protocol"));
            Log.d(TAG, "Host: "+props.getProperty("mail.imap.host"));
            Log.d(TAG, "Authentication: "+props.getProperty("mail.imap.auth"));
            Log.d(TAG, "Port: "+props.getProperty("mail.imap.port"));
        }
        try {
            session = Session.getDefaultInstance(props, null);
            store = session.getStore("imaps");
            store.connect(mailhost, user, password);
            Log.i(TAG, "Store: "+store.toString());
        } catch (NoSuchProviderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public synchronized Message[] readMail() throws Exception {
        try {
            Folder folder = store.getFolder("Inbox");
            folder.open(Folder.READ_ONLY);

        /* TODO to rework
        Message[] msgs = folder.getMessages(1, 10);
        FetchProfile fp = new FetchProfile();
        fp.add(FetchProfile.Item.ENVELOPE);
        folder.fetch(msgs, fp);
        */
            Message[] msgs = folder.getMessages();
            return msgs;
        } catch (Exception e) {
            Log.e("readMail", e.getMessage(), e);
            return null;
        }
    }

    public synchronized int getNumberOfMessages() throws Exception{
        try {
            Folder folder = store.getFolder("Inbox");
            folder.open(Folder.READ_ONLY);

            int NoOfMessages = 0;
            NoOfMessages = folder.getMessageCount();
            return NoOfMessages;
        } catch (Exception e) {
            Log.e("readMail", e.getMessage(), e);
            return -1;
        }
    }
    public synchronized int getNumberOfUnreadMessages() throws Exception{
        try {
            Folder folder = store.getFolder("Inbox");
            folder.open(Folder.READ_ONLY);

            int NoOfMessages = 0;
            NoOfMessages = folder.getUnreadMessageCount();
            return NoOfMessages;
        } catch (Exception e) {
            Log.e("readMail", e.getMessage(), e);
            return -1;
        }
    }
}