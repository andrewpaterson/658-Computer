package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.ui.SimulatorEditor;
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
                                      SimulatorEditor editor,
                                      SplitterView componentView)
  {
    super(owner, componentView.getType() + " Properties", new Dimension(392, 460), editor, componentView);
    this.componentView = componentView;
    currentBitWidth = this.componentView.getProperties().bitWidth;
    componentViewProperties = this.componentView.getProperties();
  }

  @Override
  protected JPanel createEditorPanel()
  {
    return new SplitterPropertiesPanel(this, componentViewProperties);
  }

  @Override
  protected boolean updateProperties()
  {
    SplitterProperties newProperties = createProperties();
    if (componentView.getProperties().equals(newProperties))
    {
      return false;
    }
    else
    {
      componentView.setProperties(newProperties);
      return true;
    }
  }

  private SplitterProperties createProperties()
  {
    return getSplitterPropertiesPanel().createProperties();
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

