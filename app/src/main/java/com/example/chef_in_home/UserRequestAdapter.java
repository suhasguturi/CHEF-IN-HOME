package com.example.chef_in_home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserRequestAdapter extends RecyclerView.Adapter<UserRequestAdapter.RequestViewHolder> {

    private List<Request> requestList;

    public UserRequestAdapter(List<Request> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_request_item, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.textViewUserName.setText(request.getUserName());
        holder.textViewUserContact.setText(request.getUserContact());
        holder.textViewStatus.setText(request.getStatus());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), UserRequestDetailActivity.class);
                intent.putExtra("requestId", request.getRequestId());
                intent.putExtra("chefId", request.getChefId());
                view.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName, textViewUserContact, textViewStatus;
        ImageView imageView;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUserName = itemView.findViewById(R.id.textViewUserName);
            textViewUserContact = itemView.findViewById(R.id.textViewUserContact);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            imageView = itemView.findViewById(R.id.imageViewIcon);
        }
    }
}
