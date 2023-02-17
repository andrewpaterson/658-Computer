package net.logicim.ui.placement;

import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;

public class InSelectedComponent
    extends StatefulMove
{
  protected KeyboardButtons keyboardButtons;

  public InSelectedComponent(KeyboardButtons keyboardButtons)
  {
    this.keyboardButtons = keyboardButtons;
  }

  @Override
  public void start(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
  }

  @Override
  public StatefulMove move(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
    List<View> selection = circuitEditor.getSelection().getSelection();
    return new MoveComponents(selection);
  }

  @Override
  public void done(float x, float y, CircuitEditor circuitEditor, StatefulEdit statefulEdit)
  {
    circuitEditor.startSelection(x, y, keyboardButtons);
    circuitEditor.doneSelection(x, y, keyboardButtons);
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }
}

