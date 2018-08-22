package co.helpdesk.faveo.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.squareup.picasso.Picasso;

import java.util.List;

import agency.tango.android.avatarview.IImageLoader;
import agency.tango.android.avatarview.loader.PicassoLoader;
import agency.tango.android.avatarview.views.AvatarView;
import co.helpdesk.faveo.CircleTransform;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.frontend.activities.ClientDetailActivity;
import co.helpdesk.faveo.model.ClientOverview;

/**
 * This adapter is for the recycler view which we have used
 * in client details page.
 */
public class ClientOverviewAdapter extends RecyclerView.Adapter<ClientOverviewAdapter.ClientViewHolder> {
    private List<ClientOverview> clientOverviewList;
    Context context;
    public ClientOverviewAdapter(Context context,List<ClientOverview> clientOverviewList) {
        this.clientOverviewList = clientOverviewList;
        this.context=context;
    }
    @Override
    public int getItemCount() {
        return clientOverviewList.size();
    }

    @Override
    public void onBindViewHolder(ClientViewHolder clientViewHolder, int i) {
        final ClientOverview clientOverview = clientOverviewList.get(i);
        String letter = String.valueOf(clientOverview.clientName.charAt(0)).toUpperCase();
//        clientViewHolder.client.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Toast.makeText(v.getContext(), "Longed Click", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        clientViewHolder.textViewClientID.setText(clientOverview.clientID + "");
        clientViewHolder.textViewClientName.setText(clientOverview.clientName);
        clientViewHolder.textViewClientEmail.setText(clientOverview.clientEmail);
//        if ((clientOverview.clientPhone.equals("") || clientOverview.clientPhone.equals("null"))
//                &&(clientOverview.clientMobile.equals("Not available"))||clientOverview.clientMobile.equals("null")||clientOverview.clientMobile.equals("")
//                &&(clientOverview.telephone.equals(""))) {
//
//            clientViewHolder.textViewClientPhone.setVisibility(View.GONE);
//            //clientViewHolder.textViewClientPhone.setText(clientOverview.clientMobile);
//
//        }
        if ((!clientOverview.clientPhone.equals("") || !clientOverview.clientPhone.equals("null"))
                &&(clientOverview.clientMobile.equals("Not available"))||clientOverview.clientMobile.equals("null")){
            clientViewHolder.textViewClientPhone.setVisibility(View.VISIBLE);
            clientViewHolder.textViewClientPhone.setText(clientOverview.clientPhone);
        }
        else if ((clientOverview.clientPhone.equals("") || clientOverview.clientPhone.equals("null"))
                &&(!clientOverview.clientMobile.equals("Not available"))||!clientOverview.clientMobile.equals("null")){
            clientViewHolder.textViewClientPhone.setVisibility(View.VISIBLE);
            clientViewHolder.textViewClientPhone.setText(clientOverview.clientMobile);
        }
        else if ((clientOverview.clientPhone.equals("") || clientOverview.clientPhone.equals("null"))
                &&(clientOverview.clientMobile.equals("Not available"))){
            clientViewHolder.textViewClientPhone.setVisibility(View.GONE);
        }
        if (clientOverview.clientPicture.equals("")){
            clientViewHolder.roundedImageViewProfilePic.setVisibility(View.GONE);
            }
        else if (clientOverview.clientPicture.contains(".jpg")){
            //mDrawableBuilder = TextDrawable.builder()
            //.round();
//    TextDrawable drawable1 = mDrawableBuilder.build(generator.getRandomColor());
            Picasso.with(context).load(clientOverview.getClientPicture()).transform(new CircleTransform()).into(clientViewHolder.roundedImageViewProfilePic);
//        Glide.with(context)
//            .load(ticketOverview.getClientPicture())
//            .into(ticketViewHolder.roundedImageViewProfilePic);

            //ticketViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);

        }
        else {
            ColorGenerator generator = ColorGenerator.MATERIAL;
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(letter, generator.getRandomColor());
            clientViewHolder.roundedImageViewProfilePic.setImageDrawable(drawable);
        }

//        IImageLoader imageLoader = new PicassoLoader();
//        imageLoader.loadImage(clientViewHolder.roundedImageViewProfilePic, clientOverview.clientPicture, clientOverview.placeholder);
//        if (clientOverview.clientPicture != null && clientOverview.clientPicture.trim().length() != 0)
//            Picasso.with(clientViewHolder.roundedImageViewProfilePic.getContext())
//                    .load(clientOverview.clientPicture)
//                    .placeholder(R.drawable.default_pic)
//                    .error(R.drawable.default_pic)
//                    .into(clientViewHolder.roundedImageViewProfilePic);

        clientViewHolder.client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ClientDetailActivity.class);
                intent.putExtra("CLIENT_ID", clientOverview.clientID + "");
                intent.putExtra("CLIENT_NAME", clientOverview.clientName);
                intent.putExtra("CLIENT_EMAIL", clientOverview.clientEmail);
                intent.putExtra("CLIENT_PHONE", clientOverview.clientPhone);
                intent.putExtra("CLIENT_PICTURE", clientOverview.clientPicture);
                intent.putExtra("CLIENT_COMPANY", clientOverview.clientCompany);
                intent.putExtra("CLIENT_ACTIVE", clientOverview.clientActive);
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

    static class ClientViewHolder extends RecyclerView.ViewHolder {
        protected View client;
        TextView textViewClientID;
        ImageView roundedImageViewProfilePic;
        TextView textViewClientName;
        TextView textViewClientEmail;
        TextView textViewClientPhone;

        ClientViewHolder(View v) {
            super(v);
            client = v.findViewById(R.id.client);
            textViewClientID = (TextView) v.findViewById(R.id.textView_client_id);
            roundedImageViewProfilePic = (ImageView) v.findViewById(R.id.imageView_default_profile);
            textViewClientName = (TextView) v.findViewById(R.id.textView_client_name);
            textViewClientEmail = (TextView) v.findViewById(R.id.textView_client_email);
            textViewClientPhone = (TextView) v.findViewById(R.id.textView_client_phone);
        }

    }

}
