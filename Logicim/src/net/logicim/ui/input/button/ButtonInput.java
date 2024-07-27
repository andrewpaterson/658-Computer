package net.logicim.ui.input.button;

import net.logicim.ui.editor.EditorAction;
import net.logicim.ui.input.event.ButtonPressedEvent;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonInput
    implements ActionListener
{
  protected EditorAction action;

  protected JButton button;

  public ButtonInput(EditorAction action, JButton button)
  {
    this.action = action;
    this.button = button;
    this.button.addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    action.getEditor().addEditorEvent(new ButtonPressedEvent(action));
  }

  public JButton getButton()
  {
    return button;
  }

  public void enable()
  {
    boolean enabled = action.isAvailable();
    if (button.isEnabled() && !enabled)
    {
      button.setEnabled(false);
    }
    else if (!button.isEnabled() && enabled)
    {
      button.setEnabled(true);
    }
  }
}

