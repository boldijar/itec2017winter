package com.gym.app.parts.findcourses;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gym.app.R;
import com.gym.app.data.model.Event;
import com.gym.app.utils.Constants;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder> {

    private final List<Event> mEvents;
    private final EventListener mEventListener;

    public EventAdapter(List<Event> events, EventListener eventListener) {
        mEvents = events;
        mEventListener = eventListener;
    }

    @Override
    public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false));
    }

    @Override
    public void onBindViewHolder(EventHolder holder, int position) {
        final Event event = mEvents.get(position);
        Glide.with(holder.mImage.getContext()).load(event.mLesson.mImage).into(holder.mImage);
        String time = Constants.HOUR_FORMAT.format(new Date(event.mTime));
        holder.mTime.setText(time);
        holder.mTitle.setText(event.mName + " - " + event.mLesson.mName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventListener.onEventClick(event);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    public static class EventHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_image)
        ImageView mImage;
        @BindView(R.id.event_time)
        TextView mTime;
        @BindView(R.id.event_title)
        TextView mTitle;

        public EventHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface EventListener {
        void onEventClick(Event event);
    }
}
