package com.fireflies.myweather.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.fireflies.myweather.R;
import com.fireflies.myweather.database.WeatherEntry;

import java.util.List;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {


    private final String HTTPS = "https:";
    // Member variable to handle Item click
    final private ItemClickListener itemClickListener;
    private Context context;
    private List<WeatherEntry> entries;

    /**
     * Constructor for the Weather Adapter
     *
     * @param context context
     */
    public WeatherAdapter(Context context, ItemClickListener itemClickListener) {
        this.context = context;
        this.itemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public WeatherAdapter.WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.weather_row_layout, parent, false);

        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.WeatherViewHolder holder, int position) {

        WeatherEntry entry = entries.get(position);
        holder.region.setText(entry.getRegion());
        holder.day.setText(entry.getDay());
        holder.maxTempInC.setText(String.valueOf(entry.getMaxTempInC()));
        holder.minTempInC.setText(String.valueOf(entry.getMinTempInC()));
        Uri imageUri = Uri.parse(HTTPS.concat(entry.getImageLocationURL()));
        Glide.with(context)
                .load(imageUri)
                .into(holder.icon);

    }

    @Override
    public int getItemCount() {
        if (entries == null) {
            return 0;
        }
        return entries.size();
    }

    public List<WeatherEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<WeatherEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(int itemId);
    }


    class WeatherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView region;
        TextView minTempInC;
        TextView maxTempInC;
        TextView day;
        ImageView icon;
        CardView cardView;

        WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            region = itemView.findViewById(R.id.region);
            day = itemView.findViewById(R.id.day);
            minTempInC = itemView.findViewById(R.id.min_in_c_value);
            maxTempInC = itemView.findViewById(R.id.max_in_c_value);
            icon = itemView.findViewById(R.id.imageView);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int itemId = entries.get(getAdapterPosition()).getId();
            itemClickListener.onItemClick(itemId);
        }
    }
}
