package net.logicim.ui.info;

import java.util.ArrayList;
import java.util.List;

public class InfoLabels
{
  protected List<InfoLabel> labels;

  public InfoLabels()
  {
    this.labels = new ArrayList<>();
  }

  public void updateLabels()
  {
    for (InfoLabel label : labels)
    {
      label.update();
    }
  }

  public void add(InfoLabel infoLabel)
  {
    labels.add(infoLabel);
  }
}

