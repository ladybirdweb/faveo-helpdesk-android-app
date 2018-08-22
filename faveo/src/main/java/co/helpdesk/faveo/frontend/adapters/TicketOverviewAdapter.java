package co.helpdesk.faveo.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.pixplicity.easyprefs.library.Prefs;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.CircleTransform;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.model.TicketOverview;

/**
 * This adapter is for the tickets(inbox,mytickets,trash...) page in the main page.
 */
public class TicketOverviewAdapter extends RecyclerView.Adapter<TicketOverviewAdapter.TicketViewHolder> {
    private List<TicketOverview> ticketOverviewList;
    private Context context;
    public TicketOverviewAdapter(Context context,List<TicketOverview> ticketOverviewList) {
        this.ticketOverviewList = ticketOverviewList;
        this.context=context;

    }

    @Override
    public int getItemCount() {
        return ticketOverviewList.size();
    }

    @Override
    public void onBindViewHolder(TicketViewHolder ticketViewHolder, int i) {
        final TicketOverview ticketOverview = ticketOverviewList.get(i);
        String letter="";
        try {
             letter = String.valueOf(ticketOverview.clientName.charAt(0)).toUpperCase();
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }

        TextDrawable.IBuilder mDrawableBuilder;
//        if (ticketOverview.ticketAttachments.equals("0")) {
//            ticketViewHolder.attachementView.setVisibility(View.GONE);
//        } else {
//            ticketViewHolder.attachementView.setVisibility(View.VISIBLE);
//        }

        if (ticketOverview.clientPicture.equals("")){
            ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

        }
        else if (ticketOverview.clientPicture.contains(".jpg")||ticketOverview.clientPicture.contains(".png")||ticketOverview.clientPicture.contains(".jpeg")){
            mDrawableBuilder = TextDrawable.builder()
                    .round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
            Picasso.with(context).load(ticketOverview.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

            //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

        }
        else{
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
            ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }
        //if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))


        if (ticketOverview.dueDate != null && !ticketOverview.dueDate.equals("null"))
//            if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//            } else ticketViewHolder.textViewOverdue.setVisibility(View.GONE);

            try {
                if (!ticketOverview.dueDate.equals(null) || !ticketOverview.dueDate.equals("null"))
                    Log.d("dueDate", ticketOverview.getDueDate());
                if (Helper.compareDates(ticketOverview.dueDate) == 2) {
                    Log.d("duetoday", "yes");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date EndTime = null;
                    try {
                        EndTime = dateFormat.parse(ticketOverview.getDueDate());
                        Date CurrentTime = dateFormat.parse(dateFormat.format(new Date()));
                        if (CurrentTime.after(EndTime)) {
                            ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                            Log.d("dueFromInbox", "overdue");
                        } else {
                            Log.d("dueFromInbox", "duetoday");
                            ticketViewHolder.textViewduetoday.setVisibility(View.VISIBLE);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } else if (Helper.compareDates(ticketOverview.dueDate) == 1) {
                    Log.d("duetoday", "no");
                    ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
                } else {
                    Log.d("duetoday", "novalue");
                    ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
                    ticketViewHolder.textViewduetoday.setVisibility(View.GONE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }

//            if (Helper.compareDates(ticketOverview.dueDate) == 2) {
//                ticketViewHolder.textViewduetoday.setVisibility(View.VISIBLE);
//                ticketViewHolder.textViewduetoday.setText(R.string.due_today);
//                //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#FFD700"));
//                ((GradientDrawable)ticketViewHolder.textViewduetoday.getBackground()).setColor(Color.parseColor("#3da6d7"));
//                ticketViewHolder.textViewduetoday.setTextColor(Color.parseColor("#ffffff"));
//                //ticketViewHolder.textViewOverdue.setBackgroundColor();
//
//            }
//            else  if (Helper.compareDates(ticketOverview.dueDate) == 1) {
//                ticketViewHolder.textViewOverdue.setVisibility(View.VISIBLE);
//                ticketViewHolder.textViewOverdue.setText(R.string.overdue);
//                //ticketViewHolder.textViewOverdue.setBackgroundColor(Color.parseColor("#ef9a9a"));
////                GradientDrawable drawable = (GradientDrawable) context.getDrawable(ticketViewHolder.textViewOverdue);
////
//////set color
////                 drawable.setColor(color);
//                ((GradientDrawable)ticketViewHolder.textViewOverdue.getBackground()).setColor(Color.parseColor("#3da6d7"));
//                ticketViewHolder.textViewOverdue.setTextColor(Color.parseColor("#ffffff"));
//            }
//            else {
//                ticketViewHolder.textViewOverdue.setVisibility(View.GONE);
//            }


        ticketViewHolder.textViewTicketID.setText(ticketOverview.ticketID + "");
        ticketViewHolder.textViewTicketNumber.setText(ticketOverview.ticketNumber);
        ticketViewHolder.textViewClientName.setText(ticketOverview.clientName);
        ticketViewHolder.textViewSubject.setText(ticketOverview.ticketSubject);
        //Prefs.putString("ticket_subject",ticketOverview.ticketSubject);

//        GradientDrawable shape = new GradientDrawable(new );
//        shape.mutate();
//        shape.setCornerRadii(new float[]{10f,10f});
//        ticketViewHolder.ticketPriority.setBackground(shape);
        if (ticketOverview.ticketPriorityColor != null) {
            ticketViewHolder.ticketPriority.setBackgroundColor(Color.parseColor(ticketOverview.ticketPriorityColor));
        }
        if (ticketOverview.ticketTime.equals("null")||ticketOverview.ticketTime.equals("")) {
            ticketViewHolder.textViewTime.setText("not available");

        }else{
            ticketViewHolder.textViewTime.setReferenceTime(Helper.relativeTime(ticketOverview.ticketTime));
        }


//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketOverview.clientPicture, ticketOverview.placeholder);

//        if (ticketOverview.clientPicture != null && ticketOverview.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

        ticketViewHolder.ticket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TicketDetailActivity.class);
                intent.putExtra("ticket_id", ticketOverview.ticketID + "");
                Log.d("TICKETid",ticketOverview.getTicketID()+"");
                Prefs.putString("TICKETid",ticketOverview.ticketID+"");
                intent.putExtra("ticket_number", ticketOverview.ticketNumber);
                intent.putExtra("ticket_status",ticketOverview.ticketStatus);
                intent.putExtra("ticket_priority",ticketOverview.ticketPriorityColor);
                intent.putExtra("ticket_opened_by", ticketOverview.clientName);
                intent.putExtra("ticket_subject", ticketOverview.ticketSubject);
                v.getContext().startActivity(intent);
            }
        });

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_ticket, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        protected View ticket;
        ImageView roundedImageViewProfilePic;
        TextView textViewTicketID;
        TextView textViewTicketNumber;
        TextView textViewClientName;
        TextView textViewSubject;
        RelativeTimeTextView textViewTime;
        TextView textViewOverdue;
        View ticketPriority;
        // TextView ticketStatus;

        public TextView textViewduetoday;
//        ImageView attachementView;




        TicketViewHolder(View v) {
            super(v);
            ticket = v.findViewById(R.id.ticket);
            //attachementView = (ImageView) v.findViewById(R.id.attachment_icon);
            ticketPriority = v.findViewById(R.id.priority_view);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewTicketID = (TextView) v.findViewById(R.id.textView_ticket_id);
            textViewTicketNumber = (TextView) v.findViewById(R.id.textView_ticket_number);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewSubject = (TextView) v.findViewById(R.id.textView_ticket_subject);
            textViewTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewOverdue = (TextView) v.findViewById(R.id.overdue_view);
            textViewduetoday= (TextView) v.findViewById(R.id.duetoday);
        }

    }

}
