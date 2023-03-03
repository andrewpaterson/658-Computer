package net.logicim.ui.property;

import net.logicim.domain.common.Circuit;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.common.Rotation;
import net.logicim.ui.common.integratedcircuit.ComponentProperties;
import net.logicim.ui.common.integratedcircuit.StaticView;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;
import net.logicim.ui.components.button.CancelButton;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static java.awt.GridBagConstraints.BOTH;
import static net.logicim.ui.util.ButtonUtil.DEFAULT_WIDTH;
import static net.logicim.ui.util.ButtonUtil.buildButtons;
import static net.logicim.ui.util.GridBagUtil.gridBagConstraints;

public abstract class PropertyEditorDialog
    extends JDialog
    implements ButtonAction,
               KeyListener,
               ContainerListener
{
  protected Dimension dimension;
  protected SimulatorEditor editor;
  protected StaticView<?> componentView;
  protected PropertiesPanel propertiesPanel;
  protected Circuit circuit;

  public PropertyEditorDialog(Frame owner, String title, Dimension dimension, SimulatorEditor editor, StaticView<?> componentView)
  {
    super(owner, title, true);

    this.dimension = dimension;
    setSize(dimension);
    this.editor = editor;
    this.circuit = this.editor.getCircuitEditor().getCircuit();
    this.componentView = componentView;

    addKeyAndContainerListenerRecursively(this);
  }

  public void componentAdded(ContainerEvent e)
  {
    addKeyAndContainerListenerRecursively(e.getChild());
  }

  @Override
  public void componentRemoved(ContainerEvent e)
  {
  }

  private void addKeyAndContainerListenerRecursively(Component c)
  {

    c.addKeyListener(this);

    if (c instanceof Container)
    {

      Container cont = (Container) c;

      cont.addContainerListener(this);

      Component[] children = cont.getComponents();

      for (Component child : children)
      {
        addKeyAndContainerListenerRecursively(child);
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyReleased(KeyEvent e)
  {

  }

  public void keyPressed(KeyEvent keyEvent)
  {
    if ((keyEvent.getKeyCode() == KeyEvent.VK_ENTER) &&
        keyEvent.isControlDown() &&
        !keyEvent.isShiftDown() &&
        !keyEvent.isAltDown() &&
        !keyEvent.isMetaDown())
    {
      okay();
    }

    if ((keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE))
    {
      close();
    }
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

  @Override
  public void executeButtonAction()
  {
    okay();
  }

  public void okay()
  {
    ComponentProperties newComponentProperties = updateProperties();

    if (newComponentProperties != null)
    {
      editor.addEditorEvent(new PropertyEditEvent(componentView, newComponentProperties, editor.getCircuitEditor().getCurrentSubcircuitView(), circuit));
    }

    close();
  }

  protected void close()
  {
    setVisible(false);
    dispose();
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

