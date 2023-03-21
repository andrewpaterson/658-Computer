package net.logicim.ui.circuit;

import net.logicim.ui.Logicim;
import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.data.subciruit.SubcircuitInstanceProperties;
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
    super(owner, componentView.getType() + " Properties", new Dimension(DEFAULT_PROPERTY_DIALOG_WIDTH, 260), editor, (StaticView) componentView);
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

    SubcircuitInstanceProperties newProperties = getSubcircuitInstancePropertiesPanel().createProperties(componentView.getProperties());
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

