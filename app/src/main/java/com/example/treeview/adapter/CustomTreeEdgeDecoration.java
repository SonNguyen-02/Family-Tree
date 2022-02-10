package com.example.treeview.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.treeview.R;
import com.example.treeview.model.Couple;

import java.util.List;

import dev.bandb.graphview.AbstractGraphAdapter;
import dev.bandb.graphview.graph.Graph;
import dev.bandb.graphview.graph.Node;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerConfiguration;
import dev.bandb.graphview.layouts.tree.BuchheimWalkerLayoutManager;

public class CustomTreeEdgeDecoration extends RecyclerView.ItemDecoration {

    private final Paint linePaint;
    private final Path linePath;

    public CustomTreeEdgeDecoration() {
        linePath = new Path();
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeWidth(5f);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeJoin(Paint.Join.ROUND);
        linePaint.setPathEffect(new CornerPathEffect(15f));
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        Adapter adapter = parent.getAdapter();
        if (parent.getLayoutManager() == null || adapter == null) {
            return;
        }

        if (!(adapter instanceof AbstractGraphAdapter)) {
            throw new RuntimeException("TreeEdgeDecoration only works with ${AbstractGraphAdapter::class.simpleName}");
        }

        RecyclerView.LayoutManager layout = parent.getLayoutManager();
        if (!(layout instanceof BuchheimWalkerLayoutManager)) {
            throw new RuntimeException("TreeEdgeDecoration only works with ${BuchheimWalkerLayoutManager::class.simpleName}");
        }

        float offsetX = parent.getContext().getResources().getDimension(R.dimen.max_size_node) / 2;
        float offsetY = parent.getContext().getResources().getDimension(R.dimen.line_offset);

        BuchheimWalkerConfiguration configuration = ((BuchheimWalkerLayoutManager) layout).getConfiguration();
        Graph graph = ((AbstractGraphAdapter<?>) adapter).getGraph();
        if (graph != null && graph.hasNodes()) {
            List<Node> nodes = graph.getNodes();
            for (Node node : nodes) {
                List<Node> children = graph.successorsOf(node);
                for (Node child : children) {
                    Log.e("ddd", "onDraw: node: " + child.getData());
                    linePath.reset();
                    float x;
                    if (child.getData() instanceof Couple) {
                        if ((((Couple) child.getData()).getHusband().isOffspring())) {
                            x = child.getX() + offsetX;
                        } else {
                            x = child.getX() + child.getWidth() - offsetX;
                        }
                    } else {
                        x = child.getX() + child.getWidth() / 2f;
                    }
                    linePath.moveTo(x, child.getY());
                    Log.e("ddd", "onDraw: move 1: " + x + " | " + child.getY());
                    // draws a line from the child's middle-top halfway up to its parent
                    linePath.lineTo(x, child.getY() - configuration.getLevelSeparation() / 2f);
                    Log.e("ddd", "onDraw: line 1: " + x + " | " + (child.getY() - configuration.getLevelSeparation() / 2f));

                    // draws a line from the previous point to the middle of the parents width
                    linePath.lineTo(
                            node.getX() + node.getWidth() / 2f,
                            child.getY() - configuration.getLevelSeparation() / 2f
                    );
                    Log.e("ddd", "onDraw: line 2: " + (node.getX() + node.getWidth() / 2f) + " | " + (child.getY() - configuration.getLevelSeparation() / 2f));

                    // position at the middle of the level separation under the parent
                    linePath.moveTo(
                            node.getX() + node.getWidth() / 2f,
                            child.getY() - configuration.getLevelSeparation() / 2f
                    );
                    Log.e("ddd", "onDraw: move 2: " + (node.getX() + node.getWidth() / 2f) + " | " + (child.getY() - configuration.getLevelSeparation() / 2f));

                    // draws a line up to the parents middle-bottom
                    if (node.getData() instanceof Couple) {
                        linePath.lineTo(node.getX() + node.getWidth() / 2f, node.getY() + offsetY);
                    }else{
                        linePath.lineTo(node.getX() + node.getWidth() / 2f, node.getY() + node.getHeight());
                    }
                    Log.e("ddd", "onDraw: move 3: " + (node.getX() + node.getWidth() / 2f) + " | " + (node.getY() + node.getHeight()));
                    c.drawPath(linePath, linePaint);
                }
            }
        }
        super.onDraw(c, parent, state);
    }
}
