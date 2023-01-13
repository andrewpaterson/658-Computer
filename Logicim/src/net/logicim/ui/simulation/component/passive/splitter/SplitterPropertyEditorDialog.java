package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.property.PropertyEditorDialog;

import javax.swing.*;
import java.awt.*;

public class SplitterPropertyEditorDialog
    extends PropertyEditorDialog
{
  protected SplitterPropertiesPanel splitterPropertiesPanel;

  public SplitterPropertyEditorDialog(Frame owner,
                                      SimulatorEditor editor,
                                      SplitterView componentView)
  {
    super(owner, componentView.getType() + " Properties", new Dimension(360, 320), editor, componentView);
    splitterPropertiesPanel = new SplitterPropertiesPanel(componentView.getProperties());
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return splitterPropertiesPanel;
  }

  @Override
  protected boolean updateProperties()
  {
    return false;
  }
}

