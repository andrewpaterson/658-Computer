package net.logicim.ui.simulation.selection;

import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.common.wire.TraceView;
import net.logicim.ui.input.keyboard.KeyboardButtons;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Selection
{
  protected SelectionRectangle selectionRectangle;

  protected Set<View> previousSelection;
  protected List<View> selection;

  public Selection()
  {
    this.selectionRectangle = null;
    this.previousSelection = null;
    this.selection = new ArrayList<>();
  }

  public List<View> getSelection()
  {
    return selection;
  }

  public void setSelection(List<View> selection)
  {
    this.selection = selection;
  }

  public static SelectionMode calculateSelectionMode(KeyboardButtons keyboardButtons)
  {
    boolean altDown = keyboardButtons.isAltDown();
    boolean shiftDown = keyboardButtons.isShiftDown();
    if (altDown && shiftDown)
    {
      return SelectionMode.SUBTRACT;
    }
    else if (shiftDown)
    {
      return SelectionMode.ADD;
    }
    else
    {
      return SelectionMode.REPLACE;
    }
  }

  public void clearSelection()
  {
    if (!selection.isEmpty())
    {
      selection = new ArrayList<>();
    }
  }

  public boolean isSelectionEmpty()
  {
    return selection.isEmpty();
  }

  public void startSelection(Viewport viewport, int x, int y)
  {
    previousSelection = new HashSet<>(selection);
    selectionRectangle = new SelectionRectangle();
    selectionRectangle.start(viewport, x, y);
  }

  public SelectionRectangle getSelectionRectangle()
  {
    return selectionRectangle;
  }

  public boolean hasSelectionChanged()
  {
    Set<View> selection = new HashSet<>(this.selection);
    if (selection.isEmpty() && previousSelection.isEmpty())
    {
      return false;
    }
    if (selection.size() != previousSelection.size())
    {
      return true;
    }

    for (View view : selection)
    {
      if (!previousSelection.contains(view))
      {
        return true;
      }
    }

    return false;
  }

  public void drag(Viewport viewport, int x, int y)
  {
    selectionRectangle.drag(viewport, x, y);
  }

  public void clearRectangle()
  {
    selectionRectangle = null;
  }

  public boolean hasRectangle()
  {
    return selectionRectangle != null;
  }

  public void paint(Graphics2D graphics, Viewport viewport, SelectionMode selectionMode)
  {
    if (hasRectangle())
    {
      selectionRectangle.paint(graphics, viewport, selectionMode);
    }

    for (View view : selection)
    {
      view.paintSelected(graphics, viewport);
    }
  }

  public void add(View view)
  {
    selection.add(view);
  }

  public void replaceSelection(View newView, View oldView)
  {
    int i = selection.indexOf(oldView);
    if (i != -1)
    {
      selection.set(i, newView);
    }
  }

  public Int2D getSelectionCenter()
  {
    if (isSelectionEmpty())
    {
      return null;
    }
    else
    {
      Int2D position = new Int2D();
      int count = 0;
      for (View view : selection)
      {
        if (view instanceof StaticView)
        {
          StaticView<?> staticView = (StaticView<?>) view;
          position.add(staticView.getPosition());
          count++;
        }
        else if (view instanceof TraceView)
        {
          TraceView traceView = (TraceView) view;
          position.add(traceView.getStartPosition());
          position.add(traceView.getEndPosition());
          count += 2;
        }
      }
      position.set(Math.round((float) position.x / count), Math.round((float) position.y / count));
      return position;
    }
  }

  public boolean isSelecting()
  {
    return hasRectangle() || !isSelectionEmpty();
  }
}

