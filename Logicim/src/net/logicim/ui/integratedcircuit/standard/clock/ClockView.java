package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.data.integratedcircuit.standard.clock.ClockData;
import net.logicim.domain.common.propagation.VoltageConfiguration;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorState;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.*;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class ClockView
    extends IntegratedCircuitView<ClockOscillator>
{
  protected RectangleView rectangle;
  protected float frequency;

  public ClockView(CircuitEditor circuitEditor,
                   Int2D position,
                   Rotation rotation,
                   float frequency)
  {
    super(circuitEditor,
          position,
          rotation);
    this.frequency = frequency;
    create();
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -1));

    rectangle = new RectangleView(this, 2, 2, true, true);

    finaliseView();
  }

  @Override
  protected ClockOscillator createIntegratedCircuit()
  {
    return new ClockOscillator(circuitEditor.getCircuit(), "", new ClockOscillatorPins(new VoltageConfiguration("",
                                                                                                                0.8f,
                                                                                                                2.0f,
                                                                                                                0.0f,
                                                                                                                3.3f,
                                                                                                                nanosecondsToTime(2.0f),
                                                                                                                nanosecondsToTime(2.0f))), frequency);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport, long time)
  {
    if (rectangle != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      rectangle.paint(graphics, viewport);

      paintClockWaveform(graphics, viewport);

      super.paint(graphics, viewport, time);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  private void paintClockWaveform(Graphics2D graphics, Viewport viewport)
  {
    ClockOscillatorState state = integratedCircuit.getState();
    Color clockColor;
    if (state != null)
    {
      float voltage = integratedCircuit.getInternalVoltage();
      clockColor = VoltageColour.getColorForVoltage(viewport.getColours(), voltage);
    }
    else
    {
      clockColor = viewport.getColours().getDisconnectedTrace();
    }
    graphics.setColor(clockColor);

    float xOffset = 0.5f;
    float yOffset = 0.33f;

    int x1 = viewport.transformGridToScreenSpaceX(position.x - xOffset);
    int x2 = viewport.transformGridToScreenSpaceX(position.x - xOffset);
    int y1 = viewport.transformGridToScreenSpaceY(position.y + 0.0f);
    int y2 = viewport.transformGridToScreenSpaceY(position.y - yOffset);
    graphics.drawLine(x1, y1, x2, y2);

    int x3 = viewport.transformGridToScreenSpaceX(position.x + 0.0f);
    int y3 = viewport.transformGridToScreenSpaceY(position.y - yOffset);
    graphics.drawLine(x2, y2, x3, y3);

    int x4 = viewport.transformGridToScreenSpaceX(position.x + 0.0f);
    int y4 = viewport.transformGridToScreenSpaceY(position.y + yOffset);
    graphics.drawLine(x3, y3, x4, y4);

    int x5 = viewport.transformGridToScreenSpaceX(position.x + xOffset);
    int y5 = viewport.transformGridToScreenSpaceY(position.y + yOffset);
    graphics.drawLine(x4, y4, x5, y5);

    int x6 = viewport.transformGridToScreenSpaceX(position.x + xOffset);
    int y6 = viewport.transformGridToScreenSpaceY(position.y + 0.0f);
    graphics.drawLine(x5, y5, x6, y6);
  }

  public ClockData save()
  {
    return new ClockData(toInt2DData(position),
                         toRotationData(rotation),
                         frequency,
                         integratedCircuit.getState().getState(),
                         saveEvents(),
                         savePorts());
  }
}

