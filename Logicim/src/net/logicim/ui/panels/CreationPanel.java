package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.TogglePointViewAction;
import net.logicim.ui.editor.ViewSimulationTreeAction;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.icons.IconLoader.POINT_VIEW;
import static net.logicim.ui.icons.IconLoader.VIEW_SIMULATION_TREE;

public class CreationPanel
    extends ButtonBarPanel
{
  public CreationPanel(JFrame frame, Logicim editor)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    List<JComponent> components = new ArrayList<>();
    components.add(createVerticalButtonInput(editor, POINT_VIEW, TogglePointViewAction.NAME));
    JSeparator separator = new JSeparator();
    separator.setPreferredSize(new Dimension(32, 2));
    components.add(separator);
    components.add(createVerticalButtonInput(editor, VIEW_SIMULATION_TREE, ViewSimulationTreeAction.NAME));

    int y = 0;
    for (JComponent component : components)
    {
      add(component, GridBagUtil.gridBagConstraints(0, y, 1, 0, GridBagConstraints.BOTH));
      y++;
    }
    add(new JPanel(), GridBagUtil.gridBagConstraints(0, y, 1, 1, GridBagConstraints.BOTH));
  }
}

