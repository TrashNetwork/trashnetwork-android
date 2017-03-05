package happyyoung.trashnetwork.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.nekocode.badge.BadgeDrawable;
import happyyoung.trashnetwork.R;
import happyyoung.trashnetwork.database.model.ChatMessageRecord;
import happyyoung.trashnetwork.database.model.SessionRecord;
import happyyoung.trashnetwork.util.DateTimeUtil;

/**
 * Created by shengyun-zhou <GGGZ-1101-28@Live.cn> on 2017-02-23
 */

public class SessionMessageAdapter extends RecyclerView.Adapter<SessionMessageAdapter.SessionMessageViewHolder> {
    private Context context;
    private List<MessageItem> sessionList;
    private OnItemClickListener listener;

    public SessionMessageAdapter(Context context, List<MessageItem> sessionList, @Nullable OnItemClickListener listener) {
        this.context = context;
        this.sessionList = sessionList;
        this.listener = listener;
    }

    @Override
    public SessionMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SessionMessageViewHolder holder = new SessionMessageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_session_message, parent, false));
        holder.portrait = (ImageView) holder.itemView.findViewById(R.id.session_portrait);
        holder.sendingProgress = (ProgressBar) holder.itemView.findViewById(R.id.chat_send_progress);
        holder.sessionTime = (TextView) holder.itemView.findViewById(R.id.session_time);
        holder.sessionMsg = (TextView) holder.itemView.findViewById(R.id.session_msg);
        holder.username = (TextView) holder.itemView.findViewById(R.id.session_displayname);
        holder.badge = (ImageView) holder.itemView.findViewById(R.id.session_badge);
        return holder;
    }

    @Override
    public void onBindViewHolder(SessionMessageViewHolder holder, final int position) {
        final MessageItem messageItem = sessionList.get(position);
        holder.portrait.setImageBitmap(messageItem.portrait);
        holder.username.setText(messageItem.displayName);
        ChatMessageRecord cmr = messageItem.session.getLatestMessage();
        holder.sessionTime.setText(DateTimeUtil.convertTimestamp(context, cmr.getMessageTime(), true, true));
        holder.sessionMsg.setText(cmr.getLiteralContent());
        if(cmr.getStatus() == ChatMessageRecord.MESSAGE_STATUS_SENDING)
            holder.sendingProgress.setVisibility(View.VISIBLE);
        else
            holder.sendingProgress.setVisibility(View.GONE);
        if(messageItem.session.getUnreadMessageCount() > 0) {
            BadgeDrawable badgeDrawable = new BadgeDrawable.Builder()
                    .type(BadgeDrawable.TYPE_ONLY_ONE_TEXT)
                    .badgeColor(context.getResources().getColor(R.color.red_500))
                    .text1(String.valueOf(messageItem.session.getUnreadMessageCount()))
                    .textSize(context.getResources().getDimension(R.dimen.small_text_size))
                    .build();
            holder.badge.setImageDrawable(badgeDrawable);
            holder.badge.setVisibility(View.VISIBLE);
        }else{
            holder.badge.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onItemClick(position, messageItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sessionList.size();
    }

    class SessionMessageViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        private ImageView portrait;
        private TextView username;
        private TextView sessionTime;
        private TextView sessionMsg;
        private ProgressBar sendingProgress;
        private ImageView badge;

        public SessionMessageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position, MessageItem item);
    }

    public static class MessageItem implements Comparable<MessageItem> {
        private Bitmap portrait;
        private String displayName;
        private SessionRecord session;

        public MessageItem(){}

        public MessageItem(Bitmap portrait, String displayName, SessionRecord session) {
            this.portrait = portrait;
            this.displayName = displayName;
            this.session = session;
        }

        public Bitmap getPortrait() {
            return portrait;
        }

        public void setPortrait(Bitmap portrait) {
            this.portrait = portrait;
        }

        public SessionRecord getSession() {
            return session;
        }

        public void setSession(SessionRecord session) {
            this.session = session;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public int compareTo(MessageItem o) {
            return session.compareTo(o.session);
        }
    }
}