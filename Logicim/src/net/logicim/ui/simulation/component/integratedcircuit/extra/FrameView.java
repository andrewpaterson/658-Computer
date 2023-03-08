package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.common.type.Float2D;
import net.logicim.ui.common.ShapeHolder;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class FrameView
{
  protected List<ArcView> frameArcViews;
  protected List<RectangleView> frameRectangleViews;

  public FrameView(ShapeHolder shapeHolder, Color colour, float arcWidth, float rectangleWidth, float top, float bottom)
  {
    frameArcViews = new ArrayList<>();
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, 0, top, 90));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, rectangleWidth, top, 0));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, rectangleWidth, bottom, 270));
    frameArcViews.add(createArcView(shapeHolder, colour, arcWidth, 0, bottom, 180));

    frameRectangleViews = new ArrayList<>();
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, arcWidth - 0.1f, top, rectangleWidth + arcWidth + 0.1f, bottom + arcWidth * 2));
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, 0, top + arcWidth - 0.1f, arcWidth, bottom + arcWidth + 0.1f));
    frameRectangleViews.add(createRectangleView(shapeHolder, colour, rectangleWidth + arcWidth, top + arcWidth - 0.1f, rectangleWidth + arcWidth * 2, bottom + arcWidth + 0.1f));
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
  }
}

