package net.logicim.ui.circuit;

import net.logicim.domain.common.Circuit;
import net.logicim.ui.SimulatorEditor;
import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;
import net.logicim.ui.components.button.CancelButton;
import net.logicim.ui.property.PropertiesPanel;

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

public class NewSubcircuitDialog
    extends JDialog
    implements ButtonAction,
               KeyListener,
               ContainerListener
{
  protected Dimension dimension;
  protected SimulatorEditor editor;
  protected SubcircuitPropertiesPanel propertiesPanel;
  protected Circuit circuit;

  public NewSubcircuitDialog(Frame owner,
                             SimulatorEditor editor)
  {
    super(owner, "Create subcircuit", true);

    this.dimension = new Dimension(392, 260);
    setSize(dimension);
    this.editor = editor;
    this.circuit = this.editor.getCircuitEditor().getCircuit();

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
    propertiesPanel = (SubcircuitPropertiesPanel) editorPanel;
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
    String name = propertiesPanel.getSubcircuitName();
    editor.addEditorEvent(new NewSubcircuitEvent(name));

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

  protected JPanel createEditorPanel()
  {
    return new SubcircuitPropertiesPanel("");
  }
}

