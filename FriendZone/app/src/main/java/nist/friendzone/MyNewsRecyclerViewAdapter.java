package nist.friendzone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nist.friendzone.Model.Couple;

public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder>
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private Context context;
    private final List<Couple> couples;

    public MyNewsRecyclerViewAdapter(Context context, List<Couple> couples)
    {
        this.context = context;
        this.couples = couples;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = couples.get(position);
        StorageReference myStorageReference = storage.getReferenceFromUrl(couples.get(position).part1Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(holder.part1AvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(couples.get(position).part2Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(holder.part2AvatarView);
        holder.namesTextView.setText(couples.get(position).names);
        holder.agesTextView.setText(couples.get(position).ages);
        holder.descriptionTextView.setText(couples.get(position).description);
        holder.timeTextView.setText(context.getResources().getString(R.string.postedTimeText) + couples.get(position).time);

        holder.commentsImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(context, "Denne funktion er ikke implementeret endnu", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return couples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View view;
        public final ImageView part1AvatarView;
        public final ImageView part2AvatarView;
        public final TextView namesTextView;
        public final TextView agesTextView;
        public final TextView descriptionTextView;
        public final TextView timeTextView;
        public final ImageButton commentsImageButton;
        public Couple mItem;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            part1AvatarView = view.findViewById(R.id.part1AvatarView);
            part2AvatarView = view.findViewById(R.id.part2AvatarView);
            namesTextView = view.findViewById(R.id.namesTextView);
            agesTextView = view.findViewById(R.id.agesTextView);
            descriptionTextView = view.findViewById(R.id.descriptionTextView);
            timeTextView = view.findViewById(R.id.timeTextView);
            commentsImageButton = view.findViewById(R.id.commentsImageButton);
        }
    }
}
