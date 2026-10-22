package net.logicim.ui.placement;

import net.common.type.Float2D;
import net.logicim.ui.Edit;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.selection.SelectionEdit;

import java.awt.*;
import java.util.HashSet;
import java.util.List;

public class StartEditInComponent
    extends StatefulEdit
{
  protected KeyboardButtons keyboardButtons;

  public StartEditInComponent(KeyboardButtons keyboardButtons)
  {
    this.keyboardButtons = keyboardButtons;
  }

  @Override
  public void start(float x, float y, Edit edit)
  {
  }

  @Override
  public StatefulEdit move(float x, float y, Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    return new MoveComponents(circuitEditor.getCurrentSelection().getSelection(), false);
  }

  @Override
  public StatefulEdit rotate(boolean right, Edit edit)
  {
//    moveComponents(editAction.getRightRotations(), editAction.getStart(), editAction.getDiff());
    return this;
  }

  @Override
  public void done(float x, float y, Edit edit)
  {
    CircuitEditor circuitEditor = edit.getEditor();
    List<View> previousSelection = circuitEditor.getCurrentSelection().getSelection();
    boolean hasSelectionChanged = SelectionEdit.calculateSelection(circuitEditor, new Float2D(x, y), new Float2D(x, y), keyboardButtons, new HashSet<>(previousSelection));
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
  }

  @Override
  public boolean canTransformComponents()
  {
    return true;
  }
}

