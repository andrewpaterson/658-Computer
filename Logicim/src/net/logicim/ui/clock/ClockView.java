package net.logicim.ui.clock;

import net.logicim.domain.common.Circuit;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.ui.common.Position;
import net.logicim.ui.common.View;
import net.logicim.ui.common.Viewport;

import java.awt.*;

import static net.logicim.domain.common.Units.Hz;

public class ClockView
    extends View
{
  protected ClockOscillator clock;

  public ClockView(Circuit circuit, Position position)
  {
    super(position);
    this.clock = new ClockOscillator(circuit, "", new ClockOscillatorPins(), 1 * Hz);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    int left = viewport.transformX(anchor.x);
    int top = viewport.transformY(anchor.y);
    int width = viewport.transformWidth(2);
    int height = viewport.transformHeight(2);
    graphics.setStroke(new BasicStroke(2));
    graphics.setColor(Color.BLACK);
    graphics.drawRect(left, top, width, height);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }
}

