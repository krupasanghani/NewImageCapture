package com.example.multiplecamera.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.multiplecamera.R;
import com.example.multiplecamera.model.ImageResponse;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ImageResponse> listdata = new ArrayList<>();
    UploadAdapter.OnShareClickedListener mCallback;

    // RecyclerView recyclerView;
    public UploadAdapter(Context context, ArrayList<ImageResponse> listdata) {
        this.context = context;
        this.listdata = listdata;
    }

    public void updateImageList(ArrayList<ImageResponse> arrayList) {
        this.listdata = arrayList;

        System.out.println("listdata: " + listdata);
        notifyDataSetChanged();
    }

    public void setOnShareClickedListener(UploadAdapter.OnShareClickedListener mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public UploadAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.upload_item, parent, false);
        UploadAdapter.ViewHolder viewHolder = new UploadAdapter.ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(UploadAdapter.ViewHolder holder, int position) {
        final ImageResponse myListData = listdata.get(position);

        System.out.println("myListData: " + myListData);

        Glide.with(context)
                .load(myListData.getImageFile())
                .centerCrop()
                .placeholder(R.drawable.ic_image_picker)
                .into(holder.mainImageRoundedImageView);

        holder.addAudioAppCompatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("m: " + myListData);
                if(myListData != null)
                    mCallback.AddAudioClicked(myListData);
            }
        });

        holder.addNotesAppCompatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.AddNotesClicked(myListData);
                Toast.makeText(view.getContext(), "click on item: " + myListData, Toast.LENGTH_LONG).show();
            }
        });

        holder.deleteAppCompatImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.DeleteRecordClicked(myListData);
                Toast.makeText(view.getContext(), "Deleted record locally", Toast.LENGTH_LONG).show();

            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public RoundedImageView mainImageRoundedImageView;
        public AppCompatTextView addAudioAppCompatTextView;
        public AppCompatTextView addNotesAppCompatTextView;
        public AppCompatImageView deleteAppCompatImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mainImageRoundedImageView = (RoundedImageView) itemView.findViewById(R.id.mainImageRoundedImageView);
            this.addAudioAppCompatTextView = (AppCompatTextView) itemView.findViewById(R.id.addAudioAppCompatTextView);
            this.addNotesAppCompatTextView = (AppCompatTextView) itemView.findViewById(R.id.addNotesAppCompatTextView);
            this.deleteAppCompatImageView = (AppCompatImageView) itemView.findViewById(R.id.deleteAppCompatImageView);
        }
    }

    public interface OnShareClickedListener {
        public void AddAudioClicked(ImageResponse myListData);

        public void AddNotesClicked(ImageResponse myListData);
        public void DeleteRecordClicked(ImageResponse myListData);
    }
}

