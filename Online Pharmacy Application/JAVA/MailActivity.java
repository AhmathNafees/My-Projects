package com.example.medi;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.app.NotificationManager;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class MailActivity extends AppCompatActivity {

    private RecyclerView mailRecyclerView;
    private MailAdapter mailAdapter;
    private List<Mail> mailList = new ArrayList<>();
    private FirebaseFirestore db;

    private static final String CHANNEL_ID = "MailNotificationsChannel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mailRecyclerView = findViewById(R.id.mailRecyclerView);
        mailRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        createNotificationChannel();

        // Example: Trigger a notification
        showNotification("Test Notification", "Notifications are working!");

        fetchEmails();
        listenForNewMails();
    }

    private void fetchEmails() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get the current user's UID

        db.collection("mail")
                .whereEqualTo("receiverUid", currentUserUid) // Query to fetch mails for the current user
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mailList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Mail mail = doc.toObject(Mail.class);
                        mailList.add(mail);
                    }
                    mailAdapter = new MailAdapter(mailList, this);
                    mailRecyclerView.setAdapter(mailAdapter);
                })
                .addOnFailureListener(e -> Log.e("MailActivity", "Error fetching emails", e));
    }

    private void listenForNewMails() {
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("mail")
                .whereEqualTo("receiverUid", currentUserUid)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Log.e("FirestoreListener", "Error listening for new emails", e);
                        return;
                    }

                    if (snapshots != null) {
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // New mail document added
                                String subject = dc.getDocument().getString("message.subject");
                                String body = dc.getDocument().getString("message.text");
                                String title = subject != null ? subject : "New Mail Received!";

                                showNotification(title, body != null ? body : "You have received a new mail.");
                            }
                        }
                    }
                });
    }


    private void showNotification(String title, String message) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager == null) {
            Log.e("NotificationError", "NotificationManager is null. Ensure the context is valid.");
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.img_3) // Replace with your app's notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        int notificationId = (int) System.currentTimeMillis(); // Unique ID for each notification
        notificationManager.notify(notificationId, builder.build());
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Mail Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Notifications for new mails");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

}
