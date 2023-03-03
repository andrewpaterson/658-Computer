package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
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
  public void start(float x, float y, EditAction editAction)
  {
  }

  @Override
  public StatefulEdit move(float x, float y, EditAction editAction)
  {
    CircuitEditor circuitEditor = editAction.getCircuitEditor();
    return new MoveComponents(circuitEditor.getCurrentSelection().getSelection(), false);
  }

  @Override
  public StatefulEdit rotate(boolean right, EditAction editAction)
  {
    throw new SimulatorException();
  }

  @Override
  public void done(float x, float y, EditAction editAction)
  {
    CircuitEditor circuitEditor = editAction.getCircuitEditor();
    List<View> previousSelection = circuitEditor.getCurrentSelection().getSelection();
    boolean hasSelectionChanged = SelectionEdit.calculateSelection(circuitEditor, new Float2D(x, y), new Float2D(x, y), keyboardButtons, new HashSet<>(previousSelection));
    if (hasSelectionChanged)
    {
      editAction.pushUndo();
    }
  }

  @Override
  public void discard(EditAction editAction)
  {
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }
}

