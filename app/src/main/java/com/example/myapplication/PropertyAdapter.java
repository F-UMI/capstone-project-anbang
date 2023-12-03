package com.example.myapplication;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.dto.PropertyDto;

import java.util.List;


public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.Holder> {
    private Context context;
    private List<PropertyDto> propertyDtoList;

    public PropertyAdapter(Context context, List<PropertyDto> propertyDtoList) {
        this.context = context;
        this.propertyDtoList = propertyDtoList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_property, parent, false);
        return new Holder(view);
    }

    @Override
    public int getItemCount() {
        return propertyDtoList.size();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(propertyDtoList.get(position));
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView propertyNameTv;
        private TextView bargainerNameTv;
        private TextView listingCreationDateTv;
        private int position;

        public Holder(View itemView) {
            super(itemView);
            propertyNameTv = itemView.findViewById(R.id.itemPropertyName);
            bargainerNameTv = itemView.findViewById(R.id.itembargainerName);
            listingCreationDateTv = itemView.findViewById(R.id.itemlistingCreationDate);
            itemView.setClickable(true);
            itemView.setOnClickListener(view -> {
                position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Intent intent = new Intent(context, PropertyViewActivity.class);
                    intent.putExtra("propertyId", propertyDtoList.get(position).getPropertyId());
                    intent.putExtra("propertyName", propertyDtoList.get(position).getBargainerName());
                    intent.putExtra("bargainerName", propertyDtoList.get(position).getBargainerName());
                    intent.putExtra("listingCreationDate", propertyDtoList.get(position).getListingCreationDate());
                    intent.putExtra("propertyType", propertyDtoList.get(position).getPropertyType());
                    intent.putExtra("propertyAddress", propertyDtoList.get(position).getPropertyAddress());
                    intent.putExtra("detailedAddress", propertyDtoList.get(position).getDetailedAddress());
                    intent.putExtra("propertySize", propertyDtoList.get(position).getPropertySize());
                    intent.putExtra("numberOfRooms", propertyDtoList.get(position).getNumberOfRooms());
                    intent.putExtra("typeOfPropertyTransaction", propertyDtoList.get(position).getTypeOfPropertyTransaction());
                    intent.putExtra("propertyPrice", propertyDtoList.get(position).getPropertyPrice());
                    intent.putExtra("maintenanceCost", propertyDtoList.get(position).getMaintenanceCost());
                    intent.putExtra("availableMoveInDate", propertyDtoList.get(position).getAvailableMoveInDate());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

        }

        public void bind(PropertyDto propertyDto) {
            propertyNameTv.setText(propertyDto.getPropertyName());
            bargainerNameTv.setText(propertyDto.getBargainerName());
            listingCreationDateTv.setText(propertyDto.getListingCreationDate());
        }


    }

}