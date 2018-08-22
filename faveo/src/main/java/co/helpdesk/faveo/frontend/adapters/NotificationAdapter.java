package co.helpdesk.faveo.frontend.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.Helper;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.frontend.activities.TicketDetailActivity;
import co.helpdesk.faveo.model.NotificationThread;

/**
 * This adapter is for the notification page.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.CardViewHolder> {

    private List<NotificationThread> notiThreadList;
    int totalSeen=0;


    public NotificationAdapter(List<NotificationThread> notiThreadList) {
        this.notiThreadList = notiThreadList;
    }

    // create new views (invoked by the layout manager)
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate a new card view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_notification, parent, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CardViewHolder viewHolder, final int position) {
        final NotificationThread notiThread = notiThreadList.get(position);
        viewHolder.textNotificationtime.setReferenceTime(Helper.relativeTime(notiThread.noti_time));
        viewHolder.textSub.setText(notiThread.getRequesterName().trim()+", "+notiThread.getTicket_subject());
        IImageLoader imageLoader = new PicassoLoader();
        imageLoader.loadImage(viewHolder.roundedImageViewProfilePic, notiThread.profiel_pic, notiThread.placeHolder);
        if (notiThread.getNoti_seen().equals("1")) {

            viewHolder.textSub.setTypeface(null, Typeface.NORMAL);
            viewHolder.textSub.setTextColor(Color.parseColor("#7a7a7a"));
        } else {
            //totalSeen++;

            //Toast.makeText(context, "Unseen :"+totalSeen, Toast.LENGTH_SHORT).show();
            viewHolder.textSub.setTypeface(null, Typeface.BOLD);
            viewHolder.textSub.setTextColor(Color.parseColor("#000000"));
        }


        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //updateNotification(notiThread.getTicket_id() + "");
                new UpdateNotificationSeen(notiThread.getNoti_id()).execute();


                if (notiThread.getNoti_scenario().equals("tickets")) {
                    Intent intent = new Intent(view.getContext(), TicketDetailActivity.class);
                    Log.d("ticket_id", notiThread.getTicket_id() + "");
                    intent.putExtra("ticket_id", notiThread.getTicket_id() + "");
                    view.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(view.getContext(), ClientDetailActivity.class);
                    intent.putExtra("CLIENT_ID", notiThread.getClient_id() + "");
                    view.getContext().startActivity(intent);
                }
                notiThread.setNoti_seen("1");
                notifyDataSetChanged();
            }
        });

    }


//    private void updateNotification(String ticketID) {
//
//        OkHttpClient httpClient = new OkHttpClient();
//
//        RequestBody body = new FormBody.Builder()
//                .add("notification_id", ticketID)
//                .add("token", Preference.getToken())
//                .build();
//
//        Request request = new Request.Builder()
//                .url(Constants.URL + "helpdesk/notifications-seen?")
//                .post(body)
//                .build();
//
//        httpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Log.d("Response Of okHttp", response.body().string());
//            }
//        });
//        //
//
//    }

    @Override
    public int getItemCount() {
        return notiThreadList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView textSub;
        RelativeTimeTextView textNotificationtime;
        AvatarView roundedImageViewProfilePic;

        CardViewHolder(View itemView) {
            super(itemView);
            card = (CardView) itemView.findViewById(R.id.notification_cardview);
            textSub = (TextView) itemView.findViewById(R.id.noti_subject);
            textNotificationtime = (RelativeTimeTextView) itemView.findViewById(R.id.noti_time);
            roundedImageViewProfilePic = (AvatarView) itemView.findViewById(R.id.dthumbnail);
        }
    }

    private class UpdateNotificationSeen extends AsyncTask<String, Void, String> {

        int noti_id;

        UpdateNotificationSeen(int noti_id) {
            this.noti_id = noti_id;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postSeenNotifications(noti_id);
        }

        protected void onPostExecute(String result) {

            if (result == null) {
                Log.d("Noti-seen", "Error");
                return;
            }
            Log.d("noti-seen", "success");
        }
    }

}
