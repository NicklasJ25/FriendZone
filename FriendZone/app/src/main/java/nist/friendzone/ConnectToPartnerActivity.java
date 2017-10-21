package nist.friendzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import nist.friendzone.Model.GsonDateAdapter;
import nist.friendzone.Model.User;

public class ConnectToPartnerActivity extends AppCompatActivity implements View.OnClickListener
{
    private String TAG = "ConnectToPartnerActivit";

    private EditText emailEditText;
    private TextView partnerTextView;
    private Button deletePartnerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_partner);

        emailEditText = findViewById(R.id.emailEditText);
        partnerTextView = findViewById(R.id.partnerTextView);
        deletePartnerButton = findViewById(R.id.deletePartnerButton);
        Button findPartnerButton = findViewById(R.id.findPartnerButton);

        //TODO: Event Listener
        /*
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        Database database = new Database();
                        database.MakePartners(user.getEmail().replace(".", ","), dataSnapshot.child("Partner").getValue().toString().replace(".", ","));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        myReference.child("Partner").removeValue();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(ConnectToPartnerActivity.this);
        String dialogString = String.format(getResources().getString(R.string.FindPartnerDialogTextView), dataSnapshot.child("Partner").getValue());
        builder.setMessage(dialogString).setPositiveButton(getResources().getString(R.string.Yes), dialogClickListener)
                .setNegativeButton(getResources().getString(R.string.No), dialogClickListener).show();
        */

        deletePartnerButton.setOnClickListener(this);
        findPartnerButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String partnerEmail = null;
        switch (v.getId())
        {
            case R.id.deletePartnerButton:
                partnerEmail = partnerTextView.getText().toString();
                MyPreferences.setPartnerEmail(this, null);

                emailEditText.setVisibility(View.VISIBLE);
                partnerTextView.setVisibility(View.GONE);
                deletePartnerButton.setVisibility(View.GONE);
                break;
            case R.id.findPartnerButton:
                partnerEmail = emailEditText.getText().toString();
                partnerTextView.setText(partnerEmail);
                MyPreferences.setPartnerEmail(this, MyPreferences.getLoggedInEmail(this));

                emailEditText.setVisibility(View.GONE);
                partnerTextView.setVisibility(View.VISIBLE);
                deletePartnerButton.setVisibility(View.VISIBLE);
                break;
        }

        String url = MyApplication.baseUrl + "user?Email=" + partnerEmail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest
                (
                Request.Method.GET,
                url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if (!response.equals("null"))
                        {
                            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateAdapter()).create();
                            User partner = gson.fromJson(response, User.class);
                            partner.Partner = MyPreferences.getPartnerEmail(getBaseContext());
                            UpdateUser(partner);
                        }
                        else
                        {
                            //TODO: Opret Global String
                            Toast.makeText(getBaseContext(), "Bruger findes ikke", Toast.LENGTH_LONG).show();
                            emailEditText.setVisibility(View.VISIBLE);
                            partnerTextView.setVisibility(View.GONE);
                            deletePartnerButton.setVisibility(View.GONE);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, error.toString());
                    }
                }
        );

        requestQueue.add(request);
    }

    private void UpdateUser(final User user)
    {
        String url = MyApplication.baseUrl + "user?email=" + user.Email;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest req = new StringRequest(
                Request.Method.PUT,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                    }
                })
        {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                byte[] body = null;
                try {
                    Gson gson = new Gson();
                    body = gson.toJson(user).getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }
        };

        requestQueue.add(req);
    }
}
