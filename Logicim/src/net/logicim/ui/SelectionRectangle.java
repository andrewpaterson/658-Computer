package net.logicim.ui;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.util.RectangleUtil;

import java.awt.*;

public class SelectionRectangle
{
  protected Float2D start;
  protected Float2D end;

  public SelectionRectangle()
  {
    start = null;
    end = null;
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if ((start != null) && (end != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();
      Int2D start = new Int2D(viewport.transformGridToScreenSpace(this.start));
      Int2D end = new Int2D(viewport.transformGridToScreenSpace(this.end));
      Stroke dashed = new BasicStroke(viewport.getDefaultLineWidth(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                      0, new float[]{9}, 0);
      graphics.setStroke(dashed);
      graphics.setColor(Color.BLACK);

      RectangleUtil.drawRect(graphics, start.x, start.y, end.x, end.y);

      graphics.setStroke(stroke);
      graphics.setColor(color);
    }
  }

  public void start(Viewport viewport, int screenX, int screenY)
  {
    start = new Float2D(viewport.transformScreenToGridX((float) screenX),
                        viewport.transformScreenToGridY((float) screenY));
    end = new Float2D(start);
  }

  protected void drag(Viewport viewport, int x, int y)
  {
    if (end != null)
    {
      end.set(viewport.transformScreenToGridX((float) x),
              viewport.transformScreenToGridY((float) y));
    }
  }

  public Float2D getStart()
  {
    return start;
  }

  public Float2D getEnd()
  {
    return end;
  }
}

