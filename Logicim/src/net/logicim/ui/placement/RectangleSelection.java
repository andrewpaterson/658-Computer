package net.logicim.ui.placement;

import net.logicim.common.SimulatorException;
import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.View;
import net.logicim.ui.input.keyboard.KeyboardButtons;
import net.logicim.ui.simulation.CircuitEditor;

import java.awt.*;
import java.util.List;

//This should be SelectionRectangle but Selection classes are proper fucked.
public class RectangleSelection
    extends StatefulMove
{
  private KeyboardButtons keyboardButtons;

  public RectangleSelection(KeyboardButtons keyboardButtons)
  {
    this.keyboardButtons = keyboardButtons;
  }

  @Override
  public void start(float x, float y, StatefulEdit statefulEdit)
  {
    statefulEdit.getCircuitEditor().startSelection(x, y, keyboardButtons);
  }

  @Override
  public StatefulMove move(float x, float y, StatefulEdit statefulEdit)
  {
    CircuitEditor circuitEditor = statefulEdit.getCircuitEditor();
    if (circuitEditor.isSelecting())
    {
      circuitEditor.getSelection().drag(x, y);
      List<View> views = circuitEditor.getSelectionFromRectangle(circuitEditor.getSelection().getSelectionRectangle());
      circuitEditor.getSelection().selectionMoved(keyboardButtons, views);
    }

    return this;
  }

  @Override
  public StatefulMove rotate(boolean right, StatefulEdit statefulEdit)
  {
    return this;
  }

  @Override
  public void done(float x, float y, StatefulEdit statefulEdit)
  {
    boolean hasSelectionChanged = statefulEdit.getCircuitEditor().doneSelection(x, y, keyboardButtons);
    if (hasSelectionChanged)
    {
      statefulEdit.pushUndo();
    }
  }

  @Override
  public void discard(StatefulEdit statefulEdit)
  {
    throw new SimulatorException();
  }

  @Override
  public void paint(Graphics2D graphics, Viewport viewport)
  {
  }
}

