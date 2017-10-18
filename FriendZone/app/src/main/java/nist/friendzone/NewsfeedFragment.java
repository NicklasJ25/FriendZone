package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nist.friendzone.Model.Post;

public class NewsfeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private String TAG = "NewsfeedFragment";

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    EndlessScrollListener endlessScrollListener;
    NewsfeedAdapter adapter;
    List<Post> posts = new ArrayList<>();

    private int postsPrPage = 100;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        swipeRefreshLayout = view.findViewById(R.id.newsfeedSwipeRefreshLayout);
        recyclerView = view.findViewById(R.id.newsfeedRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        endlessScrollListener = new EndlessScrollListener(linearLayoutManager)
        {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view)
            {
                getNewsfeedsAtPage(page);
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);
        adapter = new NewsfeedAdapter(getContext(), posts);
        recyclerView.setAdapter(adapter);
        getNewsfeedsAtPage(0);

        swipeRefreshLayout.setOnRefreshListener(this);
        return view;
    }

    private void getNewsfeedsAtPage(final int page)
    {
        int start = page * postsPrPage;
        int count = start + postsPrPage;
        String url = MyApplication.baseUrl + "post?Start=" + start + "&Count=" + count;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray> () {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++)
                    {
                        JSONObject object = response.getJSONObject(i);
                        Gson gson = new Gson();
                        Post post = gson.fromJson(object.toString(), Post.class);
                        posts.add(0, post);
                    }
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onRefresh()
    {
        posts.clear();
        endlessScrollListener.resetState();
        getNewsfeedsAtPage(0);
    }
}