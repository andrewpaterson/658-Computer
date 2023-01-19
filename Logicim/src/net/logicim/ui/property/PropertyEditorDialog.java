package net.logicim.ui.property;

import net.logicim.common.type.Int2D;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.ComponentView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.simulation.CircuitEditor;
import net.logicim.ui.simulation.component.factory.ViewFactory;
import net.logicim.ui.simulation.component.factory.ViewFactoryStore;
import net.logicim.ui.util.ButtonUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.DEFAULT_WIDTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertyEditorDialog
    extends JDialog
    implements ButtonAction
{
  protected Dimension dimension;
  protected SimulatorEditor editor;
  protected ComponentView<?> componentView;
  protected PropertiesPanel propertiesPanel;

  public PropertyEditorDialog(Frame owner, String title, Dimension dimension, SimulatorEditor editor, ComponentView<?> componentView)
  {
    super(owner, title, true);

    this.dimension = dimension;
    setSize(dimension);
    this.editor = editor;
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

  public Dimension getDimension()
  {
    return dimension;
  }

  protected ComponentView<?> recreateComponentView(ComponentProperties properties)
  {
    CircuitEditor circuitEditor = editor.getCircuitEditor();

    Rotation rotation = componentView.getRotation();
    Int2D position = componentView.getPosition();

    Class<? extends ComponentView<?>> aClass = (Class<? extends ComponentView<?>>) componentView.getClass();
    ViewFactory viewFactory = ViewFactoryStore.getInstance().get(aClass);
    ComponentView<?> newComponentView = viewFactory.create(circuitEditor, position, rotation, properties);

    circuitEditor.deleteComponentView(this.componentView);
    circuitEditor.placeComponentView(newComponentView);

    return newComponentView;
  }

  @Override
  public void executeButtonAction()
  {
    okay();
  }

  public void okay()
  {
    boolean propertyChanged = updateProperties();

    if (propertyChanged)
    {
      this.componentView.clampProperties();
      ComponentView<?> componentView = recreateComponentView(this.componentView.getProperties());
      editor.replaceSelection(componentView, this.componentView);
      editor.pushUndo();
    }

    setVisible(false);
    dispose();
  }

  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }

  protected abstract JPanel createEditorPanel();

  protected abstract boolean updateProperties();
}

