package the.prothean.skymusic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import the.prothean.entryactivity.R;

public class RecyclerAdapter extends RecyclerView.Adapter<CustomViewHolder> {

    ArrayList<Music> notes = new ArrayList<Music>();
    private OnUserClickListener mOnUserClickListener;

    public RecyclerAdapter(OnUserClickListener mOnUserClickListener){
        this.mOnUserClickListener = mOnUserClickListener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.music_row, parent, false);
        return new CustomViewHolder(view, mOnUserClickListener);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Music note = notes.get(position);

        holder.songNameTV.setText(""+note.songName);
        holder.imageIV.setImageBitmap(note.cover);
    }

    public void updateElements(ArrayList<Music> updatedList) {
        this.notes.clear();
        this.notes.addAll(updatedList);
        notifyDataSetChanged();
    }

    public interface OnUserClickListener {
        void onUserClick(int position);
    }



}

class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView songNameTV;
    ImageView imageIV;

    RecyclerAdapter.OnUserClickListener onUserClickListener;

    public CustomViewHolder(View view, RecyclerAdapter.OnUserClickListener onUserClickListener) {
        super(view);
        songNameTV = view.findViewById(R.id.textViewSongName);
        imageIV = view.findViewById(R.id.imageViewScreenSong);
        this.onUserClickListener = onUserClickListener;

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        onUserClickListener.onUserClick(getAdapterPosition());
    }
}