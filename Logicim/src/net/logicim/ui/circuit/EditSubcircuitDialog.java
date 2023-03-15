package net.logicim.ui.circuit;

import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.property.PropertiesPanel;
import net.logicim.ui.property.PropertyEditorDialog;

import javax.swing.*;
import java.awt.*;

public class EditSubcircuitDialog
    extends PropertyEditorDialog
{
  public EditSubcircuitDialog(Frame owner,
                              SimulatorEditor editor)
  {
    super(owner, "Create subcircuit", new Dimension(392, 260), editor, null);
  }

  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }

  protected JPanel createEditorPanel()
  {
    return new SubcircuitPropertiesPanel(getSubcircuitView().getTypeName());
  }

  private SubcircuitPropertiesPanel getSubcircuitPropertiesPanel()
  {
    return (SubcircuitPropertiesPanel) getPropertiesPanel();
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    String typeName = getSubcircuitPropertiesPanel().getSubcircuitName();
    editor.getCircuitEditor().getSubcircuitNameError(typeName);
    getSubcircuitView().setTypeName(typeName);
    return null;
  }

  private SubcircuitView getSubcircuitView()
  {
    return editor.getCircuitEditor().getCurrentSubcircuitView();
  }

  @Override
  public void okay()
  {
    updateProperties();
    close();
  }
}

