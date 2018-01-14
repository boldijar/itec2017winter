package com.gym.app.parts.section;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.gym.app.R;
import com.gym.app.data.model.Message;
import com.gym.app.data.model.User;
import com.gym.app.parts.home.BaseHomeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * TODO: Class description
 *
 * @author Paul
 * @since 2017.12.09
 */

public class SectionFragment extends BaseHomeFragment implements SectionView {

    @BindView(R.id.section_users)
    RecyclerView mUsers;
    @BindView(R.id.section_messages)
    RecyclerView mMessages;
    @BindView(R.id.section_input)
    EditText mInput;
    private SectionPresenter mSectionPresenter = new SectionPresenter(this);
    private MessageAdapter mMessageAdapter = new MessageAdapter();

    @Override
    protected int getTitle() {
        return R.string.section_dashboard;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_section;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mUnbinder = ButterKnife.bind(this, view);
        mSectionPresenter.loadUsers();
        mSectionPresenter.loadMessages();
    }

    @Override
    public void showUsers(List<User> users) {
        SectionAdapter adapter = new SectionAdapter(users);
        mUsers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mUsers.setAdapter(adapter);
    }

    @Override
    public void showErrorGettingUsers() {
    }

    @OnClick(R.id.section_retry)
    void onRetry() {
        mSectionPresenter.loadMessages();
    }

    @Override
    public void showMessage(List<Message> value) {
        mMessageAdapter.add(value);
        mMessages.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true));
        mMessages.setAdapter(mMessageAdapter);
    }

    @Override
    public void addMessage(Message message) {
        mMessageAdapter.add(message);
    }

    @OnClick(R.id.section_send)
    void onSend() {
        if (mInput.getText().length() == 0) {
            return;
        }
        mSectionPresenter.sendMessage(mInput.getText().toString());
        mInput.setText(null);
    }
}
