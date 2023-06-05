package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.Colours;
import net.logicim.ui.info.*;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CircuitInfoPanel
    extends InformationPanel
{
  public CircuitInfoPanel(JFrame frame, Logicim editor)
  {
    super(frame);
    setLayout(new GridBagLayout());
    setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, getPanelInfoBorder()));
    setBackground(getPanelBackground());
    setPreferredSize(new Dimension(16, 16));

    List<JComponent> components = new ArrayList<>();
    components.add(createLeft(editor, new SubcircuitInfo(editor), 200));
    components.add(createLeft(editor, new SimulationInfo(editor), 200));

    components.add(new JPanel());

    components.add(createRight(editor, new MouseXInfo(editor)));
    components.add(createRight(editor, new MouseYInfo(editor)));
    components.add(createRight(editor, new ZoomInfo(editor)));

    int x = 0;
    for (JComponent component : components)
    {
      if (component instanceof JLabel)
      {
        add(component, GridBagUtil.gridBagConstraints(x, 0, 0, 0, GridBagConstraints.NONE));
      }
      else if (component instanceof JPanel)
      {
        add(component, GridBagUtil.gridBagConstraints(x, 0, 1, 1, GridBagConstraints.BOTH));
      }
      x++;
    }
  }

  protected JLabel createRight(Logicim editor, InfoLabel infoLabel)
  {
    JLabel label = infoLabel.getLabel();
    label.setMinimumSize(new Dimension(80, 16));
    label.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Colours.getInstance().getPanelInfoBorder()));
    editor.addInfoLabel(infoLabel);
    return label;
  }

  protected JLabel createLeft(Logicim editor, InfoLabel infoLabel, int width)
  {
    JLabel label = infoLabel.getLabel();
    label.setMinimumSize(new Dimension(width, 16));
    label.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Colours.getInstance().getPanelInfoBorder()));
    editor.addInfoLabel(infoLabel);
    return label;
  }
}

