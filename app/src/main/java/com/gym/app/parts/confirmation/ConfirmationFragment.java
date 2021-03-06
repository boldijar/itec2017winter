package com.gym.app.parts.confirmation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gym.app.R;
import com.gym.app.data.model.ChangeTimeRequest;
import com.gym.app.data.model.Event;
import com.gym.app.parts.home.BaseHomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Paul
 * @since 2017.12.09
 */

public class ConfirmationFragment extends BaseHomeFragment implements ConfirmationView, ConfirmationAdapter.ConfirmationListener {

    @BindView(R.id.confirmation_recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.confirmation_empty)
    View mEmpty;
    private ConfirmationPresenter mConfirmationPresenter = new ConfirmationPresenter(this);
    private ConfirmationAdapter mConfirmationAdapter;

    @Override
    protected int getTitle() {
        return R.string.confirm_changes;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_confirmation;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mConfirmationPresenter.loadConfirmations();
    }

    @Override
    public void showModels(List<ConfirmationModel> models) {
        mConfirmationAdapter = new ConfirmationAdapter(models, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mConfirmationAdapter);
        mEmpty.setVisibility(mConfirmationAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showChanged(ConfirmationType type, int id) {
        mConfirmationAdapter.remove(type, id);
        mEmpty.setVisibility(mConfirmationAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void confirmChange(ChangeTimeRequest request) {
        mConfirmationPresenter.confirmChange(request);
    }

    @Override
    public void confirmChange(Event model) {
        mConfirmationPresenter.confirmEventChange(model);
    }
}
