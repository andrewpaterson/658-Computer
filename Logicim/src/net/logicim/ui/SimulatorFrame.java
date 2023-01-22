package net.logicim.ui;

import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.EnumStore;
import net.logicim.data.SaveData;
import net.logicim.data.SaveDataClassStore;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class SimulatorFrame
    extends JFrame
    implements WindowListener,
               KeyListener
{
  private SimulatorPanel simulatorPanel;

  public SimulatorFrame() throws HeadlessException
  {
    simulatorPanel = new SimulatorPanel(this);
    add(simulatorPanel);

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
    setJMenuBar(menuBar);

    addWindowListener(this);
    addKeyListener(this);
  }

  public static void main(String[] args)
  {
    ensureDataConstructors();
    ensureEnumStores();

    SimulatorFrame simulatorFrame = new SimulatorFrame();

    simulatorFrame.setTitle("Logicim");
    simulatorFrame.setMinimumSize(new Dimension(320, 240));
    simulatorFrame.setVisible(true);

    WindowSizer.setPercentageOfScreenSize(simulatorFrame, 65.0, 65.0);
    WindowSizer.centre(simulatorFrame);

    simulatorFrame.loop();
  }

  private void loop()
  {
    simulatorPanel.loop();
  }

  protected static void ensureEnumStores()
  {
    EnumStore.getInstance();
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

  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    simulatorPanel. keyPressed(e);
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    simulatorPanel.keyReleased(e);
  }


  @Override
  public void windowOpened(WindowEvent e)
  {
  }

  @Override
  public void windowClosing(WindowEvent e)
  {
    simulatorPanel.windowClosing();
  }

  @Override
  public void windowClosed(WindowEvent e)
  {
  }

  @Override
  public void windowIconified(WindowEvent e)
  {
  }

  @Override
  public void windowDeiconified(WindowEvent e)
  {
  }

  @Override
  public void windowActivated(WindowEvent e)
  {
  }

  @Override
  public void windowDeactivated(WindowEvent e)
  {
  }
}

