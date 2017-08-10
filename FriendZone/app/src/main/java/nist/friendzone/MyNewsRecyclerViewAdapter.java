package nist.friendzone;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

 import nist.friendzone.dummy.UserPairs.UserPair;

import java.util.List;

public class MyNewsRecyclerViewAdapter extends RecyclerView.Adapter<MyNewsRecyclerViewAdapter.ViewHolder>
{
    private final List<UserPair> userPairs;

    public MyNewsRecyclerViewAdapter(List<UserPair> userPairs)
    {
        this.userPairs = userPairs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.mItem = userPairs.get(position);
        holder.nameTextView.setText(userPairs.get(position).names);
        holder.ageTextView.setText(userPairs.get(position).ages);
        holder.descriptionTextView.setText(userPairs.get(position).descriptions);
    }

    @Override
    public int getItemCount()
    {
        return userPairs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View view;
        public final TextView nameTextView;
        public final TextView ageTextView;
        public final TextView descriptionTextView;
        public UserPair mItem;

        public ViewHolder(View view)
        {
            super(view);
            this.view = view;
            nameTextView = (TextView) view.findViewById(R.id.nameTextView);
            ageTextView = (TextView) view.findViewById(R.id.ageTextView);
            descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        }
    }
}
