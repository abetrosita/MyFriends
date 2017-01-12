package com.example.abetrosita.myfriends;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AbetRosita on 1/8/2017.
 */

class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.FriendViewHolder>{

    private static final String LOG_TAG = FriendAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Context mContext;

    final private FriendAdapterOnClickHandler mClickHandler;

    public interface FriendAdapterOnClickHandler {
        void onClick(View view, View viewAction);
    }

    public FriendAdapter(Cursor cursor, FriendAdapterOnClickHandler clickHandler) {
        mCursor = cursor;
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

        if (mCursor.isClosed() || mCursor ==null) return;
        if(!mCursor.moveToPosition(position)) return;

        holder.friendName.setText(mCursor.getString(
                mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_NAME)));
        holder.friendPhone.setText(mCursor.getString(
                mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_PHONE)));
        holder.friendEmail.setText(mCursor.getString(
                mCursor.getColumnIndex(FriendsContract.FriendsColumns.FRIENDS_EMAIL)));
        holder.friendActions.setTag(mCursor.getString(
                mCursor.getColumnIndex(BaseColumns._ID)));

    }


    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
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
            mClickHandler.onClick(v, friendActions);
        }
    }

    public void loadFriendsData(Cursor cursor){
        if (mCursor != null) mCursor.close();
        mCursor = cursor;
        if (cursor != null) {
            Log.d(LOG_TAG, "++++ NOTIFY DATA SET CHANGED CALLED");
            this.notifyDataSetChanged();
        }
    }

}
