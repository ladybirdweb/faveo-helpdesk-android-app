package co.helpdesk.faveo.frontend.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.pixplicity.easyprefs.library.Prefs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import co.helpdesk.faveo.R;
import co.helpdesk.faveo.backend.api.v1.Helpdesk;
import co.helpdesk.faveo.frontend.fragments.ticketDetail.Conversation;
import es.dmoral.toasty.Toasty;

public class TicketReplyActivity extends AppCompatActivity {
        ImageView imageViewBack;
        Button buttonSave;
        EditText editTextSubject;
    ProgressDialog progressDialog;
    public static String ticketID;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_reply);
        progressDialog = new ProgressDialog(this);
        buttonSave= (Button) findViewById(R.id.button_send);
        toolbar= (Toolbar) findViewById(R.id.toolbarForReply);
        imageViewBack= (ImageView) toolbar.findViewById(R.id.imageViewBackTicketReply);
        editTextSubject= (EditText) findViewById(R.id.editText_reply_message);
        ticketID=Prefs.getString("TICKETid",null);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String replyMessage = editTextSubject.getText().toString();
                if (replyMessage.trim().length() == 0) {
                    Toasty.warning(TicketReplyActivity.this, getString(R.string.msg_must_not_be_empty), Toast.LENGTH_LONG).show();
                    return;
                }

                String userID = Prefs.getString("ID", null);
                if (userID != null && userID.length() != 0) {
                    try {
                        replyMessage = URLEncoder.encode(replyMessage, "utf-8");
                        Log.d("Msg", replyMessage);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    new ReplyTicket(Integer.parseInt(ticketID), replyMessage).execute();
                    progressDialog.setMessage(getString(R.string.sending_msg));
                    progressDialog.show();
                }
            }
        });



    }
    @Override
    public void onBackPressed() {
        if (!TicketDetailActivity.isShowing) {
            Log.d("isShowing", "false");
            Intent intent = new Intent(this, TicketDetailActivity.class);
            startActivity(intent);
        } else Log.d("isShowing", "true");


        super.onBackPressed();

//        if (fabExpanded)
//            exitReveal();
//        else super.onBackPressed();
    }
    /**
     * This API is for replying to the particular ticket.
     */
    private class ReplyTicket extends AsyncTask<String, Void, String> {
        int ticketID;
        //String cc;
        String replyContent;

        ReplyTicket(int ticketID, String replyContent) {
            this.ticketID = ticketID;
            //this.cc = cc;
            this.replyContent = replyContent;
        }

        protected String doInBackground(String... urls) {
            return new Helpdesk().postReplyTicket(ticketID, replyContent);
        }

        protected void onPostExecute(String result) {
            Log.d("reply", result + "");
            progressDialog.dismiss();
            if (result == null) {
                Toasty.error(TicketReplyActivity.this, getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show();
                return;
            }
//            editTextCC.getText().clear();
            editTextSubject.getText().clear();
            Toasty.success(TicketReplyActivity.this, getString(R.string.posted_reply), Toast.LENGTH_LONG).show();
            Intent intent=new Intent(TicketReplyActivity.this,TicketDetailActivity.class);
            startActivity(intent);
            // try {
//                TicketThread ticketThread;
//                JSONObject jsonObject = new JSONObject(result);
//                JSONObject res = jsonObject.getJSONObject("result");
//                String clientPicture = "";
//                try {
//                    clientPicture = res.getString("profile_pic");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String messageTitle = "";
//                try {
//                    messageTitle = res.getString("title");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                String clientName = "";
//                try {
//                    clientName = res.getString("first_name") + " " + res.getString("last_name");
//                    if (clientName.equals("") || clientName == null)
//                        clientName = res.getString("user_name");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                String messageTime = res.getString("created_at");
//                String message = res.getString("body");
//                String isReply = "true";
//                try {
//                    isReply = res.getString("is_reply");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            // ticketThread = new TicketThread(clientPicture, clientName, messageTime, messageTitle, message, isReply);
            //ticketThread = new TicketThread(clientName, messageTime, message, isReply);

//            if (fragmentConversation != null) {
//                exitReveal();
//                //fragmentConversation.addThreadAndUpdate(ticketThread);
//            }
//            } catch (JSONException e) {
//                if (fragmentConversation != null) {
//                    exitReveal();
//                }
//                e.printStackTrace();
//                // Toast.makeText(TicketDetailActivity.this, "Unexpected Error ", Toast.LENGTH_LONG).show();
//            }
        }
    }
}
