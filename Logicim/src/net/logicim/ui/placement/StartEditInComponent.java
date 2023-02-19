package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.common.type.Float2D;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.selection.Selection;

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
  public void start(float x, float y, SimulatorEdit simulatorEdit)
  {
  }

  @Override
  public StatefulEdit move(float x, float y, SimulatorEdit simulatorEdit)
  {
    CircuitEditor circuitEditor = simulatorEdit.getCircuitEditor();
    return new MoveComponents(circuitEditor.getSelection().getSelection(), false);
  }

  @Override
  public StatefulEdit rotate(boolean right, SimulatorEdit simulatorEdit)
  {
    throw new SimulatorException();
  }

  @Override
  public void done(float x, float y, SimulatorEdit simulatorEdit)
  {
    CircuitEditor circuitEditor = simulatorEdit.getCircuitEditor();
    List<View> previousSelection = circuitEditor.getSelection().getSelection();
    List<View> selection = circuitEditor.getSelectionFromRectangle(new Float2D(x, y), new Float2D(x, y));
    boolean hasSelectionChanged = Selection.hasSelectionChanged(new HashSet<>(selection), new HashSet<>(previousSelection));
    if (hasSelectionChanged)
    {
      circuitEditor.getSelection().setSelection(selection);
      simulatorEdit.pushUndo();
    }
  }

  @Override
  public void discard(SimulatorEdit simulatorEdit)
  {
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }
}

