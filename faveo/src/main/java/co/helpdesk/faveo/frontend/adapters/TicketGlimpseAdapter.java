package co.helpdesk.faveo.frontend.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pixplicity.easyprefs.library.Prefs;

import java.util.List;

import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.model.TicketGlimpse;

/**
 * This adapter is for the Open/Close page in the client detail page.
 */
public class TicketGlimpseAdapter extends RecyclerView.Adapter<TicketGlimpseAdapter.TicketViewHolder> {
    private List<TicketGlimpse> ticketGlimpseList;
    private final String clientName;

    public TicketGlimpseAdapter(List<TicketGlimpse> ticketGlimpseList, String clientName) {
        this.ticketGlimpseList = ticketGlimpseList;
        this.clientName = clientName;
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
        if (ticketGlimpse.isTicketOpen)
            ticketViewHolder.color.setBackgroundColor(Color.parseColor("#4CD964"));
        else
            ticketViewHolder.color.setBackgroundColor(Color.parseColor("#d50000"));
    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket_status, viewGroup, false);
        return new TicketViewHolder(itemView, clientName);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTicketID;
        TextView textViewTicketNumber;
        TextView textViewSubject;
        View color;

        TicketViewHolder(View v, final String clientName) {
            super(v);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            color = v.findViewById(R.id.color);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                    intent.putExtra("ticket_id", textViewTicketID.getText().toString());
                    Prefs.putString("TICKETid",textViewTicketID.getText().toString());
                    intent.putExtra("ticket_number", textViewTicketNumber.getText().toString());
                    intent.putExtra("ticket_opened_by", clientName);
                    intent.putExtra("ticket_subject", textViewSubject.getText().toString());
                    v.getContext().startActivity(intent);
                }
            });

        }

    }

}
