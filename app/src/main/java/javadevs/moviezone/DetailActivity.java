package javadevs.moviezone;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView poster;
    public TextView overviewtxt, releaseDatetxt, ratingtxt;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        String activityTitle = "";
        poster = (ImageView)findViewById(R.id.poster);
        overviewtxt = (TextView)findViewById(R.id.txt_overview);
        releaseDatetxt = (TextView)findViewById(R.id.txt_date);
        ratingtxt = (TextView)findViewById(R.id.txt_Rating);

        overviewtxt.setText(getIntent().getStringExtra("overview"));
        releaseDatetxt.setText(getIntent().getStringExtra("releasedate"));
        Picasso.with(this)
                .load("http://image.tmdb.org/t/p/w300/"+getIntent().getStringExtra("posterpath"))
                .into(poster);
        ratingtxt.setText(getIntent().getStringExtra("voteAverage"));
        activityTitle = getIntent().getStringExtra("title");
        this.setTitle(activityTitle);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartIntent();
                Toast.makeText(getApplicationContext(),"Works",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void StartIntent() {
        Intent shared = new Intent((Intent.ACTION_SEND));
        String userInfoSend = "Check out this awesome Movie @"+getIntent().getStringExtra("title")+"\n"+getIntent().getStringExtra("overview")+"\n"+"Release Date"+getIntent().getStringExtra("voteAverage");
        shared.setType("text/plain");
        shared.putExtra( Intent.EXTRA_TEXT,userInfoSend);
        shared.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        startActivity(shared);

    }
}
