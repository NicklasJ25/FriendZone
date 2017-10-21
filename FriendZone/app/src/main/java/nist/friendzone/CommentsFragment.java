package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nist.friendzone.Model.Comment;
import nist.friendzone.Model.GsonDateAdapter;
import nist.friendzone.Model.Post;
import nist.friendzone.Model.RealmDatabase;
import nist.friendzone.Model.User;

public class CommentsFragment extends Fragment implements View.OnClickListener
{
    private String TAG = "CommentsFragment";

    private FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database;
    RecyclerView recyclerView;
    EndlessScrollListener endlessScrollListener;
    CommentAdapter adapter;
    List<Comment> comments = new ArrayList<>();

    private ImageView part1AvatarView;
    private ImageView part2AvatarView;
    private TextView namesTextView;
    private TextView agesTextView;
    private TextView descriptionTextView;
    private TextView timeTextView;
    private EditText commentEditText;

    private int commentsPrPage = 100;
    private Post post;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);

        part1AvatarView = view.findViewById(R.id.part1AvatarView);
        part2AvatarView = view.findViewById(R.id.part2AvatarView);
        namesTextView = view.findViewById(R.id.namesTextView);
        agesTextView = view.findViewById(R.id.agesTextView);
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        timeTextView = view.findViewById(R.id.timeTextView);
        commentEditText = view.findViewById(R.id.commentEditText);
        ImageButton commentImageButton = view.findViewById(R.id.commentImageButton);

        Gson gson = new Gson();
        post = gson.fromJson(getArguments().getString("Post"), Post.class);

        SetPostDetails();

        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        recyclerView.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                getCommentsAtPage(page);
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);
        adapter = new CommentAdapter(getContext(), comments);
        recyclerView.setAdapter(adapter);
        getCommentsAtPage(0);

        commentImageButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view)
    {
        if (!commentEditText.getText().toString().equals(""))
        {
            String email = MyPreferences.getLoggedInEmail(getContext());

            final Comment comment = new Comment(
                    post.ID,
                    email,
                    commentEditText.getText().toString(),
                    new Date()
            );

            String url = MyApplication.baseUrl + "Comment";
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
                public String getBodyContentType() {
                    return "application/json";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    byte[] body = null;
                    try {
                        Gson gson = new Gson();
                        body = gson.toJson(comment).getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unable to gets bytes from JSON", e.fillInStackTrace());
                    }
                    return body;
                }
            };

            requestQueue.add(req);

            commentEditText.setText("");

            comments.clear();
            endlessScrollListener.resetState();
            getCommentsAtPage(0);
        }
        else
        {
            Toast.makeText(getContext(), "Husk at skrive en besked", Toast.LENGTH_LONG).show();
        }
    }

    private void SetPostDetails()
    {
        if (post.User.ProfilePicture != null)
        {
            //TODO: Hent billede andet sted
            StorageReference myStorageReference = storage.getReferenceFromUrl(post.User.ProfilePicture);
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(myStorageReference)
                    .into(part1AvatarView);
        }

        if(post.User.User2.ProfilePicture != null)
        {
            //TODO: Hent billede andet sted
            StorageReference partnerStorageReference = storage.getReferenceFromUrl(post.User.User2.ProfilePicture);
            Glide.with(this)
                    .using(new FirebaseImageLoader())
                    .load(partnerStorageReference)
                    .into(part2AvatarView);
        }

        String names = post.User.Firstname + " & " + post.User.User2.Firstname;
        namesTextView.setText(names);
        String ages = User.GetAge(post.User.Birthday) + " & " + User.GetAge(post.User.User2.Birthday);
        agesTextView.setText(ages);
        descriptionTextView.setText(post.Description);
        timeTextView.setText(getResources().getString(R.string.postedTimeText) + post.Time);
    }

    private void getCommentsAtPage(final int page)
    {
        int start = page * commentsPrPage;
        int count = start + commentsPrPage;
        String url = MyApplication.baseUrl + "Comment?PostID=" + post.ID + "&Start=" + start + "&Count=" + count;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject object = response.getJSONObject(i);
                        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new GsonDateAdapter()).create();
                        Comment comment = gson.fromJson(object.toString(), Comment.class);
                        comments.add(comment);
                    }
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
            }
        });

        // add JsonArrayRequest to the RequestQueue
        requestQueue.add(req);
    }
}
