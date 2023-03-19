package net.logicim.ui.property;

import net.logicim.domain.common.Circuit;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.DEFAULT_WIDTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertyEditorDialog
    extends InputDialog
{
  protected Logicim editor;
  protected StaticView<?> componentView;
  protected PropertiesPanel propertiesPanel;
  protected Circuit circuit;

  public PropertyEditorDialog(Frame owner,
                              String title,
                              Dimension dimension,
                              Logicim editor,
                              StaticView<?> componentView)
  {
    super(owner, title, true, dimension);

    this.editor = editor;
    this.circuit = this.editor.getCircuitEditor().getCircuit();
    this.componentView = componentView;
  }

  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    JPanel editorPanel = createEditorPanel();
    propertiesPanel = (PropertiesPanel) editorPanel;
    contentPane.add(editorPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    JPanel bottomPanel = new JPanel();
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));

    buildButtons(bottomPanel,
                 DEFAULT_WIDTH,
                 new ActionButton("Okay", this),
                 new CancelButton("Cancel", this));
    bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
  }

  public void okay()
  {
    ComponentProperties newComponentProperties = updateProperties();

    if (newComponentProperties != null)
    {
      editor.addEditorEvent(new PropertyEditEvent(componentView,
                                                  newComponentProperties,
                                                  editor.getCircuitEditor().getCurrentSubcircuitEditor(),
                                                  circuit));
    }

    close();
  }

  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }

  protected boolean updateRotation(RotationEditorHolder rotationEditorHolder)
  {
    boolean propertyChanged = false;
    Rotation rotation = rotationEditorHolder.getRotation();
    if (componentView.getRotation() != rotation)
    {
      componentView.setRotation(rotation);
      propertyChanged = true;
    }
    return propertyChanged;
  }

  protected abstract JPanel createEditorPanel();

  protected abstract ComponentProperties updateProperties();
}

