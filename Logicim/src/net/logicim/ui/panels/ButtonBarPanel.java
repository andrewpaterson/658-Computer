package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.Colours;
import net.logicim.ui.icons.IconLoader;
import net.logicim.ui.input.button.ButtonInput;

import javax.swing.*;
import java.awt.*;

public class ButtonBarPanel
    extends JPanel
{
  protected JFrame frame;

  public ButtonBarPanel(JFrame frame)
  {
    this.frame = frame;
  }

  protected Color getPanelBackground()
  {
    return Colours.getInstance().getPanelBackground();
  }

  protected Dimension defaultButtonBarSize()
  {
    return new Dimension(32 + 8, 32 + 8);
  }

  protected JButton createHorizontalButtonInput(Logicim editor, String iconKey, String actionName)
  {
    JButton button = createButton(iconKey, 40, 32);
    ButtonInput buttonInput = new ButtonInput(editor.getAction(actionName), button);
    editor.addButtonInput(buttonInput);
    return buttonInput.getButton();
  }

  protected JButton createVerticalButtonInput(Logicim editor, String iconKey, String actionName)
  {
    JButton button = createButton(iconKey, 32, 40);
    ButtonInput buttonInput = new ButtonInput(editor.getAction(actionName), button);
    editor.addButtonInput(buttonInput);
    return buttonInput.getButton();
  }

  protected JButton createButton(String iconKey, int width, int height)
  {
    JButton button = new JButton(IconLoader.getIcon(iconKey));
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setPreferredSize(new Dimension(width, height));
    button.setRolloverIcon(IconLoader.getRolloverIcon(iconKey));
    button.setPressedIcon(IconLoader.getPressedIcon(iconKey));
    button.setDisabledIcon(IconLoader.getDisabledIcon(iconKey));
    button.setContentAreaFilled(false);
    button.setFocusPainted(false);
    button.setFocusable(false);
    return button;
  }

  protected JSeparator createHorizontalSeparator()
  {
    JSeparator separator = new JSeparator();
    separator.setPreferredSize(new Dimension(32, 2));
    return separator;
  }
}

