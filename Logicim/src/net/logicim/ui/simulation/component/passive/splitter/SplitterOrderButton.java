package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.components.button.Button;

import java.awt.event.ActionEvent;

public class SplitterOrderButton
    extends Button
{
  private final SplitterPropertiesPanel splitterPropertiesPanel;

  public SplitterOrderButton(SplitterPropertiesPanel splitterPropertiesPanel, String text)
  {
    super(text);
    this.splitterPropertiesPanel = splitterPropertiesPanel;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {
    splitterPropertiesPanel.orderFanOut(getText());
  }
}
