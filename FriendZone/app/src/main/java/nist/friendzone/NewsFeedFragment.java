package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import nist.friendzone.Model.Newsfeed;

public class NewsFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    FirebaseDatabase database;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    EndlessScrollListener endlessScrollListener;
    RecyclerAdapterNewsfeed adapter;
    List<Newsfeed> newsfeeds = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        swipeRefreshLayout = view.findViewById(R.id.newsfeedSwipeRefreshLayout);
        recyclerView = view.findViewById(R.id.newsfeedRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout.setOnRefreshListener(this);

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
        adapter = new RecyclerAdapterNewsfeed(getContext(), newsfeeds);
        recyclerView.setAdapter(adapter);
        getNewsfeedsAtPage(0);
        return view;
    }

    private void getNewsfeedsAtPage(final int page)
    {
        database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = database.getReference("Newsfeed");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                int childCount = (int) dataSnapshot.getChildrenCount();
                int i = 1;
                for (DataSnapshot date: dataSnapshot.getChildren())
                {
                    if (i == childCount - page)
                    {
                        for (DataSnapshot newsfeed : date.getChildren())
                        {
                            Newsfeed newNewsfeed = newsfeed.getValue(Newsfeed.class);
                            newNewsfeed.firebaseRef = "Newsfeed/" + date.getKey() + "/" + newsfeed.getKey();
                            newsfeeds.add(newNewsfeed);
                        }
                    }
                    i++;
                }
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }

    @Override
    public void onRefresh()
    {
        newsfeeds.clear();
        endlessScrollListener.resetState();
        getNewsfeedsAtPage(0);
    }
}