package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.property.PropertyEditorDialog;

import javax.swing.*;
import java.awt.*;

public class SplitterPropertyEditorDialog
    extends PropertyEditorDialog
{
  public SplitterPropertyEditorDialog(Frame owner,
                                      SimulatorEditor editor,
                                      ComponentView<?> componentView)
  {
    super(owner, componentView.getType() + " Properties", new Dimension(360, 320), editor, componentView);
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return new JPanel();
  }

  @Override
  protected boolean updateProperties()
  {
    return false;
  }
}

