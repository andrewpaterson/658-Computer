package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.*;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.icons.IconLoader.*;

public class CreationPanel
    extends ButtonBarPanel
{
  public CreationPanel(JFrame frame, Logicim editor)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    List<JComponent> components = new ArrayList<>();
    components.add(createVerticalButtonInput(editor, ADD_COMPONENT, AddComponentAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_TRACE, AddTraceAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_SPLITTER, AddSplitterAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_TUNNEL, AddTunnelAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_CONNECTION, AddConnectionAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_GND, AddPowerAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_LABEL, AddLabelAction.NAME));
    components.add(createVerticalButtonInput(editor, ADD_SUBCIRCUIT, AddSubcircuitAction.NAME));
    components.add(createHorizontalSeparator());
    components.add(createVerticalButtonInput(editor, NEW_SUBCIRCUIT, NewSubcircuitAction.NAME));

    int y = 0;
    for (JComponent component : components)
    {
      add(component, GridBagUtil.gridBagConstraints(0, y, 1, 0, GridBagConstraints.BOTH));
      y++;
    }
    add(new JPanel(), GridBagUtil.gridBagConstraints(0, y, 1, 1, GridBagConstraints.BOTH));
  }
}

