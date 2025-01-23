package com.example.medi;

import com.google.firebase.firestore.PropertyName;

public class Mail {
    private String to;
    private String senderUid;
    private String receiverUid;
    private Message message;

    @PropertyName("to")
    public String getTo() {
        return to;
    }

    @PropertyName("to")
    public void setTo(String to) {
        this.to = to;
    }

    @PropertyName("senderUid")
    public String getSenderUid() {
        return senderUid;
    }

    @PropertyName("senderUid")
    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    @PropertyName("receiverUid")
    public String getReceiverUid() {
        return receiverUid;
    }

    @PropertyName("receiverUid")
    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    @PropertyName("message")
    public Message getMessage() {
        return message;
    }

    @PropertyName("message")
    public void setMessage(Message message) {
        this.message = message;
    }

    public static class Message {
        private String subject;
        private String text;

        @PropertyName("subject")
        public String getSubject() {
            return subject;
        }

        @PropertyName("subject")
        public void setSubject(String subject) {
            this.subject = subject;
        }

        @PropertyName("text")
        public String getText() {
            return text;
        }

        @PropertyName("text")
        public void setText(String text) {
            this.text = text;
        }
    }
}
