package net.logicim.ui.property;

import net.logicim.data.common.properties.ComponentProperties;
import net.logicim.domain.common.Circuit;
import net.logicim.ui.Logicim;
import net.logicim.ui.circuit.InputDialog;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.util.ButtonUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertyEditorDialog
    extends InputDialog
{
  protected Logicim editor;
  protected StaticView<ComponentProperties> componentView;
  protected PropertiesPanel propertiesPanel;
  protected Circuit circuit;
  protected ActionButton saveAsDefaultButton;

  public PropertyEditorDialog(Frame owner,
                              String title,
                              Dimension dimension,
                              Logicim editor,
                              StaticView<ComponentProperties> componentView)
  {
    super(owner, title, true, dimension);

    this.editor = editor;
    this.circuit = getCircuit(editor);
    this.componentView = componentView;
  }

  protected static Circuit getCircuit(Logicim editor)
  {

    CircuitEditor circuitEditor = editor.getCircuitEditor();
    if (circuitEditor != null)
    {
      return circuitEditor.getCircuit();
    }
    else
    {
      return null;
    }
  }

  public void build()
  {
    Container contentPane = getContentPane();
    contentPane.setLayout(new GridBagLayout());

    JPanel editorPanel = createEditorPanel();
    propertiesPanel = (PropertiesPanel) editorPanel;
    contentPane.add(editorPanel, gridBagConstraints(0, 0, 1, 1, BOTH));

    ActionButton okayButton = new ActionButton("Okay", this);
    setOkayButton(okayButton);

    saveAsDefaultButton = new ActionButton("Save Defaults", this);
    JPanel bottomPanel = buildButtons(1,
                                      ButtonUtil.DEFAULT_WIDTH,
                                      saveAsDefaultButton,
                                      okayButton,
                                      new CancelButton("Cancel", this));
    contentPane.add(bottomPanel, gridBagConstraints(0, 2, 0, 0, BOTH));
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

  @Override
  public boolean executeButtonAction(ActionButton actionButton)
  {
    boolean handled = super.executeButtonAction(actionButton);
    if (handled)
    {
      return true;
    }

    if (actionButton == saveAsDefaultButton)
    {
      ComponentProperties properties = getPropertiesPanel().createProperties(componentView.getProperties());
      componentView.clampProperties(properties);
      DefaultComponentProperties.getInstance().put((Class<? extends StaticView<?>>) componentView.getClass(), properties);
      return true;
    }
    return false;
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

  public StaticView<?> getComponentView()
  {
    return componentView;
  }

  protected abstract JPanel createEditorPanel();

  protected abstract ComponentProperties updateProperties();
}

