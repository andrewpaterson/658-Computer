package net.logicim.ui.simulation.component.integratedcircuit.standard.clock;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.clock.ClockData;
import net.logicim.domain.common.propagation.FamilyVoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.common.*;
import net.logicim.ui.common.port.PortView;
import net.logicim.ui.simulation.component.integratedcircuit.standard.common.StandardIntegratedCircuitView;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

import static net.logicim.domain.common.Units.GHz;
import static net.logicim.ui.common.integratedcircuit.PropertyClamp.clamp;

public class ClockView
    extends StandardIntegratedCircuitView<ClockOscillator, ClockProperties>
{
  protected RectangleView rectangle;

  public ClockView(CircuitEditor circuitEditor,
                   Int2D position,
                   Rotation rotation,
                   ClockProperties properties)
  {
    super(circuitEditor,
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
  }

  @Override
  protected void createPortViews()
  {
    super.createPortViews();

    new PortView(this, integratedCircuit.getPort("Output"), new Int2D(0, -1));
    if (properties.inverseOut)
    {
      new PortView(this, integratedCircuit.getPort("Output2"), new Int2D(2, -1));
    }
  }

  @Override
  protected ClockOscillator createIntegratedCircuit(FamilyVoltageConfiguration familyVoltageConfiguration)
  {
    return new ClockOscillator(circuitEditor.getCircuit(), properties.name,
                               new ClockOscillatorPins(familyVoltageConfiguration, properties.inverseOut), properties.frequency_Hz);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    super.paint(graphics, viewport, time);

    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    if (rectangle != null)
    {
      rectangle.paint(graphics, viewport);

      paintClockWaveform(graphics, viewport, time);
    }
    paintPorts(graphics, viewport, time);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }

  private void paintClockWaveform(Graphics2D graphics, Viewport viewport, long time)
  {
    ClockOscillatorState state = integratedCircuit.getState();
    Color clockColor;
    if (state != null)
    {
      float voltage = integratedCircuit.getInternalVoltage(time);
      clockColor = VoltageColour.getColourForVoltage(Colours.getInstance(), voltage);
    }
    else
    {
      clockColor = Colours.getInstance().getDisconnectedTrace();
    }
    graphics.setColor(clockColor);

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

    int x1 = viewport.transformGridToScreenSpaceX(position.x - xOffset + xi);
    int x2 = viewport.transformGridToScreenSpaceX(position.x - xOffset + xi);
    int y1 = viewport.transformGridToScreenSpaceY(position.y + 0.0f + yi);
    int y2 = viewport.transformGridToScreenSpaceY(position.y - yOffset + yi);
    graphics.drawLine(x1, y1, x2, y2);

    int x3 = viewport.transformGridToScreenSpaceX(position.x + 0.0f + xi);
    int y3 = viewport.transformGridToScreenSpaceY(position.y - yOffset + yi);
    graphics.drawLine(x2, y2, x3, y3);

    int x4 = viewport.transformGridToScreenSpaceX(position.x + 0.0f + xi);
    int y4 = viewport.transformGridToScreenSpaceY(position.y + yOffset + yi);
    graphics.drawLine(x3, y3, x4, y4);

    int x5 = viewport.transformGridToScreenSpaceX(position.x + xOffset + xi);
    int y5 = viewport.transformGridToScreenSpaceY(position.y + yOffset + yi);
    graphics.drawLine(x4, y4, x5, y5);

    int x6 = viewport.transformGridToScreenSpaceX(position.x + xOffset + xi);
    int y6 = viewport.transformGridToScreenSpaceY(position.y + 0.0f + yi);
    graphics.drawLine(x5, y5, x6, y6);
  }

  public ClockData save(boolean selected)
  {
    return new ClockData(position,
                         rotation,
                         properties.name,
                         properties.family.getFamily(),
                         properties.frequency_Hz,
                         saveEvents(),
                         savePorts(),
                         selected,
                         saveState(),
                         properties.inverseOut,
                         properties.explicitPowerPorts);
  }

  @Override
  protected ClockOscillatorState saveState()
  {
    return new ClockOscillatorState(integratedCircuit.getState().getState());
  }

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }

  @Override
  public void clampProperties()
  {
    properties.frequency_Hz = clamp(properties.frequency_Hz, 0, 999 * GHz);
  }
}
