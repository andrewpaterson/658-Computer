package net.logicim.ui;

import net.logicim.common.reflect.ClassInspector;
import net.logicim.common.reflect.EnumStore;
import net.logicim.data.SaveDataClassStore;
import net.logicim.data.common.SaveData;
import net.logicim.ui.common.Colours;
import net.logicim.ui.panels.*;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;
import net.logicim.ui.subcircuit.SubcircuitListModel;
import net.logicim.ui.util.GridBagUtil;
import net.logicim.ui.util.WindowSizer;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static java.awt.GridBagConstraints.*;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

public class SimulatorFrame
    extends JFrame
    implements WindowListener,
               KeyListener,
               ContainerListener
{
  protected SimulatorPanel simulatorPanel;
  protected ToolbarPanel toolbarPanel;
  protected DisplayPanel displayPanel;
  protected CreationPanel creationPanel;
  protected SelectedInfoPanel selectedInfoPanel;
  protected CircuitInfoPanel circuitInfoPanel;

  public SimulatorFrame() throws HeadlessException
  {
    simulatorPanel = new SimulatorPanel(this);
    toolbarPanel = new ToolbarPanel(this, simulatorPanel.getEditor());
    displayPanel = new DisplayPanel(this, simulatorPanel.getEditor());
    creationPanel = new CreationPanel(this, simulatorPanel.getEditor());
    selectedInfoPanel = new SelectedInfoPanel(this);
    circuitInfoPanel = new CircuitInfoPanel(this, simulatorPanel.getEditor());
    JPanel surroundPanel = createSubcircuitPanel(simulatorPanel);

    setLayout(new GridBagLayout());
    add(toolbarPanel, GridBagUtil.gridBagConstraints(0, 0, 1, 0, HORIZONTAL, 3, 1));  // Toolbar row

    add(displayPanel, GridBagUtil.gridBagConstraints(0, 1, 0, 1, VERTICAL));          // Display settings
    add(surroundPanel, GridBagUtil.gridBagConstraints(1, 1, 1, 1, BOTH));              // Simulator panel
    add(creationPanel, GridBagUtil.gridBagConstraints(2, 1, 0, 1, VERTICAL));          // Object creation

    add(selectedInfoPanel, GridBagUtil.gridBagConstraints(0, 2, 1, 0, HORIZONTAL, 3, 1));  // Selected object info row
    add(circuitInfoPanel, GridBagUtil.gridBagConstraints(0, 3, 1, 0, HORIZONTAL, 3, 1));  // Simulation info row

    JMenuBar menuBar;
    JMenu menu;
    JMenuItem menuItem;

    menuBar = new JMenuBar();
    menu = new JMenu("A Menu");
    menu.setMnemonic(KeyEvent.VK_A);
    menu.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
    menuBar.add(menu);

    menuItem = new JMenuItem("A text-only menu item", KeyEvent.VK_T);
    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK));
    menuItem.getAccessibleContext().setAccessibleDescription("This doesn't really do anything");
    menu.add(menuItem);
    setJMenuBar(menuBar);

    addWindowListener(this);

    ListenerHelper.addKeyAndContainerListenerRecursively(this, this, this);
  }

  protected JPanel createSubcircuitPanel(SimulatorPanel simulatorPanel)
  {
    JList<SubcircuitEditor> table = new JList<>();
    SubcircuitListModel subcircuitListModel = new SubcircuitListModel(simulatorPanel.getSubcircuitList(), table);
    simulatorPanel.getEditor().setSubcircuitListChangedNotifier(subcircuitListModel);
    table.setModel(subcircuitListModel);
    table.setMinimumSize(new Dimension(0, 0));
    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setMinimumSize(new Dimension(0, 0));

    table.setBackground(Colours.getInstance().getPanelBackground());
    JSplitPane splitPane = new JSplitPane(HORIZONTAL_SPLIT, scrollPane, simulatorPanel);
    splitPane.setDividerLocation(200);
    return createSurroundPanel(splitPane);
  }

  protected JPanel createSurroundPanel(JComponent insetPanel)
  {
    JPanel panel = new JPanel();
    panel.setBorder(new MatteBorder(1, 1, 1, 1, Colours.getInstance().getPanelBorder()));
    panel.setOpaque(false);
    panel.setLayout(new GridBagLayout());
    panel.add(insetPanel, GridBagUtil.gridBagConstraints(0, 0, 1, 1, BOTH));
    return panel;
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
    simulatorPanel.keyPressed(e);
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

  @Override
  public void componentAdded(ContainerEvent e)
  {
  }

  @Override
  public void componentRemoved(ContainerEvent e)
  {

  }
}

