package net.logicim.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerListener;
import java.awt.event.KeyListener;

public class ListenerHelper
{
  public static void addKeyAndContainerListenerRecursively(Component c, KeyListener keyListener, ContainerListener containerListener)
  {
    if (!((c instanceof JTextArea) || (c instanceof JTextField)))
    {
      c.addKeyListener(keyListener);
    }

    if (c instanceof Container)
    {
      Container cont = (Container) c;
      cont.addContainerListener(containerListener);
      Component[] children = cont.getComponents();
      for (Component aChildren : children)
      {
        addKeyAndContainerListenerRecursively(aChildren, keyListener, containerListener);
      }
    }
  }

  public static void removeKeyAndContainerListenerRecursively(Component c, KeyListener keyListener, ContainerListener containerListener)
  {
    if (!((c instanceof JTextArea) || (c instanceof JTextField)))
    {
      c.removeKeyListener(keyListener);
    }
    if (c instanceof Container)
    {
      Container cont = (Container) c;
      cont.removeContainerListener(containerListener);
      Component[] children = cont.getComponents();
      for (Component aChildren : children)
      {
        removeKeyAndContainerListenerRecursively(aChildren, keyListener, containerListener);
      }
    }
  }
}

