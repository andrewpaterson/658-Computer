package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;

public class InSelectedComponent
    extends StatefulMove
{
  protected KeyboardButtons keyboardButtons;

  public InSelectedComponent(KeyboardButtons keyboardButtons)
  {
    this.keyboardButtons = keyboardButtons;
  }

  @Override
  public void start(float x, float y, StatefulEdit statefulEdit)
  {
  }

  @Override
  public StatefulMove move(float x, float y, StatefulEdit statefulEdit)
  {
    CircuitEditor circuitEditor = statefulEdit.getCircuitEditor();
    return new MoveComponents(circuitEditor.getSelection().getSelection(), false);
  }

  @Override
  public StatefulMove rotate(boolean right, StatefulEdit statefulEdit)
  {
    throw new SimulatorException();
  }

  @Override
  public void done(float x, float y, StatefulEdit statefulEdit)
  {
    CircuitEditor circuitEditor = statefulEdit.getCircuitEditor();
    circuitEditor.startSelection(x, y, keyboardButtons);
    circuitEditor.doneSelection(x, y, keyboardButtons);
  }

  @Override
  public void discard(StatefulEdit statefulEdit)
  {
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }
}

