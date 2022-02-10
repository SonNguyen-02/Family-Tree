package com.example.treeview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.treeview.adapter.CustomTreeEdgeDecoration;
import com.example.treeview.adapter.GraphAdapter;
import com.example.treeview.model.MultiplePeople;
import com.example.treeview.model.People;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import dev.bandb.graphview.graph.Graph;
import dev.bandb.graphview.graph.Node;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private RecyclerView rcvFamilyTree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rcvFamilyTree = findViewById(R.id.rcv_family_tree);
        setUpGraphView();
    }

    private void setUpGraphView() {

        BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(150)
                .setSubtreeSeparation(150)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();

        rcvFamilyTree.setLayoutManager(new BuchheimWalkerLayoutManager(this, configuration));

        rcvFamilyTree.addItemDecoration(new CustomTreeEdgeDecoration());

        Graph graph = new Graph();

        // level 1
        MultiplePeople couple1 = new MultiplePeople(new People("King IV", "", R.drawable.test_image, People.MALE),
                new People("Queen elizabeth", "The queen mother", R.drawable.test_image, People.FEMALE, false));
        Node node1 = new Node(couple1);

        // level 2
        MultiplePeople couple2 = new MultiplePeople(new People("Prince Philip", "Duck of Edin", R.drawable.test_image, People.MALE, false),
                new People("Queen elizabeth II", "", R.drawable.test_image, People.FEMALE));
        Node node2 = new Node(couple2);
        Node node3 = new Node(new People("Princess margaret", "", R.drawable.test_image, People.FEMALE));

        // level 3
        MultiplePeople couple3 = new MultiplePeople(
                new People("Camila", "Duchess of Cornwall", R.drawable.test_image, People.FEMALE, false),
                new People("Charles", "Prince of Wales", R.drawable.test_image, People.MALE),
                new People("Diana", "Princess of Wales", R.drawable.test_image, People.FEMALE, false));
        Node node4 = new Node(couple3);
        Node node5 = new Node(new People("Anne", "Princess Royal", R.drawable.test_image, People.FEMALE));
        Node node6 = new Node(new People("Prince Andrew", "Duck of York", R.drawable.test_image, People.MALE));
        Node node7 = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));

        // level 4
