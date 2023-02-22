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

public class StartEditInPort
    extends StatefulEdit
{
  protected KeyboardButtons keyboardButtons;

  public StartEditInPort(KeyboardButtons keyboardButtons)
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
    return new WirePull();
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
    java.util.List<View> previousSelection = circuitEditor.getSelection().getSelection();
    boolean hasSelectionChanged = SelectionEdit.calculateSelection(circuitEditor, new Float2D(x, y), new Float2D(x, y), keyboardButtons, new HashSet<>(previousSelection));
    if (hasSelectionChanged)
    {
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

