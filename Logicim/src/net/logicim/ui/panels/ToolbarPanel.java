package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.*;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.icons.IconLoader.*;

public class ToolbarPanel
    extends ButtonBarPanel
{
  public ToolbarPanel(JFrame frame, Logicim editor)
  {
    super(frame);
    setLayout(new GridBagLayout());
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    List<JComponent> components = new ArrayList<>();
    components.add(createHorizontalButtonInput(editor, LOAD, LoadSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SAVE, SaveSimulationAction.NAME));
    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createHorizontalButtonInput(editor, SIMULATION_SELECT, NewSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_SELECT, SelectSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_RESET, ResetSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_RUN, RunSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_PAUSE, PauseSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_DEFAULT, NormalSpeedSimulationAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_FASTER, IncreaseSimulationSpeedAction.NAME));
    components.add(createHorizontalButtonInput(editor, SIMULATION_SLOWER, DecreaseSimulationSpeedAction.NAME));
    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createHorizontalButtonInput(editor, ROTATE_LEFT, PlacementRotateLeftAction.NAME));
    components.add(createHorizontalButtonInput(editor, ROTATE_RIGHT, PlacementRotateRightAction.NAME));
    components.add(createHorizontalButtonInput(editor, MIRROR_HORIZONTAL, PlacementFlipHorizontallyAction.NAME));
    components.add(createHorizontalButtonInput(editor, MIRROR_VERTICAL, PlacementFlipVerticallyAction.NAME));

    int x = 0;
    for (JComponent component : components)
    {
      add(component, GridBagUtil.gridBagConstraints(x, 0, 0, 0, GridBagConstraints.BOTH));
      x++;
    }
    add(new JPanel(), GridBagUtil.gridBagConstraints(x, 0, 1, 0, GridBagConstraints.BOTH));
  }
}

