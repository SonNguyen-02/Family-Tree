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

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

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
        initData();
    }

    private void initData() {
        rcvFamilyTree = findViewById(R.id.rcv_family_tree);

        BuchheimWalkerConfiguration configuration = new BuchheimWalkerConfiguration.Builder()
                .setSiblingSeparation(100)
                .setLevelSeparation(150)
                .setSubtreeSeparation(150)
                .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
                .build();

        rcvFamilyTree.setLayoutManager(new BuchheimWalkerLayoutManager(this, configuration));

        rcvFamilyTree.addItemDecoration(new CustomTreeEdgeDecoration());


        GraphAdapter adapter = new GraphAdapter(this);
        adapter.submitGraph(getGraph());
        rcvFamilyTree.setAdapter(adapter);
    }

    @NonNull
    private Graph getGraph() {
        Graph graph = new Graph();

        MultiplePeople rootFamily = new MultiplePeople(
                new People("King IV", "", R.drawable.test_image, People.MALE),
                new People("Queen elizabeth", "The queen mother", R.drawable.test_image, People.FEMALE, false));

        graph.addNode(new Node(rootFamily));

        int i = 0;
        Random random = new Random();
        while (i <= 30) {
            Node node = graph.getNodeAtPosition(i);
            if (node.getData() instanceof MultiplePeople) {
                switch (random.nextInt(5)) {
                    case 0:
                        graph.addEdge(node, new Node(getCouple()));
                        graph.addEdge(node, new Node(getPeople()));
                        graph.addEdge(node, new Node(getCouple()));
                        break;
                    case 1:
                        graph.addEdge(node, new Node(getPeople()));
                        graph.addEdge(node, new Node(getCouple()));
                        break;
                    case 2:
                        graph.addEdge(node, new Node(getPeople()));
                        graph.addEdge(node, new Node(getCouple()));
                        graph.addEdge(node, new Node(getPeople()));
                        graph.addEdge(node, new Node(getPeople()));
                        break;
                    case 3:
                        graph.addEdge(node, new Node(getTrip()));
                        graph.addEdge(node, new Node(getPeople()));
                        graph.addEdge(node, new Node(getPeople()));
                        break;
                    case 4:
                        graph.addEdge(node, new Node(getTrip()));
                        graph.addEdge(node, new Node(getCouple()));
                        break;
                }
            }
            i++;
        }
        return graph;
    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    private People getPeople() {
        return new People("Prince agar", "Prince of Wales", R.drawable.test_image, People.MALE, true, "Charles", "Diana");
    }

    @NonNull
    @Contract(" -> new")
    private MultiplePeople getCouple() {
        if (new Random().nextBoolean()) {
            return new MultiplePeople(
                    new People("Prince Philip", "Prince of Wales", R.drawable.test_image, People.MALE, false),
                    new People("Queen elizabeth II", "Duck of Edin", R.drawable.test_image, People.FEMALE));
        }
        return new MultiplePeople(
                new People("Prince Philip", "Prince of Wales", R.drawable.test_image, People.MALE),
                new People("Queen elizabeth II", "Duck of Edin", R.drawable.test_image, People.FEMALE, false));
    }

    @NonNull
    @Contract(" -> new")
    private MultiplePeople getTrip() {
        return new MultiplePeople(
                new People("Camila", "Duchess of Cornwall", R.drawable.test_image, People.FEMALE, false),
                new People("Charles", "Prince of Wales", R.drawable.test_image, People.MALE),
                new People("Diana", "Princess of Wales", R.drawable.test_image, People.FEMALE, false));
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
        if (item.getItemId() == R.id.menu_rand_family) {
            GraphAdapter adapter = new GraphAdapter(this);
            adapter.submitGraph(getGraph());
            rcvFamilyTree.setAdapter(adapter);
        }
        return super.onOptionsItemSelected(item);
    }
}