package com.android.faveo.frontend.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.faveo.R;
import com.android.faveo.frontend.activities.TicketDetailActivity;
import com.android.faveo.model.TicketGlimpse;

import java.util.List;

public class TicketGlimpseAdapter extends RecyclerView.Adapter<TicketGlimpseAdapter.TicketViewHolder> {
    private List<TicketGlimpse> ticketGlimpseList;

    public TicketGlimpseAdapter(List<TicketGlimpse> ticketGlimpseList) {
        this.ticketGlimpseList = ticketGlimpseList;
    }

    @Override
    public int getItemCount() {
        return ticketGlimpseList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        TicketGlimpse ticketGlimpse = ticketGlimpseList.get(i);
        ticketViewHolder.textViewTicketID.setText(ticketGlimpse.ticketID + "");
        ticketViewHolder.textViewTicketNumber.setText(ticketGlimpse.ticketNumber);
        ticketViewHolder.textViewSubject.setText(ticketGlimpse.ticketSubject);
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_ticket_status, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected TextView textViewTicketID;
        protected TextView textViewTicketNumber;
        protected TextView textViewSubject;

        public TicketViewHolder(View v) {
            super(v);
            textViewTicketID = (TextView)  v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView)  v.findViewById(R.id.textView_ticket_number);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                    intent.putExtra("TICKET_ID", textViewTicketID.getText().toString());
                    intent.putExtra("TICKET_NUMBER", textViewTicketNumber.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });

        }

    }

}
