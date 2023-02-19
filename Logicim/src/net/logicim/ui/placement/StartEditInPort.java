package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;

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
    circuitEditor.startSelection(x, y, keyboardButtons);
    circuitEditor.doneSelection(x, y, keyboardButtons);
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

