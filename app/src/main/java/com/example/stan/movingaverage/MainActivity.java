package com.example.stan.movingaverage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.affectiva.android.affdex.sdk.Frame;
import com.affectiva.android.affdex.sdk.detector.CameraDetector;
import com.affectiva.android.affdex.sdk.detector.Face;

import java.util.List;


public class MainActivity extends AppCompatActivity implements CameraDetector.CameraEventListener, CameraDetector.ImageListener{
    SurfaceView cameraDetectorSurfaceView;
    CameraDetector cameraDetector;
    int maxProcessingRate = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraDetectorSurfaceView=(SurfaceView)findViewById(R.id.cameraDetectorSurfaceView);
        cameraDetector = new CameraDetector(this,CameraDetector.CameraType.CAMERA_FRONT,cameraDetectorSurfaceView);

        cameraDetector.setMaxProcessRate(maxProcessingRate);
        cameraDetector.setImageListener(this);
        cameraDetector.setOnCameraEventListener(this);

        cameraDetector.setDetectAllEmotions(true);
        cameraDetector.start();

    }

    @Override
    public void onCameraSizeSelected(int cameraHeight, int cameraWidth, Frame.ROTATE rotate) {
        // Grab Layout Paras from the Surface View.
        ViewGroup.LayoutParams paras = cameraDetectorSurfaceView.getLayoutParams();

        // Change height and width given by Affective SDK.
        paras.height = cameraHeight;
        paras.width = cameraWidth;

        // Set it with new sizing.
        cameraDetectorSurfaceView.setLayoutParams(paras);
    }

    @Override
    public void onImageResults(List<Face> faces, Frame frame, float timeStamp) {
        // check if the frame was processed.
        if(faces == null)
            return;//frame was not processed.
        // check if there is any face recognized.
        if(faces.size() == 0)
            return;//no face
        Face face = faces.get(0);
        float joy = face.emotions.getJoy();
        float anger = face.emotions.getAnger();
        float surprise = face.emotions.getSurprise();

        System.out.println("Joy: " + joy);
        System.out.println("Anger: " + anger);
        System.out.println("Surprise: " + surprise);
    }
}
