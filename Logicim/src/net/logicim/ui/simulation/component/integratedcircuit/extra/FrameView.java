package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.common.type.Float2D;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameView
{
  protected List<ArcView> frameArcViews;
  protected List<RectangleView> frameRectangleViews;
  protected List<LineView> frameLineViews;

  public FrameView(ShapeHolder shapeHolder, Color colour, float arcWidth, float left, float right, float top, float bottom)
  {
    frameArcViews = new ArrayList<>();
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, left, top, 90));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, right, top, 0));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, right, bottom, 270));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, left, bottom, 180));

    frameRectangleViews = new ArrayList<>();
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, left + arcWidth - 0.1f, top, right + arcWidth + 0.1f, bottom + arcWidth * 2));
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, left, top + arcWidth - 0.1f, arcWidth, bottom + arcWidth + 0.1f));
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, right + arcWidth, top + arcWidth - 0.1f, right + arcWidth * 2, bottom + arcWidth + 0.1f));

    frameLineViews = new ArrayList<>();
    frameLineViews.add(new LineView(shapeHolder, new Float2D(left + arcWidth, top), new Float2D(right + arcWidth, top)));
    frameLineViews.add(new LineView(shapeHolder, new Float2D(left, top + arcWidth), new Float2D(left, bottom + arcWidth)));
    frameLineViews.add(new LineView(shapeHolder, new Float2D(left + arcWidth, bottom + arcWidth * 2), new Float2D(right + arcWidth, bottom + arcWidth * 2)));
    frameLineViews.add(new LineView(shapeHolder, new Float2D(right + arcWidth * 2, top + arcWidth), new Float2D(right + arcWidth * 2, bottom + arcWidth)));
  }

  protected RectangleView createRectangleView(ShapeHolder shapeHolder, Color frameColour, float left, float top, float right, float bottom)
  {
    RectangleView rectangleView = new RectangleView(shapeHolder, new Float2D(left, top), new Float2D(right, bottom), false, true);
    rectangleView.setFillColour(frameColour);
    return rectangleView;
  }

  protected ArcView createArcView(ShapeHolder shapeHolder, Color frameColour, float width, float x, float y, int startAngle)
  {
    ArcView arcView = new ArcView(shapeHolder, new Float2D(width + x, width + y), width, startAngle, 90, true, true);
    arcView.setFillColour(frameColour);
    return arcView;
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    for (RectangleView rectangleView : frameRectangleViews)
    {
      rectangleView.paint(graphics, viewport);
    }
    for (ArcView arcView : frameArcViews)
    {
      arcView.paint(graphics, viewport);
    }
    for (LineView lineView : frameLineViews)
    {
      lineView.paint(graphics, viewport);
    }
  }
}

