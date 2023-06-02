package net.logicim.ui.panels;

import net.logicim.ui.icons.IconLoader;

import javax.swing.*;
import java.awt.*;

import static net.logicim.ui.icons.IconLoader.SAVE;

public class ToolbarPanel
    extends ButtonBarPanel
{
  public ToolbarPanel(JFrame frame)
  {
    super(frame);
    setLayout(new GridBagLayout());
    setBackground(getPanelBackground());
    setPreferredSize(defaultButtonBarSize());

    JButton button = new JButton(IconLoader.getIcon(SAVE));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setPreferredSize(new Dimension(32, 32));
    button.setRolloverIcon(IconLoader.getRolloverIcon(SAVE));
    button.setPressedIcon(IconLoader.getPressedIcon(SAVE));
    button.setContentAreaFilled(false);
    button.setFocusPainted(false);
    button.setFocusable(false);
    add(button);
  }
}

