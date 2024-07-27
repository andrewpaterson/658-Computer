package net.logicim.ui.simulation.selection;

import net.common.type.Int2D;
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
  protected List<View> selection;

  public Selection()
  {
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
    boolean ctrlDown = keyboardButtons.isControlDown();
    boolean shiftDown = keyboardButtons.isShiftDown();
    if (ctrlDown && shiftDown)
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

  public void paint(Graphics2D graphics, Viewport viewport)
  {
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

  public static Int2D getViewsCenter(List<View> views)
  {
    if (!views.isEmpty())
    {
      int count = 0;
      Int2D position = new Int2D();
      for (View view : views)
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
    else
    {
      return null;
    }
  }

  public List<View> calculateSelection(List<View> views, Set<View> previousSelection, SelectionMode selectionMode)
  {
    if (selectionMode == SelectionMode.REPLACE)
    {
      return views;
    }
    else if (selectionMode == SelectionMode.ADD)
    {
      Set<View> newSelection = new HashSet<>(views);
      newSelection.addAll(previousSelection);
      return new ArrayList<>(newSelection);
    }
    else if (selectionMode == SelectionMode.SUBTRACT)
    {
      Set<View> newSelection = new HashSet<>(previousSelection);
      newSelection.removeAll(views);
      return new ArrayList<>(newSelection);
    }
    return null;
  }

  public static boolean hasSelectionChanged(Set<View> selection, Set<View> previousSelection)
  {
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
}

