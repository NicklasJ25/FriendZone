package nist.friendzone;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nist.friendzone.Model.Post;
import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;

public class CreatePostFragment extends Fragment implements View.OnClickListener
{
    private String TAG = "CreatePostFragment";

    EditText descriptionEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_create_post, container, false);

        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        Button publishButton = view.findViewById(R.id.createPostButton);

        publishButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        final Post post = new Post();
        post.Email = MyPreferences.getLoggedInEmail(getContext());
        post.Description = descriptionEditText.getText().toString();
        post.Time = Calendar.getInstance();

        String url = MyApplication.baseUrl + "post";
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
                params.put("post", gson.toJson(post));
                return params;
            }
        };

        requestQueue.add(req);

        BottomNavigationView navigation = getActivity().findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.newsFeedNavigation);
    }
}
