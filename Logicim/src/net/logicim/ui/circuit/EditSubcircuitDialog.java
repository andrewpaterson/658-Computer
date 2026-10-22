package net.logicim.ui.circuit;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.Logicim;
import net.logicim.ui.property.PropertiesPanel;
import net.logicim.ui.property.PropertyEditorDialog;

import javax.swing.*;
import java.awt.*;

public class EditSubcircuitDialog
    extends PropertyEditorDialog
{
  protected SubcircuitView subcircuitView;

  public EditSubcircuitDialog(Frame owner,
                              Logicim editor,
                              SubcircuitView subcircuitView)
  {
    super(owner, "Edit Subcircuit", new Dimension(DEFAULT_WIDTH, 320), editor, null);
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

