package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.editor.*;
import net.logicim.ui.icons.IconLoader;
import net.logicim.ui.input.button.ButtonInput;
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
    components.add(createButtonInput(editor, LOAD, LoadSimulationAction.NAME).getButton());
    components.add(createButtonInput(editor, SAVE, SaveSimulationAction.NAME).getButton());
    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createButtonInput(editor, SIMULATION_RESET, ResetSimulationAction.NAME).getButton());
    components.add(createButtonInput(editor, SIMULATION_RUN, RunSimulationAction.NAME).getButton());
    components.add(createButtonInput(editor, SIMULATION_PAUSE, PauseSimulationAction.NAME).getButton());
    components.add(createButtonInput(editor, SIMULATION_DEFAULT, NormalSpeedSimulationAction.NAME).getButton());
    components.add(createButtonInput(editor, SIMULATION_FASTER, IncreaseSimulationSpeedAction.NAME).getButton());
    components.add(createButtonInput(editor, SIMULATION_SLOWER, DecreaseSimulationSpeedAction.NAME).getButton());

    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createButtonInput(editor, ROTATE_LEFT, PlacementRotateLeftAction.NAME).getButton());
    components.add(createButtonInput(editor, ROTATE_RIGHT, PlacementRotateRightAction.NAME).getButton());
    components.add(createButtonInput(editor, MIRROR_HORIZONTAL, PlacementFlipHorizontallyAction.NAME).getButton());
    components.add(createButtonInput(editor, MIRROR_VERTICAL, PlacementFlipVerticallyAction.NAME).getButton());

    int x = 0;
    for (JComponent component : components)
    {
      add(component, GridBagUtil.gridBagConstraints(x, 0, 0, 0, GridBagConstraints.BOTH));
      x++;
    }
    add(new JPanel(), GridBagUtil.gridBagConstraints(x, 0, 1, 0, GridBagConstraints.BOTH));
  }

  protected ButtonInput createButtonInput(Logicim editor, String iconKey, String actionName)
  {
    JButton button = createButton(iconKey);
    return new ButtonInput(editor.getAction(actionName), button);
  }

  protected JButton createButton(String iconKey)
  {
    JButton button = new JButton(IconLoader.getIcon(iconKey));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setPreferredSize(new Dimension(40, 32));
    button.setRolloverIcon(IconLoader.getRolloverIcon(iconKey));
    button.setPressedIcon(IconLoader.getPressedIcon(iconKey));
    button.setDisabledIcon(IconLoader.getDisabledIcon(iconKey));
    button.setContentAreaFilled(false);
    button.setFocusPainted(false);
    button.setFocusable(false);
    return button;
  }
}

