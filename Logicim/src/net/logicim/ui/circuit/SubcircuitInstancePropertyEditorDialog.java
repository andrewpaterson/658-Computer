package net.logicim.ui.circuit;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.subcircuit.SubcircuitInstanceProperties;
import net.logicim.ui.property.PropertyEditorDialog;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SubcircuitInstancePropertyEditorDialog
    extends PropertyEditorDialog
{
  protected SubcircuitInstanceView componentView;
  protected SubcircuitInstanceProperties componentViewProperties;

  public SubcircuitInstancePropertyEditorDialog(Frame owner,
                                                Logicim editor,
                                                SubcircuitInstanceView componentView)
  {
    super(owner, componentView.getType() + " Properties", new Dimension(392, 260), editor, componentView);
    this.componentView = componentView;
    componentViewProperties = this.componentView.getProperties();
  }

  @Override
  protected JPanel createEditorPanel()
  {
    List<String> subcircuitTypeNames = editor.getAllowedSubcircuitTypeNamesForSubcircuitInstance(componentView);
    return new SubcircuitInstancePropertiesPanel(this,
                                                 componentViewProperties,
                                                 subcircuitTypeNames,
                                                 componentView.getRotation());
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    boolean propertyChanged = updateRotation(getSubcircuitInstancePropertiesPanel());

    SubcircuitInstanceProperties newProperties = getSubcircuitInstancePropertiesPanel().createProperties();
    if (componentView.getProperties().equals(newProperties))
    {
      if (propertyChanged)
      {
        return newProperties;
      }
      return null;
    }
    else
    {
      return newProperties;
    }
  }

  private SubcircuitInstancePropertiesPanel getSubcircuitInstancePropertiesPanel()
  {
    return (SubcircuitInstancePropertiesPanel) getPropertiesPanel();
  }

  public void focusLost()
  {
  }

  @Override
  public void okay()
  {
    focusLost();
    super.okay();
  }
}

