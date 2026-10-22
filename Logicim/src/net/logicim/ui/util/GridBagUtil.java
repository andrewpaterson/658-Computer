package net.logicim.ui.util;

import java.awt.*;

public class GridBagUtil
{
  public static GridBagConstraints gridBagConstraints(int gridX, int gridY, double weightX, double weightY, int fill)
  {
    return gridBagConstraints(gridX, gridY, weightX, weightY, fill, 1, 1);
  }

  public static GridBagConstraints gridBagConstraints(int gridX, int gridY, double weightX, double weightY, int fill, int gridWidth, int gridHeight)
  {
    return gridBagConstraints(gridX, gridY, weightX, weightY, fill, gridWidth, gridHeight, new Insets(0, 0, 0, 0));
  }

  public static GridBagConstraints gridBagConstraints(int gridX, int gridY, double weightX, double weightY, int fill, Insets insets)
  {
    return gridBagConstraints(gridX, gridY, weightX, weightY, fill, 1, 1, insets);
  }

  public static GridBagConstraints gridBagConstraints(int gridX, int gridY, double weightX, double weightY, int fill, int gridWidth, int gridHeight, Insets insets)
  {
    GridBagConstraints gridBagConstraints = new GridBagConstraints();

    gridBagConstraints.gridx = gridX;
    gridBagConstraints.gridy = gridY;
    gridBagConstraints.weightx = weightX;
    gridBagConstraints.weighty = weightY;
    gridBagConstraints.gridwidth = gridWidth;
    gridBagConstraints.gridheight = gridHeight;
    gridBagConstraints.insets = insets;
    gridBagConstraints.fill = fill;

    return gridBagConstraints;
  }
}

