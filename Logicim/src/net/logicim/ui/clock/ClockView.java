package net.logicim.ui.clock;

import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillator;
import net.logicim.domain.integratedcircuit.standard.clock.ClockOscillatorPins;
import net.logicim.ui.CircuitEditor;
import net.logicim.ui.common.Position;
import net.logicim.ui.common.View;
import net.logicim.ui.common.Viewport;

import java.awt.*;

import static net.logicim.domain.common.Units.Hz;

public class ClockView
    extends View
{
  protected ClockOscillator clock;

  public ClockView(CircuitEditor circuitEditor, Position position)
  {
    super(circuitEditor, position);
    this.clock = new ClockOscillator(circuitEditor.getCircuit(), "", new ClockOscillatorPins(), 1 * Hz);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    Stroke stroke = graphics.getStroke();
    Color color = graphics.getColor();

    int left = viewport.transformGridToScreenSpaceX(position.x);
    int top = viewport.transformGridToScreenSpaceY(position.y);
    int width = viewport.transformGridToScreenWidth(2);
    int height = viewport.transformGridToScreenHeight(2);
    graphics.setStroke(new BasicStroke(viewport.getLineWidth()));
    graphics.setColor(Color.WHITE);
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Color.BLACK);
    graphics.drawRect(left, top, width, height);

    graphics.setStroke(stroke);
    graphics.setColor(color);
  }
}

