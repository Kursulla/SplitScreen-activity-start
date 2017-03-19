package rs.webnet.splitscreenanim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by kursulla on 4/6/14.
 * <p/>
 * Class purpose:
 */
public class PhotoPickerActivity extends Activity {


    private Button startGalleryPicker;

    private Button startCameraPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phoot_picker_fragment);

        startCameraPicker  = (Button)findViewById(R.id.start_camera_picker_btn);
        startGalleryPicker= (Button)findViewById(R.id.start_gallery_picker_btn);
//

        startGalleryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhotoPickerActivity.this, Test1.class);
                startActivity(intent);

            }
        });
        startCameraPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }


}
