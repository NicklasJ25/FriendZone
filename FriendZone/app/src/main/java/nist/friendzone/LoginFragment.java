package nist.friendzone;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Date;

import nist.friendzone.Model.GsonDateAdapter;
import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener
{
    private String TAG = "LoginFragment";

    private TextView emailTextView;
    private EditText passwordTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        emailTextView = view.findViewById(R.id.emailTextView);
        passwordTextView = view.findViewById(R.id.passwordTextView);

        view.findViewById(R.id.loginButton).setOnClickListener(this);
        view.findViewById(R.id.createUserButton).setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.loginButton:
                Login();
                break;
            case R.id.createUserButton:
                Bundle bundle = new Bundle();
                bundle.putString("AccountType", "EmailPassword");

                Fragment fragment = new SignupNameFragment();
                fragment.setArguments(bundle);
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.frameLayout, fragment)
                        .commit();
                break;
        }
    }

    private void Login()
    {
        String email = emailTextView.getText().toString();
        final String password = passwordTextView.getText().toString();

        String url = MyApplication.baseUrl + "user?Email=" + email;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        StringRequest jsonObjectRequest = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null) {
                            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateAdapter()).create();
                            User user = gson.fromJson(response, User.class);

                            if (user.Password.equals(password)) {
                                MyPreferences.setLoggedInEmail(getContext(), user.Email);
                                RealmDatabase.CreateUser(user);

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                //TODO: Opret global string
                                Toast.makeText(getContext(), "Forkert kode", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            //TODO: Opret global string
                            Toast.makeText(getContext(), "Email findes ikke", Toast.LENGTH_LONG).show();
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
}
