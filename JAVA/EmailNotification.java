package com.example.medi;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class EmailNotification {

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Method to send email with sender and receiver UID
    public void sendEmailWithSenderReceiver(String senderUid, String receiverUid, String recipientEmail, String subject, String body) {
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("to", recipientEmail); // Recipient email
        emailData.put("senderUid", senderUid); // Sender UID
        emailData.put("receiverUid", receiverUid); // Receiver UID
        emailData.put("message", new HashMap<String, Object>() {{
            put("subject", subject);
            put("text", body);
        }});

        db.collection("mail")
                .add(emailData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println("Email queued for sending: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    System.err.println("Error adding email to Firestore: " + e.getMessage());
                });
    }
}
