package net.logicim.ui.debugdetail;

import net.logicim.ui.common.Viewport;

import java.awt.*;

public abstract class InformationPanelFactory
{
  public abstract InformationPanel createInformationPanel(Graphics2D graphics,
                                                          Viewport viewport,
                                                          int width,
                                                          int height);
}

