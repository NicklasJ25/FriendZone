package nist.friendzone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;

public class SignupCreateFragment extends Fragment implements View.OnClickListener
{
    private String TAG = "SignupCreateFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_signup_create, container, false);

        Button createAccountButton = view.findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        String accountType = getArguments().getString("AccountType");
        if (accountType.equals("EmailPassword"))
        {
            String email = getArguments().getString("Email");
            String firstname = getArguments().getString("Firstname");
            String lastname = getArguments().getString("Lastname");
            Date birthday = new Date();
            birthday.setTime(getArguments().getLong("Birthday"));
            String phone = getArguments().getString("Phone");
            String streetname = getArguments().getString("Address");
            String postalcode = getArguments().getString("Postalcode");
            String profilePicture = getArguments().getString("ProfilePicture");
            String password = getArguments().getString("Password");

            User user = new User(email, firstname, lastname, birthday, phone, streetname, postalcode, profilePicture, password);

            CreateUser(user);
        }
    }

    private void CreateUser(final User user)
    {
        String url = MyApplication.baseUrl + "user";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest req = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, response);

                        MyPreferences.setLoggedInEmail(getContext(), user.Email);

                        RealmDatabase.CreateUser(user);

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
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
                    if (user != null) {
                        body = gson.toJson(user).getBytes("UTF-8");
                    }
                } catch (UnsupportedEncodingException e) {
                    Log.e(TAG, "Unable to gets bytes from JSON", e.fillInStackTrace());
                }
                return body;
            }
        };

        requestQueue.add(req);
    }
}
