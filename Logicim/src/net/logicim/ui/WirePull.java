package net.logicim.ui;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class WirePull
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
    this.firstPosition = null;
    resetStart();
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

  public Int2D getFirstPosition()
  {
    if (firstPosition == null)
    {
      firstPosition = new Int2D();
    }
    return firstPosition;
  }

  public Int2D getMiddlePosition()
  {
    return middlePosition;
  }

  public Int2D getSecondPosition()
  {
    return secondPosition;
  }

  @SuppressWarnings("ConstantConditions")
  public void update(int mouseX, int mouseY)
  {
    if (firstPosition != null)
    {
      if (firstPosition.equals(mouseX, mouseY))
      {
        resetStart();
      }
      else if (firstPosition.x == mouseX)
      {
        if (firstPosition.y < mouseY)
        {
          startOneDirection(mouseY, Rotation.North, firstPosition.y);
        }
        else if (firstPosition.y > mouseY)
        {
          startOneDirection(firstPosition.y, Rotation.South, mouseY);
        }
      }
      else if (firstPosition.y == mouseY)
      {
        if (firstPosition.x < mouseX)
        {
          startOneDirection(mouseX, Rotation.East, firstPosition.x);
        }
        else if (firstPosition.x > mouseX)
        {
          startOneDirection(firstPosition.x, Rotation.West, mouseX);
        }
      }
      else
      {
        if (firstDirection != null)
        {
          if (firstDirection == Rotation.North)
          {
            firstLength = mouseY - firstPosition.y;
          }
          else if (firstDirection == Rotation.East)
          {
            firstLength = mouseX - firstPosition.x;
          }
          else if (firstDirection == Rotation.South)
          {
            firstLength = firstPosition.y - mouseY;
          }
          else if (firstDirection == Rotation.West)
          {
            firstLength = firstPosition.x - mouseX;
          }

          middlePosition.set(0, firstLength);
          firstDirection.transform(middlePosition);
          middlePosition.add(firstPosition);

          if (middlePosition.equals(mouseX, mouseY))
          {
            resetSecond();
          }
          else if (middlePosition.x == mouseX)
          {
            if (middlePosition.y < mouseY)
            {
              secondDirection(mouseY, Rotation.North, middlePosition.y);
            }
            else if (middlePosition.y > mouseY)
            {
              secondDirection(middlePosition.y, Rotation.South, mouseY);
            }
          }
          else if (middlePosition.y == mouseY)
          {
            if (middlePosition.x < mouseX)
            {
              secondDirection(mouseX, Rotation.East, middlePosition.x);
            }
            else if (middlePosition.x > mouseX)
            {
              secondDirection(middlePosition.x, Rotation.West, mouseX);
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

    graphics.setStroke(viewport.getStroke());
    graphics.setColor(viewport.getColours().getDisconnectedTrace());
    graphics.drawLine(x1, y1, x2, y2);
  }

  public boolean isEmpty()
  {
    return middlePosition == null;
  }
}

