package net.logicim.ui.input.button;

import net.logicim.ui.editor.EditorAction;

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
    action.executeEditorAction();
  }

  public JButton getButton()
  {
    return button;
  }
}

