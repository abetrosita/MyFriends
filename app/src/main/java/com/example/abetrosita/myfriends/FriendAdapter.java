package com.example.abetrosita.myfriends;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by AbetRosita on 1/8/2017.
 */

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private List<Friend> mFriends;
    private Context mContext;
    private LinearLayout lastView;
    private boolean editVisible;

    final private FriendAdapterOnClickHandler mClickHandler;

    public interface FriendAdapterOnClickHandler {
        void onClick(View view, View viewAction);
    }

    public FriendAdapter(List<Friend> friends, FriendAdapterOnClickHandler clickHandler) {
        mFriends = friends;
        mClickHandler = clickHandler;
   }


    @Override
    public FriendViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        mContext = viewGroup.getContext();
        int friendListItemLayout = R.layout.friend_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = layoutInflater.inflate(friendListItemLayout, viewGroup,
                shouldAttachToParentImmediately);
        FriendViewHolder viewHolder = new FriendViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FriendViewHolder holder, int position) {
        final Friend friend = mFriends.get(position);
        final LinearLayout llAction = holder.friendActions;
        //final LinearLayout llDetails = holder.friendDetail;

        holder.friendName.setText(friend.getName());
        holder.friendPhone.setText(friend.getPhone());
        holder.friendEmail.setText(friend.getEmail());
        holder.friendActions.setTag(friend.getId());

        /*

        holder.friendView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "VIEW CLICKED", Toast.LENGTH_SHORT).show();
            }
        });
        holder.friendEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "EDIT CLICKED", Toast.LENGTH_SHORT).show();
            }
        });
        holder.friendDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "DELETE CLICKED", Toast.LENGTH_SHORT).show();
                ContentResolver contentResolver = mContentResolver;
                Uri uri = FriendsContract.Friends.buildFriendUri(String.valueOf(friend.getId()));
                contentResolver.delete(uri, null, null);
                //MainActivity.getSupportLoaderManager().restartLoader(1, null, this);

            }
        });

        holder.friendDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastView != null){
                    lastView.setVisibility(View.GONE);
                }

                if(llAction==lastView && editVisible){
                    llAction.setVisibility(View.GONE);
                    editVisible = false;
                }else {
                    llAction.setVisibility(View.VISIBLE);
                    lastView = llAction;
                    editVisible = true;
                }
            }
        });

        */

    }


    @Override
    public int getItemCount() {
        return (null != mFriends ? mFriends.size() : 0);
    }

    class FriendViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView friendName;
        TextView friendPhone;
        TextView friendEmail;
        TextView friendView;
        TextView friendEdit;
        TextView friendDelete;
        final LinearLayout friendDetail;
        final LinearLayout friendActions;

        public FriendViewHolder(View itemView) {
            super(itemView);

            friendName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            friendPhone = (TextView) itemView.findViewById(R.id.tv_friend_phone);
            friendEmail = (TextView) itemView.findViewById(R.id.tv_friend_email);
            friendView = (TextView) itemView.findViewById(R.id.tv_friend_view);
            friendEdit = (TextView) itemView.findViewById(R.id.tv_friend_edit);
            friendDelete = (TextView) itemView.findViewById(R.id.tv_friend_delete);
            friendDetail = (LinearLayout) itemView.findViewById(R.id.ll_friend_detail);
            friendActions = (LinearLayout) itemView.findViewById(R.id.ll_friend_actions);

            friendDetail.setOnClickListener(this);
            friendView.setOnClickListener(this);
            friendEdit.setOnClickListener(this);
            friendDelete.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            //int position = getAdapterPosition();
            mClickHandler.onClick(v, friendActions);
        }
    }

    public void loadFriendsData(List<Friend> friends){
        mFriends = friends;
        notifyDataSetChanged();
    }

}
