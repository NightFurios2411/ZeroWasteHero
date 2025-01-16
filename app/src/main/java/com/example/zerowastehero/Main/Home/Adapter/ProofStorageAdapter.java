package com.example.zerowastehero.Main.Home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zerowastehero.DataBinding.Model.ProofModel;
import com.example.zerowastehero.R;

import java.util.List;

public class ProofStorageAdapter extends BaseAdapter {

    private Context context;
    private List<ProofModel> proofModels;

    public ProofStorageAdapter(Context context, List<ProofModel> proofModels) {
        this.context = context;
        this.proofModels = proofModels;
    }

    @Override
    public int getCount() {
        return proofModels.size();
    }

    @Override
    public Object getItem(int position) {
        return proofModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_proof_storage_view_card, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // Bind data to the view
        ProofModel proof = proofModels.get(position);

        if (proof.getImageURL() != null && !proof.getImageURL().isEmpty()) {
            Glide.with(context)
                    .load(proof.getImageURL())
                    .placeholder(R.drawable.shape_gray_card)
                    .into(holder.IVProofViewImage);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView IVProofViewImage;

        ViewHolder(View view) {
            IVProofViewImage = view.findViewById(R.id.IVProofViewImage);
        }
    }
}
