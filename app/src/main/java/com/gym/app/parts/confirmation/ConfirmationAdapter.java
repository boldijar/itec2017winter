package com.gym.app.parts.confirmation;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.ChangeTimeRequest;
import com.gym.app.data.model.Event;
import com.gym.app.utils.Constants;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class ConfirmationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ConfirmationModel> mList;
    private final ConfirmationListener mConfirmationListener;

    public ConfirmationAdapter(List<ConfirmationModel> list, ConfirmationListener confirmationListener) {
        mList = list;
        mConfirmationListener = confirmationListener;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).getType().ordinal();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ConfirmationType type = ConfirmationType.values()[viewType];
        if (type == ConfirmationType.TIME) {
            return new TimeChangeHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_confirmation, parent, false));
        }
        return new EventAddHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_request, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConfirmationModel model = mList.get(position);
        if (model.getType() == ConfirmationType.TIME) {
            bindTime((TimeChangeHolder) holder, (ChangeTimeRequest) model);
        } else {
            bindEvent((EventAddHolder) holder, (Event) model);
        }
    }

    private void bindEvent(EventAddHolder holder, final Event model) {
        String text = "Name: <b>" + model.mName + "</b>";
        text += "<br>Address: <b>" + model.mAddress + "</b>";
        text += "<br>Teacher: <b>" + model.mTeacher + "</b>";
        text += "<br>Time: <b>" + Constants.DAY_FORMAT_TIME.format(new Date(model.mTime)) + "</b>";
        text += "<br>Lesson: <b>" + model.mLessons[0].mName;
        holder.mText.setText(Html.fromHtml(text));
        holder.mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConfirmationListener.confirmChange(model);
            }
        });
    }

    private void bindTime(final TimeChangeHolder holder, final ChangeTimeRequest model) {
        holder.mTitle.setText(model.mEvent[0].mName + " - " + model.mEvent[0].mLessons[0].mName);
        holder.mTeacher.setText(model.mEvent[0].mTeacher);
        String oldDate = Constants.DAY_FORMAT_TIME.format(new Date(model.mOldTime));
        String newDate = Constants.DAY_FORMAT_TIME.format(new Date(model.mNewTime));
        String message = String.format("Requested to change time from <b>%s</b> to <b>%s</b>", oldDate, newDate);
        holder.mMessage.setText(Html.fromHtml(message));
        holder.mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConfirmationListener.confirmChange(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void remove(ConfirmationType type, int id) {
        for (int i = 0; i < mList.size(); i++) {
            ConfirmationModel model = mList.get(i);
            if (model.getId() == id && model.getType() == type) {
                mList.remove(i);
                notifyItemRemoved(i);
                return;
            }
        }
    }

    public static class TimeChangeHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.time_confirmation_confirm)
        View mConfirmButton;
        @BindView(R.id.time_confirmation_message)
        TextView mMessage;
        @BindView(R.id.time_confirmation_teacher)
        TextView mTeacher;
        @BindView(R.id.time_confirmation_title)
        TextView mTitle;

        public TimeChangeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class EventAddHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.event_request_confirm)
        View mConfirmButton;
        @BindView(R.id.event_request_text)
        TextView mText;

        public EventAddHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ConfirmationListener {
        void confirmChange(ChangeTimeRequest request);

        void confirmChange(Event model);
    }
}
