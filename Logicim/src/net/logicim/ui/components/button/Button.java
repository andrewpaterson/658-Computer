package net.logicim.ui.components.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Button
    implements ActionListener
{
  protected String text;

  public Button(String text)
  {
    this.text = text;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
  }

  public String getText()
  {
    return text;
  }
}

