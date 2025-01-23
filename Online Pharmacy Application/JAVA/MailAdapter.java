package com.example.medi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MailAdapter extends RecyclerView.Adapter<MailAdapter.MailViewHolder> {

    private final List<Mail> mailList;
    private final Context context;

    public MailAdapter(List<Mail> mailList, Context context) {
        this.mailList = mailList;
        this.context = context;
    }

    @NonNull
    @Override
    public MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.mail_item, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MailViewHolder holder, int position) {
        Mail mail = mailList.get(position);
        holder.subject.setText(mail.getMessage().getSubject());
        holder.body.setText(mail.getMessage().getText());
    }

    @Override
    public int getItemCount() {
        return mailList.size();
    }

    static class MailViewHolder extends RecyclerView.ViewHolder {
        TextView email, subject, body;

        public MailViewHolder(@NonNull View itemView) {
            super(itemView);
            subject = itemView.findViewById(R.id.subjectText);
            body = itemView.findViewById(R.id.bodyText);
        }
    }
}
