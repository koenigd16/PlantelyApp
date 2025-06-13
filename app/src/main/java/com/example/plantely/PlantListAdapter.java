// java/com/example/plantely/PlantListAdapter.java
package com.example.plantely;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

/**
 * RecyclerView-Adapter zur Darstellung und Klick-Behandlung der Pflanzenliste.
 */
public class PlantListAdapter
        extends ListAdapter<Plant, PlantListAdapter.ViewHolder> {

    public interface OnClickListener {
        void onClick(Plant plant);
    }

    private final OnClickListener clickListener;

    public PlantListAdapter(OnClickListener listener) {
        super(DIFF_CALLBACK);
        this.clickListener = listener;
    }

    private static final DiffUtil.ItemCallback<Plant> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Plant>() {
                @Override
                public boolean areItemsTheSame(@NonNull Plant o1, @NonNull Plant o2) {
                    return o1.getName().equals(o2.getName());
                }
                @Override
                public boolean areContentsTheSame(@NonNull Plant o1, @NonNull Plant o2) {
                    return o1.getName().equals(o2.getName())
                            && o1.getCategory().equals(o2.getCategory());
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plant, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {
        Plant p = getItem(position);
        holder.name.setText(p.getName());
        holder.category.setText(p.getCategory());
        holder.itemView.setOnClickListener(v -> clickListener.onClick(p));
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name, category;
        ViewHolder(View itemView) {
            super(itemView);
            name     = itemView.findViewById(R.id.textPlantName);
            category = itemView.findViewById(R.id.textPlantCategory);
        }
    }
}
