package nist.friendzone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nist.friendzone.Model.Post;

public class RecyclerAdapterNewsfeed extends RecyclerView.Adapter<RecyclerAdapterNewsfeed.ViewHolder>
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private final List<Post> posts;
    private Context context;

    public RecyclerAdapterNewsfeed(Context context, List<Post> posts)
    {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position)
    {
        holder.mItem = posts.get(position);
        StorageReference myStorageReference = storage.getReferenceFromUrl(posts.get(position).part1Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(holder.part1AvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(posts.get(position).part2Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(holder.part2AvatarView);
        holder.namesTextView.setText(posts.get(position).names);
        holder.agesTextView.setText(posts.get(position).ages);
        holder.descriptionTextView.setText(posts.get(position).description);
        holder.timeTextView.setText(context.getResources().getString(R.string.postedTimeText) + posts.get(position).time);

        holder.commentsImageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("FirebaseRef", posts.get(position).firebaseRef);
                bundle.putString("Part1Picture", posts.get(position).part1Picture);
                bundle.putString("Part2Picture", posts.get(position).part2Picture);
                bundle.putString("Names", posts.get(position).names);
                bundle.putString("Ages", posts.get(position).ages);
                bundle.putString("Description", posts.get(position).description);
                bundle.putString("Time", posts.get(position).time);

                Fragment fragment = new CommentsFragment();
                fragment.setArguments(bundle);
                ((MainActivity) context).getSupportFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .hide(((MainActivity) context).getSupportFragmentManager().findFragmentByTag("NewsFeedFragment"))
                        .add(R.id.content, fragment)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View view;
        private final ImageView part1AvatarView;
        private final ImageView part2AvatarView;
        private final TextView namesTextView;
        private final TextView agesTextView;
        private final TextView descriptionTextView;
        private final TextView timeTextView;
        private final ImageButton commentsImageButton;
        private Post mItem;

        private ViewHolder(View view)
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
