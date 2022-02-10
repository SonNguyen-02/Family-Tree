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
import com.example.treeview.model.MultiplePeople;
import com.example.treeview.model.People;

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

        float nodeSize = parent.getContext().getResources().getDimension(R.dimen.max_size_node);
        float avatarRadius = parent.getContext().getResources().getDimension(R.dimen.avatar_size) / 2f;
        float space = (nodeSize - avatarRadius * 2) / 2f;

        BuchheimWalkerConfiguration configuration = ((BuchheimWalkerLayoutManager) layout).getConfiguration();
        Graph graph = ((AbstractGraphAdapter<?>) adapter).getGraph();
        if (graph != null && graph.hasNodes()) {
            List<Node> nodes = graph.getNodes();
            for (Node node : nodes) {
                if (node.getData() instanceof MultiplePeople) {
                    MultiplePeople multiplePeople = (MultiplePeople) node.getData();
                    linePath.reset();
                    for (int i = 1; i < multiplePeople.size(); i++) {
                        float startX = node.getX() + i * nodeSize - space;
                        float startY = node.getY() + avatarRadius;
                        linePath.moveTo(startX, startY);
                        linePath.lineTo(startX + 2 * space, startY);
                    }
                    c.drawPath(linePath, linePaint);
                }
                List<Node> children = graph.successorsOf(node);
                for (Node child : children) {
                    Log.e("ddd", "onDraw: node: " + child.getData());
                    linePath.reset();
                    float startX = 0;
                    if (child.getData() instanceof MultiplePeople) {
                        MultiplePeople multiplePeople = (MultiplePeople) child.getData();
                        for (int i = 0; i < multiplePeople.size(); i++) {
                            if (multiplePeople.get(i).isOffspring()) {
                                startX = child.getX() + (i + 1) * nodeSize - nodeSize / 2f;
                                break;
                            }
                        }
                        startX = startX == 0 ? child.getX() + child.getWidth() / 2f : startX;
                    } else {
                        startX = child.getX() + child.getWidth() / 2f;
                    }
                    linePath.moveTo(startX, child.getY());
                    // draws a line from the child's middle-top halfway up to its parent
                    linePath.lineTo(startX, child.getY() - configuration.getLevelSeparation() / 2f);

                    float endX = node.getX() + node.getWidth() / 2f, endY;
                    if (node.getData() instanceof MultiplePeople) {
                        MultiplePeople multiplePeople = (MultiplePeople) node.getData();
                        if (multiplePeople.getListPeople().size() == 3) {
                            People offspring = null;
                            if (child.getData() instanceof MultiplePeople) {
                                for (int i = 0; i < ((MultiplePeople) child.getData()).size(); i++) {
                                    if (((MultiplePeople) child.getData()).get(i).isOffspring()) {
                                        offspring = ((MultiplePeople) child.getData()).get(i);
                                    }
                                }
                            } else {
                                offspring = (People) child.getData();
                            }
                            if (offspring != null) {
                                if (multiplePeople.get(0).getName().equals(offspring.getFatherName())
                                        || multiplePeople.get(0).getName().equals(offspring.getMotherName())) {
                                    endX = node.getX() + nodeSize;
                                } else {
                                    endX = node.getX() + 2 * nodeSize;
                                }
                            }
                        }
                        endY = node.getY() + avatarRadius;
                    } else {
                        endY = node.getY() + node.getHeight();
                    }
                    // draws a line from the previous point to the middle of the parents width
                    linePath.lineTo(endX, child.getY() - configuration.getLevelSeparation() / 2f);
                    // position at the middle of the level separation under the parent
                    linePath.moveTo(endX, child.getY() - configuration.getLevelSeparation() / 2f);
                    // draws a line up to the parents middle-bottom
                    linePath.lineTo(endX, endY);

                    c.drawPath(linePath, linePaint);
                }
            }
        }
        super.onDraw(c, parent, state);
    }
}
