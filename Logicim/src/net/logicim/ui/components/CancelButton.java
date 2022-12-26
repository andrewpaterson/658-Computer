package net.logicim.ui.components;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class CancelButton
    extends Button
{
  protected JDialog dialog;

  public CancelButton(String text, JDialog dialog)
  {
    super(text);
    this.dialog = dialog;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    dialog.setVisible(false);
    dialog.dispose();
  }
}

