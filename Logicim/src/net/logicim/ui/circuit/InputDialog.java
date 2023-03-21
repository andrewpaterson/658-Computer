package net.logicim.ui.circuit;

import net.logicim.ui.components.button.ActionButton;
import net.logicim.ui.components.button.ButtonAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class InputDialog
    extends JDialog
    implements ButtonAction,
               KeyListener,
               ContainerListener

{
  protected Dimension dimension;
  protected ActionButton okayButton;

  public InputDialog(Frame owner,
                     String title,
                     boolean modal,
                     Dimension dimension)
  {
    super(owner, title, modal);
    this.dimension = dimension;
    setSize(dimension);
    addKeyAndContainerListenerRecursively(this);
  }

  public Dimension getDimension()
  {
    return dimension;
  }

  public void setOkayButton(ActionButton okayButton)
  {
    this.okayButton = okayButton;
  }

  protected void close()
  {
    setVisible(false);
    dispose();
  }

  public boolean executeButtonAction(ActionButton actionButton)
  {
    if (actionButton == okayButton)
    {
      okay();
      return true;
    }
    return false;
  }

  protected void addKeyAndContainerListenerRecursively(Component c)
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

  public void componentAdded(ContainerEvent e)
  {
    addKeyAndContainerListenerRecursively(e.getChild());
  }

  public void componentRemoved(ContainerEvent e)
  {
  }

  public void keyTyped(KeyEvent e)
  {
  }

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
      executeButtonAction(okayButton);
    }

    if ((keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE))
    {
      close();
    }
  }

  public abstract void build();

  public abstract void okay();
}
