package com.runit.dimagibot.ui.main;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runit.dimagibot.R;
import com.runit.dimagibot.data.model.TodoItem;

import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoViewHolder> {

    private List<TodoItem> mData;

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new TodoViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todo_item, viewGroup, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder todoViewHolder, int i) {
        TodoItem item = mData.get(i);
        todoViewHolder.tvIndex.setText(Integer.toString(i + 1) + ".");
        todoViewHolder.tvContent.setText(item.getContent());
        if (item.isCompleted()) {
            todoViewHolder.ivDone.setVisibility(View.VISIBLE);
        } else {
            todoViewHolder.ivDone.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    void setData(List<TodoItem> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        TextView tvIndex, tvContent;
        View ivDone;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvIndex = itemView.findViewById(R.id.tv_index);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivDone = itemView.findViewById(R.id.iv_done);
        }
    }
}
