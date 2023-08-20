package net.logicim.ui.simulation.component.integratedcircuit.standard.clock;

import net.common.type.Float2D;
import net.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.clock.ClockData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.common.voltage.VoltageColour;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.shape.line.LineView;
import net.logicim.ui.shape.rectangle.RectangleView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.domain.common.Units.GHz;
import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public class ClockView
    extends StandardIntegratedCircuitView<ClockOscillator, ClockProperties>
{
  protected RectangleView rectangle;
  protected List<LineView> clockWaveform;

  public ClockView(SubcircuitView containingSubcircuitView,
                   Int2D position,
                   Rotation rotation,
                   ClockProperties properties)
  {
    super(containingSubcircuitView,
          position,
          rotation,
          properties);
    createGraphics();
    finaliseView();
  }

  protected void createGraphics()
  {
    if (!properties.inverseOut)
    {
      rectangle = new RectangleView(this, 2, 2, true, true);
    }
    else
    {
      rectangle = new RectangleView(this, new Float2D(-0.5f, -1), new Float2D(2.5f, 1), true, true);
    }

    clockWaveform = createClockWaveform();
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();

    new PortView(this, "Output", new Int2D(0, -1));
    if (properties.inverseOut)
    {
      new PortView(this, "Output2", new Int2D(2, -1));
    }
  }

  @Override
  protected ClockOscillator createIntegratedCircuit(SubcircuitSimulation subcircuitSimulation, FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new ClockOscillator(subcircuitSimulation.getCircuit(),
                               properties.name,
                               new ClockOscillatorPins(familyVoltageConfiguration,
                                                       properties.inverseOut),
                               properties.frequency_Hz);
  }

  @Override
  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SubcircuitSimulation subcircuitSimulation)
  {
    super.paint(graphics, viewport, subcircuitSimulation);

    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);

      paintClockWaveform(graphics, viewport, subcircuitSimulation);
    }
    paintPorts(graphics, viewport, subcircuitSimulation);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }

  private void paintClockWaveform(Graphics2D graphics, Viewport viewport, SubcircuitSimulation subcircuitSimulation)
  {
    ClockOscillator integratedCircuit = simulationIntegratedCircuits.get(subcircuitSimulation);
    Color clockColor = Colours.getInstance().getDisconnectedTrace();
    if (subcircuitSimulation != null)
    {
      long time = subcircuitSimulation.getTime();
      if (integratedCircuit != null)
      {
        ClockOscillatorState state = integratedCircuit.getState();
        if (state != null)
        {
          float voltage = integratedCircuit.getInternalVoltage(time);
          clockColor = VoltageColour.getColourForVoltage(Colours.getInstance(), voltage);
        }
        else
        {
          clockColor = Colours.getInstance().getDisconnectedTrace();
        }
      }
    }

    for (LineView lineView : clockWaveform)
    {
      lineView.setBorderColour(clockColor);
      lineView.paint(graphics, viewport);
    }
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private List<LineView> createClockWaveform()
  {
    float xOffset = 0.5f;
    float xi = 0;
    float yi = 0;
    if (properties.inverseOut && rotation.isNorthSouth())
    {
      xi = 1f;
    }
    if (properties.inverseOut && rotation.isEastWest())
    {
      yi = 1f;
    }
    float yOffset = 0.33f;

    List<LineView> lineViews = new ArrayList<>();

    float x1 = (-xOffset + xi);
    float y1 = (+0.0f + yi);
    float x2 = (-xOffset + xi);
    float y2 = (-yOffset + yi);
    lineViews.add(new LineView(this, new Float2D(y1, x1), new Float2D(y2, x2)));

    float x3 = (+0.0f + xi);
    float y3 = (-yOffset + yi);
    lineViews.add(new LineView(this, new Float2D(y2, x2), new Float2D(y3, x3)));

    float x4 = (+0.0f + xi);
    float y4 = (+yOffset + yi);
    lineViews.add(new LineView(this, new Float2D(y3, x3), new Float2D(y4, x4)));

    float x5 = (+xOffset + xi);
    float y5 = (+yOffset + yi);
    lineViews.add(new LineView(this, new Float2D(y4, x4), new Float2D(y5, x5)));

    float x6 = (+xOffset + xi);
    float y6 = (+0.0f + yi);
    lineViews.add(new LineView(this, new Float2D(y5, x5), new Float2D(y6, x6)));

    return lineViews;
  }

  public ClockData save(boolean selected)
  {
    return new ClockData(position,
                         rotation,
                         properties.name,
                         properties.family,
                         properties.frequency_Hz,
                         saveEvents(),
                         savePorts(),
                         id,
                         enabled,
                         selected,
                         saveSimulationState(),
                         properties.inverseOut,
                         properties.explicitPowerPorts);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected ClockOscillatorState saveState(ClockOscillator integratedCircuit)
  {
    return new ClockOscillatorState(integratedCircuit.getState().getState());
  }

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }

  @Override
  public void clampProperties(ClockProperties newProperties)
  {
    newProperties.frequency_Hz = clamp(newProperties.frequency_Hz, 0, 999 * GHz);
  }

  @Override
  public String toDebugString()
  {
    return super.toDebugString() + String.format("Frequency [%.0f]\nInverse Out [%s]\n", properties.frequency_Hz, properties.inverseOut);
  }
}

