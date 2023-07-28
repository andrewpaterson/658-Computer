package net.logicim.ui.simulation.selection;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.Edit;
import net.logicim.ui.common.Strokes;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.placement.StatefulEdit;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.util.RectangleUtil;

import java.awt.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionEdit
    extends StatefulEdit
{
  protected Set<View> previousSelection;

  protected Float2D start;
  protected Float2D end;

  private KeyboardButtons keyboardButtons;

  public SelectionEdit(KeyboardButtons keyboardButtons)
  {
    this.keyboardButtons = keyboardButtons;

    this.start = null;
    this.end = null;

    this.previousSelection = null;
  }

  public void paint(Graphics2D graphics,
                    Viewport viewport,
                    SelectionMode selectionMode)
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

  public void startSelection(float x, float y, List<View> selection)
  {
    previousSelection = new HashSet<>(selection);
    start(x, y);
  }

  public boolean doneSelection(Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    boolean hasSelectionChanged = calculateSelection(circuitEditor, start, end, keyboardButtons, previousSelection);
    previousSelection = null;
    return hasSelectionChanged;
  }

  public static boolean calculateSelection(CircuitEditor circuitEditor, Float2D startPosition, Float2D endPosition, KeyboardButtons keyboardButtons, Set<View> previousSelection)
  {
    List<View> views = circuitEditor.getSelectionFromRectangle(startPosition, endPosition);
    SelectionMode selectionMode = Selection.calculateSelectionMode(keyboardButtons);
    List<View> selection = circuitEditor.getCurrentSelection().calculateSelection(views, previousSelection, selectionMode);
    circuitEditor.getCurrentSelection().setSelection(selection);
    return Selection.hasSelectionChanged(new HashSet<>(selection), previousSelection);
  }

  public Float2D getStart()
  {
    return start;
  }

  public Float2D getEnd()
  {
    return end;
  }

  @Override
  public void start(float x, float y, Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    startSelection(x, y, circuitEditor.getCurrentSelection().getSelection());
  }

  @Override
  public StatefulEdit move(float x, float y, Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    drag(x, y);
    List<View> views = circuitEditor.getSelectionFromRectangle(start, end);
    SelectionMode selectionMode = Selection.calculateSelectionMode(keyboardButtons);
    List<View> selection = circuitEditor.getCurrentSelection().calculateSelection(views, previousSelection, selectionMode);
    circuitEditor.getCurrentSelection().setSelection(selection);
    return this;
  }

  @Override
  public StatefulEdit rotate(boolean right, Edit edit)
  {
    return this;
  }

  @Override
  public void done(float x, float y, Edit edit)
  {
    boolean hasSelectionChanged = doneSelection(edit);
    if (hasSelectionChanged)
    {
      edit.pushUndo();
    }
  }

  @Override
  public void discard(Edit edit)
  {
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
    SelectionMode mode = Selection.calculateSelectionMode(keyboardButtons);
    paint(graphics, viewport, mode);
  }

  @Override
  public boolean canTransformComponents()
  {
    return false;
  }
}

