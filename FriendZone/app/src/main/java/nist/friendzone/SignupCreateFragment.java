package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

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
            String birthday = getArguments().getString("BirthDay");
            String phone = getArguments().getString("Phone");
            String streetname = null;
            String postalcode = null;
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
