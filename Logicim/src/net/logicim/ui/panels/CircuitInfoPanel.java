package net.logicim.ui.panels;

import net.logicim.ui.Logicim;
import net.logicim.ui.common.Colours;
import net.logicim.ui.info.InfoLabel;
import net.logicim.ui.info.MouseXInfo;
import net.logicim.ui.info.MouseYInfo;
import net.logicim.ui.info.ZoomInfo;
import net.logicim.ui.util.GridBagUtil;

import javax.swing.*;
import java.awt.*;

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

    add(new JPanel(), GridBagUtil.gridBagConstraints(0, 0, 1, 1, GridBagConstraints.BOTH));

    add(createRight(editor, new MouseXInfo(editor)), GridBagUtil.gridBagConstraints(1, 0, 0, 0, GridBagConstraints.NONE));
    add(createRight(editor, new MouseYInfo(editor)), GridBagUtil.gridBagConstraints(2, 0, 0, 0, GridBagConstraints.NONE));
    add(createRight(editor, new ZoomInfo(editor)), GridBagUtil.gridBagConstraints(3, 0, 0, 0, GridBagConstraints.NONE));
  }

  protected JLabel createRight(Logicim editor, InfoLabel infoLabel)
  {
    JLabel label = infoLabel.getLabel();
    label.setMinimumSize(new Dimension(80, 16));
    label.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Colours.getInstance().getPanelInfoBorder()));
    editor.addInfoLabel(infoLabel);
    return label;
  }
}

