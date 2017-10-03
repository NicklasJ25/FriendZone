package nist.friendzone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nist.friendzone.Model.Comment;
import nist.friendzone.Realm.RealmDatabase;
import nist.friendzone.Realm.User;

public class CommentsFragment extends Fragment implements View.OnClickListener
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database;
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
    private EditText commentEditText;

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

        setNewsfeed();

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
        adapter = new RecyclerAdapterComment(getContext(), comments);
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
            final String partnerSection = MyPreferences.getPartnerSection(getContext());
            String myEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            User myUser = RealmDatabase.GetUser(myEmail);
            String partnerEmail = partnerSection.replace(myEmail.replace(".", ","), "").replace("\\", "").replace(",", ".");
            User partnerUser = RealmDatabase.GetUser(partnerEmail);

            String FirebaseRef = getArguments().getString("FirebaseRef");
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String date = dateFormat.format(new Date());
            String time = timeFormat.format(new Date());

            int myAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(myUser.birthday.split("/")[2]);
            int partnerAge = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(partnerUser.birthday.split("/")[2]);

            Comment comment = new Comment(
                    myUser.profilePicture,
                    partnerUser.profilePicture,
                    myUser.firstname + " & " + partnerUser.firstname,
                    myAge + " & " + partnerAge,
                    commentEditText.getText().toString(),
                    date + " " + time
            );
            database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference(FirebaseRef + "/Comments").child(time + partnerSection);
            databaseReference.setValue(comment);

            commentEditText.setText("");
        }
        else
        {
            Toast.makeText(getContext(), "Husk at skrive en besked", Toast.LENGTH_LONG).show();
        }
    }

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
        DatabaseReference databaseReference = database.getReference(FirebaseRef + "/Comments");
        databaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                comments.clear();
                //TODO: Hent kommentarer ned, 100 af gangen
                for (DataSnapshot comment: dataSnapshot.getChildren())
                {
                    comments.add(comment.getValue(Comment.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
    }
}
