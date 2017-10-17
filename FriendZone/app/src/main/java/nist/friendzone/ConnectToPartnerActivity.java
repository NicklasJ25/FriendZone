package nist.friendzone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
        final String partnerValue = null;
        switch (v.getId())
        {
            case R.id.deletePartnerButton:
                partnerEmail = partnerTextView.getText().toString();

                emailEditText.setVisibility(View.VISIBLE);
                partnerTextView.setVisibility(View.GONE);
                deletePartnerButton.setVisibility(View.GONE);
                break;
            case R.id.findPartnerButton:
                partnerEmail = emailEditText.getText().toString();
                partnerTextView.setText(partnerEmail);

                emailEditText.setVisibility(View.GONE);
                partnerTextView.setVisibility(View.VISIBLE);
                deletePartnerButton.setVisibility(View.VISIBLE);
                break;
        }

        String url = MyApplication.baseUrl + "user?Email=" + partnerEmail;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        User partner = gson.fromJson(response.toString(), User.class);

                        if (partner != null)
                        {
                            partner.Partner = partnerValue;
                            UpdateUser(partner);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                }
        );

        // add JsonObjectRequest to the RequestQueue
        requestQueue.add(jsonObjectRequest);
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
            protected Map<String, String> getParams() {
                Map<String, String>  params = new HashMap<>();
                Gson gson = new Gson();
                params.put("user", gson.toJson(user));
                return params;
            }
        };

        requestQueue.add(req);
    }
}
