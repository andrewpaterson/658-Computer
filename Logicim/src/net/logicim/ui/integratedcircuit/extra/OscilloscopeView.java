package net.logicim.ui.integratedcircuit.extra;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.domain.integratedcircuit.extra.Oscilloscope;
import net.logicim.domain.integratedcircuit.extra.OscilloscopePins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.IntegratedCircuitView;
import net.logicim.ui.common.PortView;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OscilloscopeView
    extends IntegratedCircuitView<Oscilloscope>
{
  protected int inputCount;
  protected int width;
  protected List<ArcView> frameArcViews;
  protected List<RectangleView> frameRectangleViews;
  protected List<LineView> frameLineViews;

  public OscilloscopeView(CircuitEditor circuitEditor,
                          int inputCount,
                          int width,
                          Int2D position,
                          Rotation rotation,
                          String name)
  {
    super(circuitEditor, position, rotation, name);
    this.inputCount = inputCount;
    this.width = width;
    create();

    frameRectangleViews = new ArrayList<>();
    frameArcViews = new ArrayList<>();
    frameLineViews = new ArrayList<>();

    Color frameColour = new Color(0, 208, 208);

    float outerWidth = 1.0f;
    int height = 4;
    float bottom = (inputCount - 1) * height + (height / 2.0f);
    int top = -4;
    frameArcViews.add(createArcView(frameColour, outerWidth, 0, top, 90));
    frameArcViews.add(createArcView(frameColour, outerWidth, width, top, 0));
    frameArcViews.add(createArcView(frameColour, outerWidth, width, bottom, 270));
    frameArcViews.add(createArcView(frameColour, outerWidth, 0, bottom, 180));

    frameRectangleViews.add(createRectangleView(frameColour, outerWidth - 0.1f, top, width + outerWidth + 0.1f, bottom + outerWidth * 2));
    frameRectangleViews.add(createRectangleView(frameColour, 0, top + outerWidth - 0.1f, outerWidth , bottom + outerWidth + 0.1f));
    frameRectangleViews.add(createRectangleView(frameColour, width + outerWidth, top + outerWidth - 0.1f, width + outerWidth * 2, bottom + outerWidth + 0.1f));

    frameLineViews.add(new LineView(this, new Float2D(outerWidth, top), new Float2D(width + outerWidth,  top)));
    frameLineViews.add(new LineView(this, new Float2D(0, top + outerWidth), new Float2D(0, bottom + outerWidth)));
    frameLineViews.add(new LineView(this, new Float2D(outerWidth, bottom + outerWidth * 2), new Float2D(width + outerWidth, bottom + outerWidth * 2)));
    frameLineViews.add(new LineView(this, new Float2D(width + outerWidth * 2, top + outerWidth), new Float2D(width + outerWidth * 2, bottom + outerWidth)));

    for (int portNumber = 0; portNumber < inputCount; portNumber++)
    {
      new PortView(this, this.integratedCircuit.getPort("Input " + portNumber), new Int2D(0, portNumber * height));
    }

    finaliseView();
  }

  protected RectangleView createRectangleView(Color frameColour, float left, float top, float right, float bottom)
  {
    RectangleView rectangleView = new RectangleView(this, new Float2D(left, top), new Float2D(right, bottom), false, true);
    rectangleView.setFillColour(frameColour);
    return rectangleView;
  }

  protected ArcView createArcView(Color frameColour, float width, float x, float y, int startAngle)
  {
    ArcView arcView = new ArcView(this, new Float2D(width + x, width + y), width, startAngle, 90, true, true);
    arcView.setFillColour(frameColour);
    return arcView;
  }

  @Override
  protected Oscilloscope createIntegratedCircuit()
  {
    return new Oscilloscope(circuitEditor.getCircuit(), "", new OscilloscopePins(inputCount));
  }

  @Override
  public IntegratedCircuitData<?> save()
  {
    return null;
  }

  @Override
  protected void updateBoundingBox()
  {
    super.updateBoundingBox();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if ((frameArcViews != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

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

      super.paint(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }
}

