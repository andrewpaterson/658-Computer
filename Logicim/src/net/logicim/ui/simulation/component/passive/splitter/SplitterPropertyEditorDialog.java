package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.property.PropertyEditorDialog;

import javax.swing.*;
import java.awt.*;

public class SplitterPropertyEditorDialog
    extends PropertyEditorDialog
{
  protected SplitterView componentView;
  protected int currentBitWidth;
  private SplitterProperties componentViewProperties;

  public SplitterPropertyEditorDialog(Frame owner,
                                      Logicim editor,
                                      SplitterView componentView)
  {
    super(owner,
          componentView.getType() + " Properties",
          new Dimension(DEFAULT_PROPERTY_DIALOG_WIDTH, 460),
          editor,
          (StaticView) componentView);
    this.componentView = componentView;
    currentBitWidth = this.componentView.getProperties().bitWidth;
    componentViewProperties = this.componentView.getProperties();
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return new SplitterPropertiesPanel(this, componentViewProperties, componentView.getRotation());
  }

  @Override
  protected ComponentProperties updateProperties()
  {
    boolean propertyChanged = updateRotation(getSplitterPropertiesPanel());

    SplitterProperties newProperties = getSplitterPropertiesPanel().createProperties(componentView.getProperties());
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

  private SplitterPropertiesPanel getSplitterPropertiesPanel()
  {
    return (SplitterPropertiesPanel) getPropertiesPanel();
  }

  public void focusLost()
  {
    componentViewProperties = getSplitterPropertiesPanel().valueMaybeChanged();
    if (componentViewProperties.bitWidth != currentBitWidth)
    {
      currentBitWidth = componentViewProperties.bitWidth;
      getContentPane().removeAll();
      build();
    }
  }

  @Override
  public void okay()
  {
    focusLost();
    super.okay();
  }
}

