package net.logicim.ui.simulation.component.integratedcircuit.extra;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.common.IntegratedCircuitData;
import net.logicim.data.integratedcircuit.extra.OscilloscopeData;
import net.logicim.data.integratedcircuit.extra.OscilloscopeProperties;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.extra.Oscilloscope;
import net.logicim.domain.integratedcircuit.extra.OscilloscopePins;
import net.logicim.domain.integratedcircuit.extra.OscilloscopeState;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.IntegratedCircuitView;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

import static net.logicim.domain.common.Units.GHz;
import static net.logicim.domain.common.voltage.VoltageColour.clamp;
import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public class OscilloscopeView
    extends IntegratedCircuitView<Oscilloscope, OscilloscopeProperties>
{
  protected RectangleView rectangleView;
  protected FrameView frameView;
  protected float outerWidth;

  public OscilloscopeView(SubcircuitView containingSubcircuitView,
                          Int2D position,
                          Rotation rotation,
                          OscilloscopeProperties properties)
  {
    super(containingSubcircuitView, position, rotation, properties);
    createGraphics();
    finaliseView();
  }

  protected void createGraphics()
  {
    Color frameColour = new Color(0, 208, 208);

    outerWidth = 1.0f;
    float bottom = (properties.inputCount - 1) * properties.divHeightInGrids;
    int top = -4;

    frameView = new FrameView(this, frameColour, outerWidth, 0, properties.numberOfDivsWide + 2 * outerWidth, top, bottom + 2 * outerWidth);

    rectangleView = new RectangleView(this, new Float2D(outerWidth - 0.2f, -outerWidth * 3), new Float2D(properties.numberOfDivsWide + outerWidth + 0.2f, bottom + outerWidth), true, true);
    rectangleView.setFillColour(Color.WHITE);
  }

  protected void createPortViews()
  {
    for (int portNumber = 0; portNumber < properties.inputCount; portNumber++)
    {
      new PortView(this, "Input " + portNumber, new Int2D(0, portNumber * properties.divHeightInGrids));
    }
  }

  @Override
  protected Oscilloscope createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new Oscilloscope(subcircuitSimulation.getCircuit(),
                            properties.name,
                            new OscilloscopePins(properties.inputCount),
                            properties.samplingFrequency_Hz,
                            properties.numberOfDivsWide,
                            properties.samplesPerDiv,
                            Colours.getInstance());
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    if ((frameView != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      frameView.paint(graphics, viewport);

      rectangleView.paint(graphics, viewport);

      Oscilloscope integratedCircuit = simulationIntegratedCircuits.get(subcircuitSimulation);
      if (integratedCircuit != null)
      {
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
            PortView port = getPortView(input);
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
      }
      paintPorts(graphics, viewport, subcircuitSimulation);

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
                                properties.family,
                                saveEvents(),
                                savePorts(),
                                id,
                                enabled,
                                selected,
                                saveSimulationState(),
                                properties.inputCount,
                                properties.numberOfDivsWide,
                                properties.samplesPerDiv,
                                properties.divHeightInGrids,
                                properties.samplingFrequency_Hz);
  }

  @Override
  protected boolean mustIncludeExplicitPowerPorts(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return false;
  }

  public OscilloscopeState saveState(Oscilloscope integratedCircuit)
  {
    OscilloscopeState state = integratedCircuit.getState();
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
  public void clampProperties(OscilloscopeProperties newProperties)
  {
    newProperties.inputCount = clamp(newProperties.inputCount, 1, 24);
    newProperties.numberOfDivsWide = clamp(newProperties.numberOfDivsWide, 4, 128);
    newProperties.divHeightInGrids = clamp(newProperties.divHeightInGrids, 2, 6);
    newProperties.samplesPerDiv = clamp(newProperties.samplesPerDiv, 4, 64);
    newProperties.samplingFrequency_Hz = clamp(newProperties.samplingFrequency_Hz, 0.01f, 999 * GHz);
  }
}

