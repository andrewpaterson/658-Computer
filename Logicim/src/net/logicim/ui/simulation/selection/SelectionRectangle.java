package net.logicim.ui.simulation.selection;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Strokes;
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

  public void paint(Graphics2D graphics, Viewport viewport, SelectionMode selectionMode)
  {
    if ((start != null) && (end != null))
    {
      Stroke stroke = graphics.getStroke();
      Color color = graphics.getColor();
      Int2D start = new Int2D(viewport.transformGridToScreenSpace(this.start));
      Int2D end = new Int2D(viewport.transformGridToScreenSpace(this.end));
      float lineWidth = viewport.getDefaultLineWidth();
      Stroke dashed = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL,
                                      0, new float[]{9}, 0);
      graphics.setStroke(dashed);
      graphics.setColor(Color.BLACK);

      RectangleUtil.drawRect(graphics, start.x, start.y, end.x, end.y);

      Stroke solid = Strokes.getInstance().getSolidStroke(1);
      graphics.setStroke(solid);

      int x = end.x + 2;
      if (start.x < end.x)
      {
        graphics.drawRect(x, end.y - 15, 11, 11);
        graphics.fillOval(x + 2, end.y - 13, 7, 7);
        x += 15;
      }
      else if (start.x > end.x)
      {
        graphics.drawRect(x, end.y - 15, 11, 11);
        graphics.fillOval(x + 2 + 5, end.y - 13, 7, 7);
        x += 17;
      }

      if (selectionMode == SelectionMode.ADD)
      {
        graphics.drawLine(x, end.y - 10, x + 10, end.y - 10);
        graphics.drawLine(x + 5, end.y - 15, x + 5, end.y - 5);
      }
      else if (selectionMode == SelectionMode.SUBTRACT)
      {
        graphics.drawLine(x, end.y - 10, x + 10, end.y - 10);
      }

      graphics.setStroke(stroke);
      graphics.setColor(color);

    }
  }

  public void start(float x, float y)
  {
    start = new Float2D(x, y);
    end = new Float2D(start);
  }

  public void drag(float x, float y)
  {
    if (end != null)
    {
      end.set(x, y);
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

