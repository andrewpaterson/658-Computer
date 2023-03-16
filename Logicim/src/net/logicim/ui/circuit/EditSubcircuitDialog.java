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
  protected SubcircuitView subcircuitView;

  public EditSubcircuitDialog(Frame owner,
                              SimulatorEditor editor,
                              SubcircuitView subcircuitView)
  {
    super(owner, "Create subcircuit", new Dimension(392, 260), editor, null);
    this.subcircuitView = subcircuitView;
  }

  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }

  protected JPanel createEditorPanel()
  {
    return new SubcircuitPropertiesPanel(subcircuitView.getTypeName());
  }

  private SubcircuitPropertiesPanel getSubcircuitPropertiesPanel()
  {
    return (SubcircuitPropertiesPanel) getPropertiesPanel();
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    String oldTypeName = subcircuitView.getTypeName();

    String newTypeName = getSubcircuitPropertiesPanel().getSubcircuitName();
    editor.getCircuitEditor().getSubcircuitNameError(newTypeName);
    subcircuitView.setTypeName(newTypeName);

    editor.renameSubcircuit(oldTypeName, newTypeName);
    return null;
  }

  @Override
  public void okay()
  {
    updateProperties();
    close();
  }
}

