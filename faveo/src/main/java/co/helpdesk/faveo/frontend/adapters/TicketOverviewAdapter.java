package co.helpdesk.faveo.frontend.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.model.TicketOverview;

public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
    private List<TicketOverview> ticketOverviewList;

    public TicketOverviewAdapter(List<TicketOverview> ticketOverviewList) {
        this.ticketOverviewList = ticketOverviewList;
    }

    @Override
    public int getItemCount() {
        return ticketOverviewList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        final TicketOverview ticketOverview = ticketOverviewList.get(i);
        ticketViewHolder.textViewNewNotification.setText(ticketOverview.ticketBubble);
        ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");
        ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
        ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);
        ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
        ticketViewHolder.textViewTime.setText(Helper.parseDate(ticketOverview.ticketTime));
        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
                    .load(ticketOverview.clientPicture)
                    .placeholder(R.drawable.default_pic)
                    .error(R.drawable.default_pic)
                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                intent.putExtra("TICKET_ID", ticketOverview.ticketID + "");
                intent.putExtra("TICKET_NUMBER", ticketOverview.ticketNumber);
                intent.putExtra("TICKET_OPENED_BY", ticketOverview.clientName);
                intent.putExtra("TICKET_SUBJECT", ticketOverview.ticketSubject);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_ticket, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View ticket;
        protected RoundedImageView roundedImageViewProfilePic;
        protected TextView textViewTicketID;
        protected TextView textViewTicketNumber;
        protected TextView textViewClientName;
        protected TextView textViewSubject;
        protected TextView textViewTime;
        protected TextView textViewNewNotification;

        public TicketViewHolder(View v) {
            super(v);
            ticket =  v.findViewById(R.id.ticket);
            roundedImageViewProfilePic =  (RoundedImageView) v.findViewById(R.id.imageView_default_profile);
            textViewTicketID = (TextView)  v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView)  v.findViewById(R.id.textView_ticket_number);
            textViewClientName = (TextView)  v.findViewById(R.id.textView_client_name);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            textViewTime = (TextView) v.findViewById(R.id.textView_ticket_time);
            textViewNewNotification = (TextView) v.findViewById(R.id.textView_ticket_bubble);
        }

    }

}
