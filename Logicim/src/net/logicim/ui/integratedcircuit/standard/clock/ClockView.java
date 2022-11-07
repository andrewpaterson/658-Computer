package net.logicim.ui.integratedcircuit.standard.clock;

import net.logicim.common.type.Int2D;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.*;
import net.logicim.ui.shape.rectangle.RectangleView;

import java.awt.*;

public class ClockView
    extends IntegratedCircuitView<ClockOscillator>
{
  protected RectangleView rectangle;

  public ClockView(CircuitEditor circuitEditor, Int2D position, Rotation rotation, float frequency)
  {
    super(circuitEditor,
          new ClockOscillator(circuitEditor.getCircuit(), "", new ClockOscillatorPins(), frequency),
          position,
          rotation);
    new PortView(this, this.integratedCircuit.getPort("Output"), new Int2D(0, -1));

    rectangle = new RectangleView(this, 2, 2, true, true);

    finaliseView();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if (rectangle != null)
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();

      rectangle.paint(graphics, viewport);

      paintClockWaveform(graphics, viewport);

      super.paint(graphics, viewport);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  private void paintClockWaveform(Graphics2D graphics, Viewport viewport)
  {
    float voltage = integratedCircuit.getInternalVoltage();
    Color clockColor = VoltageColour.getColorForVoltage(viewport.getColours(), voltage);
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
}

