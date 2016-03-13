package co.helpdesk.faveo.frontend.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.model.TicketThread;

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
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, int i) {
        TicketThread ticketThread = ticketThreadList.get(i);
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setText(Helper.parseDate(ticketThread.messageTime));
        ticketViewHolder.textViewMessageTitle.setText(ticketThread.messageTitle);
        ticketViewHolder.textViewMessage.setText(ticketThread.message.replace("<br>", "\n"));

        ticketViewHolder.thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketViewHolder.textViewMessageTitle.getVisibility() == View.GONE) {
                    ticketViewHolder.textViewMessageTitle.setVisibility(View.VISIBLE);
                    ticketViewHolder.textViewMessage.setVisibility(View.VISIBLE);
                }
                else {
                    ticketViewHolder.textViewMessageTitle.setVisibility(View.GONE);
                    ticketViewHolder.textViewMessage.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_conversation, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View thread;
        protected TextView textViewClientName;
        protected TextView textViewMessageTime;
        protected TextView textViewMessageTitle;
        protected TextView textViewMessage;

        public TicketViewHolder(View v) {
            super(v);
            thread = v.findViewById(R.id.thread);
            textViewClientName = (TextView)  v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (TextView)  v.findViewById(R.id.textView_ticket_time);
            textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            textViewMessage = (TextView) v.findViewById(R.id.textView_client_message_body);
        }

    }

}
