package com.example.michaelg.myapplication.ListviewActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michaelg.myapplication.R;

import java.io.InputStream;
import java.util.ArrayList;


public class bookAdapter extends ArrayAdapter<Book> {
    ArrayList<Book> actorList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public bookAdapter(Context context, int resource, ArrayList<Book> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        actorList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.title = (TextView) v.findViewById(R.id.tv_title);
            holder.creationdate = (TextView) v.findViewById(R.id.tv_creationdate);
            holder.publisher = (TextView) v.findViewById(R.id.tv_publisher);
            holder.author = (TextView) v.findViewById(R.id.tv_author);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        //holder.imageview.setImageResource(R.drawable.ic_launcher);
        //new DownloadImageTask(holder.imageview).execute(actorList.get(position).getImage());
        holder.title.setText(actorList.get(position).getTitle());
        //holder.tvDescription.setText(actorList.get(position).getDescription());
        holder.creationdate.setText( actorList.get(position).getCreationdate());
        holder.publisher.setText(actorList.get(position).getPublisher());
        holder.author.setText(actorList.get(position).getAuthor());
        return v;

    }

    static class ViewHolder {
        //public ImageView imageview;
        public TextView title;
        //public TextView tvDescription;
        public TextView creationdate;
        public TextView publisher;
        public TextView author;
        //public TextView tvSpouse;
        //public TextView tvChildren;

    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }

    }
}