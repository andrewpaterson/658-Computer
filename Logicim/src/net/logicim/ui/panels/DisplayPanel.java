package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.*;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.icons.IconLoader.*;

public class DisplayPanel
    extends ButtonBarPanel
{
  public DisplayPanel(JFrame frame, Logicim editor)
  {
    super(frame);
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    List<JComponent> components = new ArrayList<>();
    components.add(createVerticalButtonInput(editor, POINT_VIEW, TogglePointViewAction.NAME));
    components.add(createHorizontalSeparator());
    components.add(createVerticalButtonInput(editor, ZOOM_IN, ZoomInAction.NAME));
    components.add(createVerticalButtonInput(editor, ZOOM_OUT, ZoomOutAction.NAME));
    components.add(createVerticalButtonInput(editor, ZOOM_TO_DEFAULT, ZoomResetAction.NAME));
    components.add(createVerticalButtonInput(editor, ZOOM_FIT_ALL, ZoomFitAllAction.NAME));
    components.add(createVerticalButtonInput(editor, ZOOM_FIT_SELECTION, ZoomFitSelectionAction.NAME ));
    components.add(createHorizontalSeparator());
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

