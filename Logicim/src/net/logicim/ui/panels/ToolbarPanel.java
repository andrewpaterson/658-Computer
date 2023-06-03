package net.logicim.ui.panels;

import net.logicim.ui.icons.IconLoader;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static net.logicim.ui.icons.IconLoader.*;

public class ToolbarPanel
    extends ButtonBarPanel
{
  public ToolbarPanel(JFrame frame)
  {
    super(frame);
    setLayout(new GridBagLayout());
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    List<JComponent> components = new ArrayList<>();
    components.add(createButton(LOAD));
    components.add(createButton(SAVE));
    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createButton(SIMULATION_RESET));
    components.add(createButton(SIMULATION_RUN));
    components.add(createButton(SIMULATION_PAUSE));
    components.add(createButton(SIMULATION_DEFAULT));
    components.add(createButton(SIMULATION_FASTER));
    components.add(createButton(SIMULATION_SLOWER));
    //Default speed
    components.add(new JSeparator(JSeparator.VERTICAL));
    components.add(createButton(ROTATE_LEFT));
    components.add(createButton(ROTATE_RIGHT));
    components.add(createButton(MIRROR_HORIZONTAL));
    components.add(createButton(MIRROR_VERTICAL));

    int x = 0;
    for (JComponent component : components)
    {
      add(component, GridBagUtil.gridBagConstraints(x, 0, 0, 0, GridBagConstraints.BOTH));
      x++;
    }
    add(new JPanel(), GridBagUtil.gridBagConstraints(x, 0, 1, 0, GridBagConstraints.BOTH));
  }

  protected JButton createButton(String key)
  {
    JButton button = new JButton(IconLoader.getIcon(key));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setPreferredSize(new Dimension(40, 32));
    button.setRolloverIcon(IconLoader.getRolloverIcon(key));
    button.setPressedIcon(IconLoader.getPressedIcon(key));
    button.setDisabledIcon(IconLoader.getDisabledIcon(key));
    button.setContentAreaFilled(false);
    button.setFocusPainted(false);
    button.setFocusable(false);
    return button;
  }
}

