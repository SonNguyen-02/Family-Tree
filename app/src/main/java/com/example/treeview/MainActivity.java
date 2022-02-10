package com.example.treeview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.treeview.adapter.CustomTreeEdgeDecoration;
import com.example.treeview.adapter.GraphAdapter;
import com.example.treeview.model.Couple;
import com.example.treeview.model.People;
import com.otaliastudios.zoom.ScaledPoint;
import com.otaliastudios.zoom.ZoomLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import dev.bandb.graphview.graph.Graph;
import dev.bandb.graphview.graph.Node;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ZoomLayout zoomLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpGraphView();

        zoomLayout = findViewById(R.id.zoom_layout);
        Button btnSave = findViewById(R.id.btn_save);
        btnSave.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                zoomLayout.zoomTo(1, false);
                saveImage(zoomLayout);
            } else {
                askPermission();
            }
        });
    }

    private void setUpGraphView() {
        RecyclerView rcvGraphView = findViewById(R.id.recycler);

        BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(150)
                .setSubtreeSeparation(150)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();

        rcvGraphView.setLayoutManager(new BuchheimWalkerLayoutManager(this, configuration));

        rcvGraphView.addItemDecoration(new CustomTreeEdgeDecoration());

        Graph graph = new Graph();

        // level 1
        Couple couple1 = new Couple(new People("King IV", "", R.drawable.test_image, People.MALE),
                new People("Queen elizabeth", "The queen mother", R.drawable.test_image, People.FEMALE, false));
        Node node1 = new Node(couple1);

        // level 2
        Couple couple2 = new Couple(new People("Prince Philip", "Duck of Edin", R.drawable.test_image, People.MALE, false),
                new People("Queen elizabeth II", "", R.drawable.test_image, People.FEMALE));
        Node node2 = new Node(couple2);
        Node node3 = new Node(new People("Princess margaret", "", R.drawable.test_image, People.FEMALE));

        // level 3
        Couple couple3 = new Couple(new People("Charles", "Prince of Wales", R.drawable.test_image, People.MALE),
                new People("Diana", "Princess of Wales", R.drawable.test_image, People.FEMALE, false));
        Node node4 = new Node(couple3);
        Node node5 = new Node(new People("Anne", "Princess Royal", R.drawable.test_image, People.FEMALE));
        Node node6 = new Node(new People("Prince Andrew", "Duck of York", R.drawable.test_image, People.MALE));
        Node node7 = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));

        // level 4
        Couple couple4 = new Couple(new People("Catherine", "Duchess of Cambridge", R.drawable.test_image, People.FEMALE, false),
                new People("Prince William", "Duchess of Cambridge", R.drawable.test_image, People.MALE));
        Node node8 = new Node(couple4);

        Couple couple5 = new Couple(new People("Prince Harry", "", R.drawable.test_image, People.MALE),
                new People("Meghan Markle", "", R.drawable.test_image, People.FEMALE, false));
        Node node9 = new Node(couple5);

        // level 5
        Node node10 = new Node(new People("Prince George of Cambridge", "", R.drawable.test_image, People.MALE));
        Node node11 = new Node(new People("Prince Charlotte of Cambridge", "", R.drawable.test_image, People.FEMALE));
        Node node12 = new Node(new People("Prince Louis of Cambridge", "", R.drawable.test_image, People.MALE));

        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node4);
        graph.addEdge(node2, node5);
        graph.addEdge(node2, node6);
        graph.addEdge(node2, node7);
        graph.addEdge(node4, node8);
        graph.addEdge(node4, node9);
        graph.addEdge(node8, node10);
        graph.addEdge(node8, node11);
        graph.addEdge(node8, node12);

        GraphAdapter adapter = new GraphAdapter();
        adapter.submitGraph(graph);
        rcvGraphView.setAdapter(adapter);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage(zoomLayout);
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void saveImage(@NonNull View view) {
        view.setDrawingCacheEnabled(true);
        Bitmap bm = view.getDrawingCache();
        Canvas canvas = new Canvas(bm);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SaveImage");
        if (!dir.exists()) {
            dir.mkdir();
        }
        File file = new File(dir, System.currentTimeMillis() + ".png");
        try (FileOutputStream fout = new FileOutputStream(file)) {
            bm.compress(Bitmap.CompressFormat.PNG, 100, fout);
            Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
            fout.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Save Error!!", Toast.LENGTH_SHORT).show();
        } finally {
            view.destroyDrawingCache();
        }
    }
}