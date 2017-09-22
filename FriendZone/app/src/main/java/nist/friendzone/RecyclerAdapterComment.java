package nist.friendzone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import nist.friendzone.Model.Comment;

public class RecyclerAdapterComment extends RecyclerView.Adapter<RecyclerAdapterComment.ViewHolder>
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private Context context;
    private final List<Comment> comments;

    public RecyclerAdapterComment(Context context, List<Comment> comments)
    {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = comments.get(position);
        StorageReference myStorageReference = storage.getReferenceFromUrl(comments.get(position).part1Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(myStorageReference)
                .into(holder.part1AvatarView);

        StorageReference partnerStorageReference = storage.getReferenceFromUrl(comments.get(position).part2Picture);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(partnerStorageReference)
                .into(holder.part2AvatarView);
        holder.namesTextView.setText(comments.get(position).names);
        holder.agesTextView.setText(comments.get(position).ages);
        holder.descriptionTextView.setText(comments.get(position).description);
        holder.timeTextView.setText(context.getResources().getString(R.string.postedTimeText) + comments.get(position).time);
    }

    @Override
    public int getItemCount()
    {
        return comments.size();
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
        private Comment mItem;

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
        }
    }
}
