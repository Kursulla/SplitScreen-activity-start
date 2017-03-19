package rs.webnet.splitscreenanim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;

public class MainActivity extends Activity {



    private RelativeLayout container;
    private SplitScreenEffect splitScreenEffect;


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        container = (RelativeLayout) findViewById(R.id.container);

        splitScreenEffect = new SplitScreenEffect(this,container);

        ImageView sample = (ImageView)findViewById(R.id.sample);
        sample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Test1.class);
                startActivity(intent);
            }
        });

        Button split = (Button) findViewById(R.id.btn);

        split.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhotoPickerActivity.class);
                splitScreenEffect.startActivity(intent);
            }
        });


    }



    @Override
    protected void onResume() {
        super.onResume();


        splitScreenEffect.onResume();


    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
