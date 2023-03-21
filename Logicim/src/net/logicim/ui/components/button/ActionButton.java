package net.logicim.ui.components.button;

import java.awt.event.ActionEvent;

public class ActionButton
    extends Button
{
  protected ButtonAction action;

  public ActionButton(String text, ButtonAction action)
  {
    super(text);
    this.action = action;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    action.executeButtonAction(this);
  }
}

