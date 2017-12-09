package com.gym.app.parts.confirmation;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gym.app.R;
import com.gym.app.data.model.ChangeTimeRequest;
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
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConfirmationModel model = mList.get(position);
        if (model.getType() == ConfirmationType.TIME) {
            bindTime((TimeChangeHolder) holder, (ChangeTimeRequest) model);
        }
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

    public interface ConfirmationListener {
        void confirmChange(ChangeTimeRequest request);
    }
}
