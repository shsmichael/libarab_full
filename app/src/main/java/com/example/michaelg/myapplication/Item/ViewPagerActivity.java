package com.example.michaelg.myapplication.Item;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.michaelg.myapplication.Fragments.Params;
import com.example.michaelg.myapplication.Item.discreteseekbar.DiscreteSeekBar;
import com.example.michaelg.myapplication.ListviewActivity.Book;
import com.example.michaelg.myapplication.R;
import com.example.michaelg.myapplication.Item.zoomable.ZoomableDraweeView;
import com.facebook.common.logging.FLog;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerActivity extends AppCompatActivity{
    List<String> pagesStr = new ArrayList<String>();
    private String ID = "NNL_ALEPH003157499";
    private String userId= "100";
    private String creationdate;
    private String title;
    private String author;
    private String weblink;
    private String publisher;
    private String source;
    private ViewItemTask mAuthTask = null;
    int i = 0;
    int j = 0;
    int flag=0;
    ViewPager vpGallery;
    EditText etchange;
    TextView textView1;
    DiscreteSeekBar discreteSeekBar1;
    private Book book;


    public void bookinfo(View v){

        Intent bookinfoactivity = new Intent(this,BookinfoActivity.class);
        bookinfoactivity.putExtra("creationdate",creationdate);
        bookinfoactivity.putExtra("title",title);
        bookinfoactivity.putExtra("author",author);
        bookinfoactivity.putExtra("webLink",weblink);
        bookinfoactivity.putExtra("publisher",publisher);
        bookinfoactivity.putExtra("source",source);
        startActivity(bookinfoactivity);
    }

    public void changepage(View v){

        String stringnumber = etchange.getText().toString();

        if (!(stringnumber.matches(""))) {
            vpGallery.setCurrentItem(Integer.parseInt(stringnumber) - 1);
            textView1.setText(Integer.parseInt(stringnumber)  + "/" + pagesStr.size());
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        FLog.setMinimumLoggingLevel(FLog.VERBOSE);
        setContentView(R.layout.activity_view_pager);
        etchange =(EditText)findViewById(R.id.et_changepage);
        textView1=(TextView) findViewById(R.id.textView);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            ID  = extras.getString("recordId");
            userId=extras.getString("userId");
            //  userId="100";
        }
        Intent intent = getIntent();
        ID = intent.getStringExtra("recordId");
        userId = intent.getStringExtra("userId");
        creationdate = intent.getStringExtra("creationdate");
        title = intent.getStringExtra("title");
        author = intent.getStringExtra("author");
        weblink = intent.getStringExtra("webLink");
        publisher = intent.getStringExtra("publisher");
        source = intent.getStringExtra("source");

        book = new Book();
        book.setCreationdate(creationdate);
        book.setTitle(title);
        book.setAuthor(author);
        book.setWeblink(weblink);
        book.setPublisher(publisher);
        book.setSource(source);

        mAuthTask = new ViewItemTask(ID,userId);
        mAuthTask.execute((Void) null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Fresco.shutDown();
    }


    public class ViewItemTask extends AsyncTask<Void, Void, JSONObject> {

        private final String bookId;
        private final String userId;

        ViewItemTask(String bookId,String userId) {
            this.bookId = bookId;
            this.userId=userId;
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            if (params.length == 0) {
                return null;
            }

            Log.v("connect", "CONNECTED");
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String serverJsonStr = null;

            try {
                final String ID_PARAM = "recordId";
                final String USER_PARAM ="userId";
                final String SERVER_BASE_URL = //"http://172.20.10.6:8080/LibArab/"+"search/bookquery?";
                        Params.getServer() + "search/bookquery?";
                //TODO: amal sheetquery
                // "search/bookquery/userId/recordId
                //TODO: change according to the server function format

                Uri builtUri = Uri.parse(SERVER_BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(ID_PARAM, bookId)
                        .appendQueryParameter(USER_PARAM,userId)
                        .build();
                URL url = new URL(builtUri.toString());
                Log.v("URL", builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                serverJsonStr = buffer.toString();
                Log.e("PROBLEM", serverJsonStr);

            } catch (IOException e) {
                Log.e("LOGE", "Error ", e);
                return null;

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("LOGE", "Error closing stream", e);
                    }
                }
            }

            JSONObject serverJson = null;
            try {
                serverJson = new JSONObject(serverJsonStr);
                return serverJson;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }


        protected void onPostExecute(final JSONObject success) {
            JSONArray pages=null;

            JSONArray pages3 = null;
            try {
                pages = success.getJSONArray("pages");

                // JSONObject page2 = pages3.getJSONObject(0);
                //  pages = page2.getJSONArray("canvases");

                // JSONObject object = pages.getJSONObject(0);
                //JSONObject object1 = pages.getJSONObject(pages.length() - 1);

                String first = "http://iiif.nli.org.il/IIIF/";
                String last = "/full/full/0/default.jpg";
                String tmp = "";
                for (int i = 1; i < pages.length()-1; i++) {

                    String  book = pages.getString(i);
                    tmp = first +book + last;
                    pagesStr.add(tmp);
                    Log.e("ItemsQ pages",tmp);
                }
                int a=pagesStr.size();
                if((pagesStr.size()==0)||(pagesStr.size()==1)){
                    TextView textView9=(TextView) findViewById(R.id.textView28);
                    textView9.setText("There               "+'\n'+"are                  "+'\n'+ "No                   "+'\n'+ "pages               ");
                    textView9.setTextSize(50);
                    textView9.setTextColor(Color.RED);
            //        textView9.setVisibility(View.VISIBLE);
                    TextView textView4=(TextView) findViewById(R.id.textView4);
                    textView4.setVisibility(textView4.INVISIBLE);
                    ImageView imageView11= (ImageView) findViewById(R.id.imageView11);
                    imageView11.setVisibility(imageView11.INVISIBLE);


                    flag=1;
                }
                // ViewPagerActivity.GalleryAdapter(pagesStr);

                // Intent intent=new Intent(getApplicationContext(),ViewPagerActivity.class);
                // startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            vpGallery = (ViewPager) findViewById(R.id.vp_gallery);
            vpGallery.setAdapter(new GalleryAdapter(pagesStr));
        }


        protected void onCancelled() {
            mAuthTask = null;
            //   showProgress(false);
        }

    }

    class GalleryAdapter extends PagerAdapter {

        List<String> items;

        // };
        public GalleryAdapter(List<String> pages){
            items=new ArrayList<String>();
            if(items.addAll(pagesStr)==false){
                return;
            }

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            if(flag==0) {
                if (j == 0) {
                    textView1.setText(position + "/" + items.size());
                    textView1.setTextSize(20);
                    if (position == items.size() - 1) {
                        i = 1;
                    }
                }
                if (j == 1) {
                    textView1.setText(items.size() - 1 + "/" + items.size());
                    j = 0;
                }
            }

            ZoomableDraweeView view = new ZoomableDraweeView(container.getContext());
            view.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(items.get(position)))
                            .build());



            GenericDraweeHierarchy hierarchy =
                    new GenericDraweeHierarchyBuilder(container.getResources())
                            .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                            .setProgressBarImage(new ProgressBarDrawable())
                            .build();

            view.setHierarchy(hierarchy);


            container.addView(view,
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);


            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if(i==0){
                textView1.setText((position - 1) + "/" + items.size());

            }
            else {
                j=1;
                textView1.setText(items.size() + "/" + items.size());
                i=0;
            }

            container.removeView((View) object);


        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}


