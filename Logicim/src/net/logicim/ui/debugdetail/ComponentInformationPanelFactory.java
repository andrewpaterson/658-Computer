package net.logicim.ui.debugdetail;

import net.logicim.ui.common.Viewport;
import net.logicim.ui.common.integratedcircuit.StaticView;

import java.awt.*;

public class ComponentInformationPanelFactory
    extends InformationPanelFactory
{
  protected StaticView<?> componentView;

  public ComponentInformationPanelFactory(StaticView<?> componentView)
  {
    this.componentView = componentView;
  }

  @Override
  public ComponentInformationPanel createInformationPanel(Graphics2D graphics,
                                                          Viewport viewport,
                                                          int width,
                                                          int height)
  {
    return new ComponentInformationPanel(componentView,
                                         graphics,
                                         viewport,
                                         width,
                                         height);
  }
}

