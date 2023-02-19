package net.logicim.ui.placement;

import net.logicim.common.geometry.Line;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Colours;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class WirePull
    extends StatefulEdit
{
  protected Int2D firstPosition;
  protected Rotation firstDirection;
  protected int firstLength;

  protected Rotation secondDirection;
  protected int secondLength;

  protected Int2D middlePosition;
  protected Int2D secondPosition;
  protected Int2D secondEnd;

  public WirePull()
  {
    resetStart();
  }

  @Override
  public void start(float x, float y, SimulatorEdit simulatorEdit)
  {
    firstPosition = new Int2D(x, y);
  }

  @Override
  public StatefulEdit move(float x, float y, SimulatorEdit simulatorEdit)
  {
    update(Math.round(x), Math.round(y));

    if (!isEmpty())
    {
      simulatorEdit.getCircuitEditor().getSelection().clearSelection();
    }

    return this;
  }

  @Override
  public StatefulEdit rotate(boolean right, SimulatorEdit simulatorEdit)
  {
    return this;
  }

  @Override
  public void done(float x, float y, SimulatorEdit simulatorEdit)
  {
    if (!isEmpty())
    {
      Line firstLine = Line.createLine(firstPosition, middlePosition);
      Line secondLine = Line.createLine(middlePosition, secondPosition);

      simulatorEdit.getCircuitEditor().createTraceViews(Line.lines(firstLine, secondLine));

      simulatorEdit.pushUndo();
    }
  }

  @Override
  public void discard(SimulatorEdit simulatorEdit)
  {
  }

  private void resetStart()
  {
    firstDirection = null;
    firstLength = 0;
    secondDirection = null;
    secondLength = 0;
    middlePosition = null;
    secondPosition = null;
    secondEnd = null;
  }

  private void resetSecond()
  {
    secondDirection = null;
    secondLength = 0;
    secondPosition = null;
    secondEnd = null;
  }

  public void update(int x, int y)
  {
    if (firstPosition != null)
    {
      if (firstPosition.equals(x, y))
      {
        resetStart();
      }
      else if (firstPosition.x == x)
      {
        if (firstPosition.y < y)
        {
          startOneDirection(y, Rotation.North, firstPosition.y);
        }
        else if (firstPosition.y > y)
        {
          startOneDirection(firstPosition.y, Rotation.South, y);
        }
      }
      else if (firstPosition.y == y)
      {
        if (firstPosition.x < x)
        {
          startOneDirection(x, Rotation.East, firstPosition.x);
        }
        else
        {
          startOneDirection(firstPosition.x, Rotation.West, x);
        }
      }
      else
      {
        if (firstDirection != null)
        {
          if (firstDirection == Rotation.North)
          {
            firstLength = y - firstPosition.y;
          }
          else if (firstDirection == Rotation.East)
          {
            firstLength = x - firstPosition.x;
          }
          else if (firstDirection == Rotation.South)
          {
            firstLength = firstPosition.y - y;
          }
          else if (firstDirection == Rotation.West)
          {
            firstLength = firstPosition.x - x;
          }

          middlePosition.set(0, firstLength);
          firstDirection.transform(middlePosition);
          middlePosition.add(firstPosition);

          if (middlePosition.equals(x, y))
          {
            resetSecond();
          }
          else if (middlePosition.x == x)
          {
            if (middlePosition.y < y)
            {
              secondDirection(y, Rotation.North, middlePosition.y);
            }
            else if (middlePosition.y > y)
            {
              secondDirection(middlePosition.y, Rotation.South, y);
            }
          }
          else if (middlePosition.y == y)
          {
            if (middlePosition.x < x)
            {
              secondDirection(x, Rotation.East, middlePosition.x);
            }
            else
            {
              secondDirection(middlePosition.x, Rotation.West, x);
            }
          }
        }
      }
    }
  }

  private void startOneDirection(int start, Rotation rotation, int end)
  {
    firstDirection = rotation;
    firstLength = start - end;
    secondDirection = null;
    secondLength = 0;

    middlePosition = new Int2D(0, firstLength);
    firstDirection.transform(middlePosition);
    middlePosition.add(firstPosition);

    secondPosition = null;
    secondEnd = null;
  }

  private void secondDirection(int start, Rotation rotation, int end)
  {
    secondLength = start - end;
    secondDirection = rotation;
    secondPosition = new Int2D(0, secondLength);
    secondDirection.transform(secondPosition);
    secondPosition.add(middlePosition);
  }

  public void paint(Graphics2D graphics, Viewport viewport)
  {
    if (firstPosition != null)
    {
      if (firstDirection != null && firstLength != 0)
      {
        paintLine(graphics, viewport, firstPosition, middlePosition);

        if ((secondDirection != null) && (secondLength != 0))
        {
          paintLine(graphics, viewport, middlePosition, secondPosition);
        }
      }
    }
  }

  private void paintLine(Graphics2D graphics, Viewport viewport, Int2D startPosition, Int2D endPosition)
  {
    int x1 = viewport.transformGridToScreenSpaceX(startPosition.x);
    int y1 = viewport.transformGridToScreenSpaceY(startPosition.y);

    int x2 = viewport.transformGridToScreenSpaceX(endPosition.x);
    int y2 = viewport.transformGridToScreenSpaceY(endPosition.y);

    graphics.setStroke(viewport.getZoomableStroke());
    graphics.setColor(Colours.getInstance().getDisconnectedTrace());
    graphics.drawLine(x1, y1, x2, y2);
  }

  public boolean isEmpty()
  {
    return middlePosition == null;
  }
}

