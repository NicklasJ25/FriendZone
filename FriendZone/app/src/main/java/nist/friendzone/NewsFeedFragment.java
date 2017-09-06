package nist.friendzone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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

import nist.friendzone.Model.Couple;

public class NewsFeedFragment extends Fragment
{
    FirebaseDatabase database;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_newsfeed, container, false);

        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    new LinearLayoutManager(getContext()).getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);
            getNewsfeeds();
        }
        return view;
    }

    private void getNewsfeeds()
    {
        database = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = database.getReference("Newsfeed");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                List<Couple> couples = new ArrayList<>();
                for (DataSnapshot dates : dataSnapshot.getChildren())
                {
                    for (DataSnapshot couple : dates.getChildren())
                    {
                        couples.add(0, couple.getValue(Couple.class));
                    }
                }
                recyclerView.setAdapter(new MyNewsRecyclerViewAdapter(getContext(), couples));
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}