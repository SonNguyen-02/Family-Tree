package com.example.treeview.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.treeview.R;
import com.example.treeview.model.Couple;
import com.example.treeview.model.People;

import java.util.Objects;

import dev.bandb.graphview.AbstractGraphAdapter;

public class GraphAdapter extends AbstractGraphAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PEOPLE = 0;
    private static final int TYPE_COUPLE = 1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_PEOPLE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_node, parent, false);
            return new PeopleNodeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.couple_node, parent, false);
            return new CoupleNodeViewHolder(view);
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
        if (holder instanceof CoupleNodeViewHolder && getNodeData(position) instanceof Couple) {
            CoupleNodeViewHolder vh = (CoupleNodeViewHolder) holder;
            Couple couple = (Couple) getNodeData(position);
            if (couple == null) {
                return;
            }
            vh.tvNameHusband.setText(couple.getHusband().getName());
            if (couple.getHusband().getDescription() != null && !couple.getHusband().getDescription().isEmpty()) {
                vh.tvDescriptionHusband.setVisibility(View.VISIBLE);
                vh.tvDescriptionHusband.setText(couple.getHusband().getDescription());
            }

            vh.imgAvatarHusband.setImageResource(couple.getHusband().getImage());

            vh.tvNameWife.setText(couple.getWife().getName());
            if (couple.getWife().getDescription() != null && !couple.getWife().getDescription().isEmpty()) {
                vh.tvDescriptionWife.setVisibility(View.VISIBLE);
                vh.tvDescriptionWife.setText(couple.getWife().getDescription());
            }
            vh.imgAvatarWife.setImageResource(couple.getWife().getImage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getNodeData(position) instanceof People) {
            return TYPE_PEOPLE;
        }
        return TYPE_COUPLE;
    }

    static class PeopleNodeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView tvName, tvDescription;

        public PeopleNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_desc);
        }
    }

    static class CoupleNodeViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatarHusband, imgAvatarWife;
        TextView tvNameHusband, tvNameWife, tvDescriptionHusband, tvDescriptionWife;

        public CoupleNodeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatarHusband = itemView.findViewById(R.id.iv_avatar_husband);
            imgAvatarWife = itemView.findViewById(R.id.iv_avatar_wife);
            tvNameHusband = itemView.findViewById(R.id.tv_name_husband);
            tvNameWife = itemView.findViewById(R.id.tv_name_wife);
            tvDescriptionHusband = itemView.findViewById(R.id.tv_desc_husband);
            tvDescriptionWife = itemView.findViewById(R.id.tv_desc_wife);
        }
    }


}
