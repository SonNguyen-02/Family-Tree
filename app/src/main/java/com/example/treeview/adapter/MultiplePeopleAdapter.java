package com.example.treeview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treeview.R;
import com.example.treeview.model.MultiplePeople;
import com.example.treeview.model.People;

public class MultiplePeopleAdapter extends RecyclerView.Adapter<GraphAdapter.PeopleNodeViewHolder> {

    private final MultiplePeople mMultiplePeople;

    public MultiplePeopleAdapter(MultiplePeople mMultiplePeople) {
        this.mMultiplePeople = mMultiplePeople;
    }

    public MultiplePeople getMultiplePeople() {
        return mMultiplePeople;
    }

    @NonNull
    @Override
    public GraphAdapter.PeopleNodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_node, parent, false);
        return new GraphAdapter.PeopleNodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphAdapter.PeopleNodeViewHolder holder, int position) {
        People people = mMultiplePeople.get(position);
        if (people == null) {
            return;
        }
        holder.tvName.setText(people.getName());
        if (people.getDescription() != null && !people.getDescription().isEmpty()) {
            holder.tvDescription.setVisibility(View.VISIBLE);
            holder.tvDescription.setText(people.getDescription());
        }
        holder.imgAvatar.setImageResource(people.getImage());
    }

    @Override
    public int getItemCount() {
        if (mMultiplePeople != null) {
            return mMultiplePeople.size();
        }
        return 0;
    }
}
