package com.android.faveo.frontend.adapters;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.faveo.R;
import com.android.faveo.frontend.activities.ClientDetailActivity;
import com.android.faveo.model.ClientOverview;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ClientOverviewAdapter extends RecyclerView.Adapter<ClientOverviewAdapter.ClientViewHolder> {
    private List<ClientOverview> clientOverviewList;

    public ClientOverviewAdapter(List<ClientOverview> clientOverviewList) {
        this.clientOverviewList = clientOverviewList;
    }

    @Override
    public int getItemCount() {
        return clientOverviewList.size();
    }

    @Override
    public void onBindViewHolder(ClientViewHolder clientViewHolder, int i) {
        final ClientOverview clientOverview = clientOverviewList.get(i);
        clientViewHolder.textViewClientID.setText(clientOverview.clientID + "");
        clientViewHolder.textViewClientName.setText(clientOverview.clientName);
        clientViewHolder.textViewClientEmail.setText(clientOverview.clientEmail);
        clientViewHolder.textViewClientPhone.setText(clientOverview.clientPhone);
        if (clientOverview.clientPicture != null && clientOverview.clientPicture.trim().length() != 0)
            Picasso.with(clientViewHolder.roundedImageViewProfilePic.getContext())
                    .load(clientOverview.clientPicture)
                    .placeholder(R.drawable.default_pic)
                    .error(R.drawable.default_pic)
                    .into(clientViewHolder.roundedImageViewProfilePic);

        clientViewHolder.client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClientDetailActivity.class);
                intent.putExtra("CLIENT_ID", clientOverview.clientID + "");
                intent.putExtra("CLIENT_NAME", clientOverview.clientName);
                intent.putExtra("CLIENT_EMAIL", clientOverview.clientEmail);
                intent.putExtra("CLIENT_PHONE", clientOverview.clientPhone);
                intent.putExtra("CLIENT_PICTURE", clientOverview.clientPicture);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public ClientViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
        from(viewGroup.getContext()).
        inflate(R.layout.card_client, viewGroup, false);
        return new ClientViewHolder(itemView);
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        protected View client;
        protected TextView textViewClientID;
        protected RoundedImageView roundedImageViewProfilePic;
        protected TextView textViewClientName;
        protected TextView textViewClientEmail;
        protected TextView textViewClientPhone;

        public ClientViewHolder(View v) {
            super(v);
            client = v.findViewById(R.id.client);
            textViewClientID = (TextView)  v.findViewById(R.id.textView_client_id);
            roundedImageViewProfilePic =  (RoundedImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView)  v.findViewById(R.id.textView_client_name);
            textViewClientEmail = (TextView) v.findViewById(R.id.textView_client_email);
            textViewClientPhone = (TextView) v.findViewById(R.id.textView_client_phone);
        }

    }

}
