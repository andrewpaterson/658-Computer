package net.logicim.ui.debugdetail;

import net.logicim.domain.passive.subcircuit.SubcircuitSimulation;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class InformationPanel
{
  protected Graphics2D graphics;
  protected Viewport viewport;
  protected int width;
  protected int height;

  public InformationPanel(Graphics2D graphics, Viewport viewport, int width, int height)
  {
    this.graphics = graphics;
    this.viewport = viewport;
    this.width = width;
    this.height = height;
  }

  public void drawDetails(SubcircuitSimulation subcircuitSimulation, int left, int top)
  {
    graphics.setColor(Colours.getInstance().getInfoBackground());
    graphics.fillRect(left, top, width, height);
    graphics.setColor(Colours.getInstance().getInfoBorder());
    graphics.drawRect(left, top, width, height);
    Shape clip = graphics.getClip();

    graphics.setClip(left, top, width, height);
    Font font = viewport.getFont(10, false);
    FontMetrics metrics = graphics.getFontMetrics(font);
    int fontHeight = metrics.getHeight();
    int yOffset = top + 10 + metrics.getAscent();
    int xOffset = left + 10;

    paintDetail(subcircuitSimulation, fontHeight, xOffset, yOffset);

    graphics.setClip(clip);
  }

  protected int drawMultilineString(int fontHeight, int x, int y, String string)
  {
    String[] strings = string.split("\n");
    for (int i = 0; i < strings.length; i++)
    {
      String s = strings[i];
      if (!((i == (strings.length - 1)) && ((s == null) || s.trim().isEmpty())))
      {
        graphics.drawString(s, x, y);
        y += fontHeight;
      }
    }
    return y;
  }

  protected boolean appendComma(StringBuilder builder, boolean first)
  {
    if (first)
    {
      first = false;
    }
    else
    {
      builder.append(", ");
    }
    return first;
  }

  protected abstract void paintDetail(SubcircuitSimulation subcircuitSimulation, int fontHeight, int x, int y);
}
