package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import nist.friendzone.Model.Comment;

public class CommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    EndlessScrollListener endlessScrollListener;
    RecyclerAdapterComment adapter;
    List<Comment> comments = new ArrayList<>();

    private ImageView part1AvatarView;
    private ImageView part2AvatarView;
    private TextView namesTextView;
    private TextView agesTextView;
    private TextView descriptionTextView;
    private TextView timeTextView;

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

        setNewsfeed();

        swipeRefreshLayout = view.findViewById(R.id.commentsSwipeRefreshLayout);
        recyclerView = view.findViewById(R.id.commentsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        swipeRefreshLayout.setOnRefreshListener(this);

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
        adapter = new RecyclerAdapterComment(getContext(), comments);
        recyclerView.setAdapter(adapter);
        getCommentsAtPage(0);
        return view;
    }

    //TODO: Tilføj kommentarer

    private void setNewsfeed()
    {
        StorageReference myStorageReference = storage.getReferenceFromUrl(getArguments().getString("Part1Picture"));
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(part1AvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(getArguments().getString("Part2Picture"));
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(part2AvatarView);
        namesTextView.setText(getArguments().getString("Names"));
        agesTextView.setText(getArguments().getString("Ages"));
        descriptionTextView.setText(getArguments().getString("Description"));
        timeTextView.setText(getResources().getString(R.string.postedTimeText) + getArguments().getString("Time"));
    }

    private void getCommentsAtPage(final int page)
    {
        String FirebaseRef = getArguments().getString("FirebaseRef");

        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference(FirebaseRef + "Comments");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                //TODO: Hent kommentarer ned, 100 af gangen
                int childCount = (int) dataSnapshot.getChildrenCount();
                int i = 1;
                for (DataSnapshot dates: dataSnapshot.getChildren())
                {
                    if (i == childCount - page)
                    {
                        for (DataSnapshot couple : dates.getChildren())
                        {
                            //comments.add(couple.getValue(Newsfeed.class));
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
        comments.clear();
        endlessScrollListener.resetState();
        getCommentsAtPage(0);
    }
}
