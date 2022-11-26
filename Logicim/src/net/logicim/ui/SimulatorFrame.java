package net.logicim.ui;

import net.logicim.common.reflect.ClassInspector;
import net.logicim.data.SaveData;
import net.logicim.data.SaveDataClassStore;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SimulatorFrame
    extends JFrame
{
  public static void main(String[] args)
  {
    SimulatorFrame simulatorFrame = new SimulatorFrame();

    ensureDataConstructors();

    JPanel newLeftComponent = new JPanel();
    newLeftComponent.setMinimumSize(new Dimension(150, 200));
    SimulatorPanel simulatorPanel = new SimulatorPanel(simulatorFrame);
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, newLeftComponent, simulatorPanel);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(150);

    simulatorFrame.add(splitPane);

    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;

    menuBar = new JMenuBar();
    menu = new JMenu("A Menu");
    menu.setMnemonic(KeyEvent.VK_A);
    menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);

    menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);
    simulatorFrame.setJMenuBar(menuBar);

    simulatorFrame.addWindowListener(new WindowAdapter()
    {
      public void windowClosing(WindowEvent e)
      {
        simulatorPanel.windowClosing();
      }
    });

    simulatorPanel.addComponentListener(new ComponentAdapter()
    {
      @Override
      public void componentResized(ComponentEvent e)
      {
        simulatorPanel.componentResized(e.getComponent().getWidth(), e.getComponent().getHeight());
      }
    });

    simulatorPanel.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        simulatorPanel.mousePressed(e.getX(), e.getY(), e.getButton());
      }

      @Override
      public void mouseReleased(MouseEvent e)
      {
        simulatorPanel.mouseReleased(e.getX(), e.getY(), e.getButton());
      }

      @Override
      public void mouseEntered(MouseEvent e)
      {
        simulatorPanel.mouseEntered(e.getX(), e.getY());
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        simulatorPanel.mouseExited();
      }
    });

    simulatorPanel.addMouseMotionListener(new MouseMotionAdapter()
    {
      @Override
      public void mouseMoved(MouseEvent e)
      {
        simulatorPanel.mouseMoved(e.getX(), e.getY());
      }

      @Override
      public void mouseDragged(MouseEvent e)
      {
        simulatorPanel.mouseMoved(e.getX(), e.getY());
      }
    });

    simulatorPanel.addMouseWheelListener(new MouseAdapter()
    {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e)
      {
        super.mouseWheelMoved(e);
        simulatorPanel.mouseWheel(e.getWheelRotation());
      }
    });

    simulatorFrame.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyPressed(KeyEvent e)
      {
        simulatorPanel.keyPressed(e.getExtendedKeyCode());
      }

      @Override
      public void keyReleased(KeyEvent e)
      {
        simulatorPanel.keyReleased(e.getExtendedKeyCode());
      }
    });

    simulatorFrame.setTitle("Logicim");
    simulatorFrame.setMinimumSize(new Dimension(320, 240));
    simulatorFrame.setVisible(true);

    WindowSizer.setPercentageOfScreenSize(simulatorFrame, 65.0, 65.0);
    WindowSizer.centre(simulatorFrame);

    simulatorPanel.loop();
  }

  protected static void ensureDataConstructors()
  {
    List<Class<SaveData>> classes = SaveDataClassStore.getInstance().findAll();
    for (Class<SaveData> aClass : classes)
    {
      ClassInspector classInspector = ClassInspector.forClass(aClass);
      classInspector.newInstance();
    }
  }
}

