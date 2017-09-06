package nist.friendzone;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mindorks.placeholderview.InfinitePlaceHolderView;
import com.mindorks.placeholderview.annotations.infinite.LoadMore;

import java.util.List;

import nist.friendzone.Model.Couple;

public class NewsfeedPlaceholderAdapter extends RecyclerView.Adapter<NewsfeedPlaceholderAdapter.ViewHolder>
{
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    private Context context;
    private InfinitePlaceHolderView mLoadMoreView;
    private final List<Couple> couples;

    public NewsfeedPlaceholderAdapter(Context context, InfinitePlaceHolderView loadMoreView, List<Couple> couples)
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
    }

    @Override
    public int getItemCount()
    {
        return couples.size();
    }

    @LoadMore
    private void onLoadMore(){
        Log.d("Test", "Load More!!!");
        new ForcedWaitedLoading();
    }

    class ForcedWaitedLoading implements Runnable{

        public ForcedWaitedLoading() {
            new Thread(this).start();
        }

        @Override
        public void run() {

//            new Handler(Looper.getMainLooper()).post(new Runnable() {
//                @Override
//                public void run() {
//                    int count = mLoadMoreView.getViewCount();
//                    for (int i = count - 1;
//                         i < (count - 1 + NewsfeedPlaceholderAdapter.LOAD_VIEW_SET_COUNT) && mFeedList.size() > i;
//                         i++) {
//                        mLoadMoreView.addView(new ItemView(mLoadMoreView.getContext(), mFeedList.get(i)));
//
//                        if(i == mFeedList.size() - 1){
//                            mLoadMoreView.noMoreToLoad();
//                            break;
//                        }
//                    }
//                    mLoadMoreView.loadingDone();
//                }
//            });
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View view;
        public final ImageView part1AvatarView;
        public final ImageView part2AvatarView;
        public final TextView namesTextView;
        public final TextView agesTextView;
        public final TextView descriptionTextView;
        public Couple mItem;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            part1AvatarView = (ImageView) view.findViewById(R.id.part1AvatarView);
            part2AvatarView = (ImageView) view.findViewById(R.id.part2AvatarView);
            namesTextView = (TextView) view.findViewById(R.id.namesTextView);
            agesTextView = (TextView) view.findViewById(R.id.agesTextView);
            descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        }
    }
}
