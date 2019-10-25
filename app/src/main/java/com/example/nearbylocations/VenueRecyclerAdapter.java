package com.example.nearbylocations;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

public class VenueRecyclerAdapter extends
        RecyclerView.Adapter<VenueRecyclerAdapter.ViewHolder> {
  private List<VenueModel> mVenues;
  private Context mContext;

  public VenueRecyclerAdapter(List<VenueModel> VenueList, Context context) {
    mVenues = VenueList;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

    View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.venue_recycler_row, parent, false);
    return new ViewHolder(rowView);
  }

  @Override
  public int getItemCount() {
    return mVenues.size();
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {

    final VenueModel selectedVenue = mVenues.get(position);

    holder.VenueName.setText(selectedVenue.getVenueName());
    holder.VenueRating.setText(selectedVenue.getVenueAddress());
    Picasso.with(mContext).load(selectedVenue.getVenueIcon())
        //attempt to load the image from cache
        .networkPolicy(NetworkPolicy.OFFLINE).fit().into(holder.VenueIcon, new Callback() {
      @Override
      public void onSuccess() {

      }

      @Override
      public void onError() {
        //Try again online if cache failed
        Picasso.with(mContext)
            .load(selectedVenue.getVenueIcon())
            .into(holder.VenueIcon, new Callback() {
              @Override
              public void onSuccess() {}

              @Override
              public void onError() {}
            });
      }
    });

  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView VenueIcon;
    TextView VenueName, VenueRating;

    public ViewHolder(View itemView) {
      super(itemView);

      VenueName = (TextView) itemView.findViewById(R.id.venue_name);
      VenueRating = (TextView) itemView.findViewById(R.id.venue_location);
      VenueIcon = (ImageView) itemView.findViewById(R.id.venue_icon);
    }
  }
}