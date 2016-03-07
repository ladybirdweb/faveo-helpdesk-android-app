package com.android.faveo.frontend.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.faveo.Helper;
import com.android.faveo.R;
import com.android.faveo.frontend.activities.TicketDetailActivity;
import com.android.faveo.model.TicketGlimpse;
import com.android.faveo.model.TicketThread;

import java.util.List;

public class TicketThreadAdapter extends RecyclerView.Adapter<TicketThreadAdapter.TicketViewHolder> {
    private List<TicketThread> ticketThreadList;

    public TicketThreadAdapter(List<TicketThread> ticketThreadList) {
        this.ticketThreadList = ticketThreadList;
    }

    @Override
    public int getItemCount() {
        return ticketThreadList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        TicketThread ticketThread = ticketThreadList.get(i);
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setText(Helper.parseDate(ticketThread.messageTime));
        ticketViewHolder.textViewMessageTitle.setText(ticketThread.messageTitle);
        ticketViewHolder.textViewMessage.setText(ticketThread.message.replace("<br>", "\n"));
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_conversation, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected TextView textViewClientName;
        protected TextView textViewMessageTime;
        protected TextView textViewMessageTitle;
        protected TextView textViewMessage;

        public TicketViewHolder(View v) {
            super(v);
            textViewClientName = (TextView)  v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (TextView)  v.findViewById(R.id.textView_ticket_time);
            textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            textViewMessage = (TextView) v.findViewById(R.id.textView_client_message_body);
        }

    }

}
