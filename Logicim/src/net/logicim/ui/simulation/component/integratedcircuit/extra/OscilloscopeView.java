package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.extra.OscilloscopeData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.extra.Oscilloscope;
import net.logicim.domain.integratedcircuit.extra.OscilloscopePins;
import net.logicim.domain.integratedcircuit.extra.OscilloscopeState;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.arc.ArcView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.Units.GHz;
import static net.logicim.ui.common.VoltageColour.clamp;
import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public class OscilloscopeView
    extends IntegratedCircuitView<Oscilloscope, OscilloscopeProperties>
{
  protected List<ArcView> frameArcViews;
  protected List<RectangleView> frameRectangleViews;
  protected List<LineView> frameLineViews;

  protected RectangleView rectangleView;
  protected float outerWidth;

  public OscilloscopeView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, OscilloscopeProperties properties)
  {
    super(circuitEditor, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  protected void createGraphics()
  {
    frameRectangleViews = new ArrayList<>();
    frameArcViews = new ArrayList<>();
    frameLineViews = new ArrayList<>();

    Color frameColour = new Color(0, 208, 208);

    outerWidth = 1.0f;
    float bottom = (properties.inputCount - 1) * properties.divHeightInGrids;
    int top = -4;
    frameArcViews.add(createArcView(frameColour, outerWidth, 0, top, 90));
    frameArcViews.add(createArcView(frameColour, outerWidth, properties.numberOfDivsWide, top, 0));
    frameArcViews.add(createArcView(frameColour, outerWidth, properties.numberOfDivsWide, bottom, 270));
    frameArcViews.add(createArcView(frameColour, outerWidth, 0, bottom, 180));

    frameRectangleViews.add(createRectangleView(frameColour, outerWidth - 0.1f, top, properties.numberOfDivsWide + outerWidth + 0.1f, bottom + outerWidth * 2));
    frameRectangleViews.add(createRectangleView(frameColour, 0, top + outerWidth - 0.1f, outerWidth, bottom + outerWidth + 0.1f));
    frameRectangleViews.add(createRectangleView(frameColour, properties.numberOfDivsWide + outerWidth, top + outerWidth - 0.1f, properties.numberOfDivsWide + outerWidth * 2, bottom + outerWidth + 0.1f));

    frameLineViews.add(new LineView(this, new Float2D(outerWidth, top), new Float2D(properties.numberOfDivsWide + outerWidth, top)));
    frameLineViews.add(new LineView(this, new Float2D(0, top + outerWidth), new Float2D(0, bottom + outerWidth)));
    frameLineViews.add(new LineView(this, new Float2D(outerWidth, bottom + outerWidth * 2), new Float2D(properties.numberOfDivsWide + outerWidth, bottom + outerWidth * 2)));
    frameLineViews.add(new LineView(this, new Float2D(properties.numberOfDivsWide + outerWidth * 2, top + outerWidth), new Float2D(properties.numberOfDivsWide + outerWidth * 2, bottom + outerWidth)));

    rectangleView = new RectangleView(this, new Float2D(outerWidth * 1 - 0.2f, -outerWidth * 3), new Float2D(properties.numberOfDivsWide + outerWidth + 0.2f, bottom + outerWidth), true, true);
    rectangleView.setFillColour(Color.WHITE);
  }

  protected void createPortViews()
  {
    for (int portNumber = 0; portNumber < properties.inputCount; portNumber++)
    {
      new PortView(this, integratedCircuit.getPort("Input " + portNumber), new Int2D(0, portNumber * properties.divHeightInGrids));
    }
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
  protected Oscilloscope createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Oscilloscope(circuitEditor.getCircuit(),
                            properties.name,
                            new OscilloscopePins(properties.inputCount),
                            properties.samplingFrequency_Hz,
                            properties.numberOfDivsWide,
                            properties.samplesPerDiv,
                            Colours.getInstance());
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

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
      rectangleView.paint(graphics, viewport);

      OscilloscopeState state = integratedCircuit.getState();
      if (state != null)
      {
        float[][] minimumVoltageGrid = state.getMinVoltage();
        float[][] maximumVoltageGrid = state.getMaxVoltage();
        int[][] colourGrid = state.getColour();
        int tickPosition = state.getTickPosition();

        int tickX = viewport.transformGridToScreenSpaceX(1 + ((float) tickPosition / properties.samplesPerDiv) + position.x);

        graphics.setStroke(viewport.getAbsoluteStroke(viewport.getDefaultLineWidth() / 2));
        Color oscilloscopeReferenceColour = new Color(232, 232, 232);
        graphics.setColor(oscilloscopeReferenceColour);
        float bottom = (properties.inputCount - 1) * properties.divHeightInGrids + outerWidth - 0.25f;
        float top = -outerWidth * 3.0f + 0.25f;

        graphics.drawLine(tickX, viewport.transformGridToScreenSpaceY(top + position.y), tickX, viewport.transformGridToScreenSpaceY(bottom + position.y));

        for (int input = 0; input < minimumVoltageGrid.length; input++)
        {
          PortView port = getPort(input);
          float[] minimumVoltageLine = minimumVoltageGrid[input];
          float[] maximumVoltageLine = maximumVoltageGrid[input];
          int[] colourLine = colourGrid[input];

          int portX1 = viewport.transformGridToScreenSpaceX(port.getGridPosition().x + 1);
          int portX2 = viewport.transformGridToScreenSpaceX(port.getGridPosition().x + properties.numberOfDivsWide);
          int lineY = viewport.transformGridToScreenSpaceY(port.getGridPosition().y);
          graphics.setColor(oscilloscopeReferenceColour);
          graphics.drawLine(portX1, lineY, portX2, lineY);

          for (int i = 0; i < minimumVoltageLine.length; i++)
          {
            float minimumVoltage = minimumVoltageLine[i];
            float maximumVoltage = maximumVoltageLine[i];
            int colour = colourLine[i];

            float behind;
            if (tickPosition >= i)
            {
              behind = tickPosition - i;
            }
            else
            {
              behind = tickPosition + minimumVoltageLine.length - i;
            }
            behind = minimumVoltageLine.length - behind;

            Color c = new Color(colour);
            if (behind < minimumVoltageLine.length / 4.0f)
            {
              float colourFraction = behind / (minimumVoltageLine.length / 4.0f);
              float whiteFraction = 1.0f - colourFraction;
              c = new Color(clamp((int) (c.getRed() * colourFraction + 255 * whiteFraction)),
                            clamp((int) (c.getGreen() * colourFraction + 255 * whiteFraction)),
                            clamp((int) (c.getBlue() * colourFraction + 255 * whiteFraction)));
            }

            int portX = viewport.transformGridToScreenSpaceX(port.getGridPosition().x + 1 + ((float) i / properties.samplesPerDiv));
            int portY1 = viewport.transformGridToScreenSpaceY(port.getGridPosition().y - (minimumVoltage / 8.0f) * properties.divHeightInGrids);
            int portY2 = viewport.transformGridToScreenSpaceY(port.getGridPosition().y - (maximumVoltage / 8.0f) * properties.divHeightInGrids);

            graphics.setColor(c);
            graphics.drawRect(portX, portY2, 1, portY1 - portY2 + 1);
          }
        }
      }
      paintPorts(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  int[][] cloneInt2DArray(int[][] matrix)
  {
    int[][] cloned = new int[matrix.length][];
    for (int i = 0; i < matrix.length; i++)
    {
      cloned[i] = matrix[i].clone();
    }
    return cloned;
  }

  float[][] cloneFloat2DArray(float[][] matrix)
  {
    float[][] cloned = new float[matrix.length][];
    for (int i = 0; i < matrix.length; i++)
    {
      cloned[i] = matrix[i].clone();
    }
    return cloned;
  }

  @Override
  public IntegratedCircuitData<?, ?> save(boolean selected)
  {
    return new OscilloscopeData(position,
                                rotation,
                                properties.name,
                                properties.family.getFamily(),
                                saveEvents(),
                                savePorts(),
                                selected,
                                saveState(),
                                properties.inputCount,
                                properties.numberOfDivsWide,
                                properties.samplesPerDiv,
                                properties.divHeightInGrids,
                                properties.samplingFrequency_Hz);
  }

  public OscilloscopeState saveState()
  {
    OscilloscopeState state = getIntegratedCircuit().getState();
    return new OscilloscopeState(properties.numberOfDivsWide * properties.samplesPerDiv,
                                 cloneFloat2DArray(state.getMinVoltage()),
                                 cloneFloat2DArray(state.getMaxVoltage()),
                                 cloneInt2DArray(state.getColour()),
                                 state.getTickPosition());
  }

  @Override
  public String getType()
  {
    return "Oscilloscope";
  }

  @Override
  public void propertyChanged()
  {
    properties.inputCount = clamp(properties.inputCount, 1, 24);
    properties.numberOfDivsWide = clamp(properties.numberOfDivsWide, 4, 128);
    properties.divHeightInGrids = clamp(properties.divHeightInGrids, 2, 6);
    properties.samplesPerDiv = clamp(properties.samplesPerDiv, 4, 64);
    properties.samplingFrequency_Hz = clamp(properties.samplingFrequency_Hz, 0.01f, 999 * GHz);
  }
}

