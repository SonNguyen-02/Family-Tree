package com.example.treeview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treeview.R;
import com.example.treeview.model.MultiplePeople;
import com.example.treeview.model.People;

import dev.bandb.graphview.AbstractGraphAdapter;

public class GraphAdapter extends AbstractGraphAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PEOPLE = 0;
    private static final int TYPE_MULTIPLE_PEOPLE = 1;

    private final Context mContext;

    public GraphAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PEOPLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_node, parent, false);
            return new PeopleNodeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_people_node, parent, false);
            return new MultiplePeopleNodeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PeopleNodeViewHolder && getNodeData(position) instanceof People) {
            PeopleNodeViewHolder vh = (PeopleNodeViewHolder) holder;
            People people = (People) getNodeData(position);
            if (people == null) {
                return;
            }
            vh.tvName.setText(people.getName());
            if (people.getDescription() != null && !people.getDescription().isEmpty()) {
                vh.tvDescription.setVisibility(View.VISIBLE);
                vh.tvDescription.setText(people.getDescription());
            }
            vh.imgAvatar.setImageResource(people.getImage());
        }
        if (holder instanceof MultiplePeopleNodeViewHolder && getNodeData(position) instanceof MultiplePeople) {
            MultiplePeopleNodeViewHolder vh = (MultiplePeopleNodeViewHolder) holder;
            MultiplePeopleAdapter adapter = new MultiplePeopleAdapter((MultiplePeople) getNodeData(position));
            vh.rcvMultiplePeople.setAdapter(adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            vh.rcvMultiplePeople.setLayoutManager(layoutManager);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getNodeData(position) instanceof People) {
            return TYPE_PEOPLE;
        }
        return TYPE_MULTIPLE_PEOPLE;
    }

    public static class PeopleNodeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvName, tvDescription;

        public PeopleNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_desc);
        }
    }

    static class MultiplePeopleNodeViewHolder extends RecyclerView.ViewHolder {

        RecyclerView rcvMultiplePeople;

        public MultiplePeopleNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            rcvMultiplePeople = itemView.findViewById(R.id.rcv_multiple_people);
        }
    }

}