//        Node node8 = new Node(new People("Prince Rook", "", R.drawable.test_image, People.MALE, true, "Charles", "Camila"));
        MultiplePeople couple11 = new MultiplePeople(new People("Prince Rook", "", R.drawable.test_image, People.MALE, true, "Charles", "Camila"),
                new People("Queen elizabeth II", "", R.drawable.test_image, People.FEMALE, false));
        Node node8 = new Node(couple11);
        Node node8a = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));
        Node node8b = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));
        Node node8c = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));

        MultiplePeople couple4 = new MultiplePeople(new People("Catherine", "Duchess of Cambridge", R.drawable.test_image, People.FEMALE, false),
                new People("Prince William", "Duchess of Cambridge", R.drawable.test_image, People.MALE, true, "Charles", "Diana"));
        Node node9 = new Node(couple4);

        MultiplePeople couple5 = new MultiplePeople(new People("Prince Harry", "", R.drawable.test_image, People.MALE, true, "Charles", "Diana"),
                new People("Meghan Markle", "", R.drawable.test_image, People.FEMALE, false));
        Node node10 = new Node(couple5);
        Node node10a = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));
        Node node10b = new Node(new People("Prince Edward", "Earl of Wessex", R.drawable.test_image, People.MALE));

        // level 5
        Node node11 = new Node(new People("Prince George of Cambridge", "", R.drawable.test_image, People.MALE));
        Node node12 = new Node(new People("Prince Charlotte of Cambridge", "", R.drawable.test_image, People.FEMALE));
        Node node13 = new Node(new People("Prince Louis of Cambridge", "", R.drawable.test_image, People.MALE));

        // them
        MultiplePeople couple6 = new MultiplePeople(new People("Catherine", "Duchess of Cambridge", R.drawable.test_image, People.FEMALE, false),
                new People("Prince William", "Duchess of Cambridge", R.drawable.test_image, People.MALE, true, "Charles", "Diana"));
        Node node14 = new Node(couple6);
        MultiplePeople couple7 = new MultiplePeople(new People("Prince Harry", "", R.drawable.test_image, People.MALE, true, "Charles", "Diana"),
                new People("Meghan Markle", "", R.drawable.test_image, People.FEMALE, false));
        Node node15 = new Node(couple7);

        MultiplePeople couple8 = new MultiplePeople(
                new People("Camila", "Duchess of Cornwall", R.drawable.test_image, People.FEMALE, false),
                new People("Charles", "Prince of Wales", R.drawable.test_image, People.MALE),
                new People("Diana", "Princess of Wales", R.drawable.test_image, People.FEMALE, false));
        Node node16 = new Node(couple8);

        MultiplePeople couple9 = new MultiplePeople(new People("Prince Harry", "", R.drawable.test_image, People.MALE, true, "Charles", "Diana"),
                new People("Meghan Markle", "", R.drawable.test_image, People.FEMALE, false));
        Node node17 = new Node(couple9);

        MultiplePeople couple10 = new MultiplePeople(new People("Catherine", "Duchess of Cambridge", R.drawable.test_image, People.FEMALE, false),
                new People("Prince William", "Duchess of Cambridge", R.drawable.test_image, People.MALE, true, "Charles", "Diana"));
        Node node18 = new Node(couple10);

        graph.addEdge(node1, node2);
        graph.addEdge(node1, node3);
        graph.addEdge(node2, node4);
        graph.addEdge(node2, node5);
        graph.addEdge(node2, node14);
        graph.addEdge(node14, node16);
        graph.addEdge(node16, node18);

        graph.addEdge(node2, node6);
        graph.addEdge(node2, node15);
        graph.addEdge(node15, node17);
        graph.addEdge(node2, node7);

        graph.addEdge(node4, node8);
        graph.addEdge(node8, node8a);
        graph.addEdge(node8, node8b);
        graph.addEdge(node8, node8c);
        graph.addEdge(node4, node9);
        graph.addEdge(node4, node10);
        graph.addEdge(node10, node10a);
        graph.addEdge(node10, node10b);
        graph.addEdge(node9, node11);
        graph.addEdge(node9, node12);
        graph.addEdge(node9, node13);

        GraphAdapter adapter = new GraphAdapter(this);
        adapter.submitGraph(graph);
        rcvFamilyTree.setAdapter(adapter);
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveImage(getBitmapFromView(rcvFamilyTree), this, "TreeView");
            } else {
                Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public Bitmap getBitmapFromView(@NonNull View view) {
        Log.e("ddd", "saveImage: " + view.getLeft() + " | " + view.getTop() + " | " + view.getRight() + " | " + view.getBottom());
        Bitmap bm = Bitmap.createBitmap(view.getRight(), view.getBottom(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bm);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bm;
    }

    private void saveImage(Bitmap bitmap, Context context, String folderName) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName);
            values.put(MediaStore.Images.Media.IS_PENDING, true);
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try {
                    saveImageToStream(bitmap, context.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    context.getContentResolver().update(uri, values, null, null);
                    Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, "Save Error!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + "/" + folderName);
            // getExternalStorageDirectory is deprecated in API 29
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String fileName = System.currentTimeMillis() + ".png";
            File file = new File(directory, fileName);
            try {
                saveImageToStream(bitmap, new FileOutputStream(file));
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                // .DATA is deprecated in API 29
                context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                Toast.makeText(this, "Successfully saved", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(this, "Save Error!!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void saveImageToStream(Bitmap bitmap, OutputStream outputStream) throws IOException {
        if (outputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_export) {
            new AlertDialog.Builder(this).setMessage("Confirm to export file png!")
                    .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            saveImage(getBitmapFromView(rcvFamilyTree), this, "TreeView");
                        } else {
                            askPermission();
                        }
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }
}