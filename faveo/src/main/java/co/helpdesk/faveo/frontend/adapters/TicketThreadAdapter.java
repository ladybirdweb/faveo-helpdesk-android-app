package co.helpdesk.faveo.frontend.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.CircleTransform;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.model.TicketThread;

/**
 * This adapter is for the conversation page in the ticket detail page.
 */
public class TicketThreadAdapter extends RecyclerView.Adapter<TicketThreadAdapter.TicketViewHolder> {
    private List<TicketThread> ticketThreadList;
    public Context context;

    public TicketThreadAdapter(List<TicketThread> ticketThreadList) {
        this.ticketThreadList = ticketThreadList;
    }

    @Override
    public int getItemCount() {
        return ticketThreadList.size();
    }

    @Override
    public void onBindViewHolder(final TicketViewHolder ticketViewHolder, final int i) {
        TicketThread ticketThread = ticketThreadList.get(i);
        String letter = String.valueOf(ticketThread.clientName.charAt(0)).toUpperCase();
        ticketViewHolder.textViewClientName.setText(ticketThread.clientName);
        ticketViewHolder.textViewMessageTime.setReferenceTime(Helper.relativeTime(ticketThread.messageTime));
        ticketViewHolder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                TicketThread ticketThread = ticketThreadList.get(i);
                Toast.makeText(v.getContext(), "Clicked :"+i, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        //ticketViewHolder.textViewMessageTitle.setText(ticketThread.messageTitle);
        //ticketViewHolder.textViewMessage.setText(Html.fromHtml(ticketThread.message));
        ticketViewHolder.webView.loadDataWithBaseURL(null, ticketThread.message.replaceAll("\\n", "<br/>"), "text/html", "UTF-8", null);
        if (ticketThread.clientPicture.equals("")){
            ticketViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);

        }
        else if (ticketThread.clientPicture.contains(".jpg")){
            //mDrawableBuilder = TextDrawable.builder()
            //.round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
            Picasso.with(context).load(ticketThread.getClientPicture()).transform(new CircleTransform()).into(ticketViewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

            //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

        }
        else {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            ticketViewHolder.roundedImageViewProfilePic.setAlpha(0.6f);
            ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }

//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(ticketViewHolder.roundedImageViewProfilePic, ticketThread.clientPicture, ticketThread.placeholder);
//        if (ticketThread.clientPicture != null && ticketThread.clientPicture.trim().length() != 0)
//            Picasso.with(ticketViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(ticketThread.clientPicture)
//                    .resize(96, 96)
//                    .centerCrop()
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(ticketViewHolder.roundedImageViewProfilePic);

//        for (int j=0;j<ticketThreadList.size()-1;j++){
//            ticketViewHolder.webView.setVisibility(View.VISIBLE);
//        }

        if (i==0){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }
        if (i==1){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }
        if (i==ticketThreadList.size()-1){
            ticketViewHolder.webView.setVisibility(View.VISIBLE);
        }

        ticketViewHolder.thread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ticketViewHolder.webView.getVisibility() == View.GONE) {
                    //ticketViewHolder.textViewMessageTitle.setVisibility(View.VISIBLE);
                    ticketViewHolder.webView.setVisibility(View.VISIBLE);
                } else {
                    //ticketViewHolder.textViewMessageTitle.setVisibility(View.GONE);
                    ticketViewHolder.webView.setVisibility(View.GONE);
                }
            }
        });

        if (!ticketThread.getIsReply().equals("true"))
            ticketViewHolder.textViewType.setText("");

    }

    @Override
    public TicketViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_conversation, viewGroup, false);
        return new TicketViewHolder(itemView);
    }

    static class TicketViewHolder extends RecyclerView.ViewHolder {

        View thread;
        ImageView roundedImageViewProfilePic;
        TextView textViewClientName;
        RelativeTimeTextView textViewMessageTime;
        TextView textViewMessageTitle;
        //  protected TextView textViewMessage;
        TextView textViewType;
        WebView webView;
        LinearLayout linearLayout;

        TicketViewHolder(View v) {

            super(v);
            thread = v.findViewById(R.id.thread);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewMessageTime = (RelativeTimeTextView) v.findViewById(R.id.textView_ticket_time);
            textViewMessageTitle = (TextView) v.findViewById(R.id.textView_client_message_title);
            linearLayout= (LinearLayout) v.findViewById(R.id.linearLayout);
            //  textViewMessage = (TextView) v.findViewById(R.id.textView_client_message_body);
            textViewType = (TextView) v.findViewById(R.id.textView_type);
            webView = (WebView) v.findViewById(R.id.webView);
        }

    }

}
