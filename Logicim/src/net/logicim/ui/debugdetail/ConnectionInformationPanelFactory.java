package net.logicim.ui.debugdetail;

import net.logicim.ui.common.ConnectionView;
import net.logicim.ui.common.Viewport;

import java.awt.*;

public class ConnectionInformationPanelFactory
    extends InformationPanelFactory
{
  protected ConnectionView connectionView;

  public ConnectionInformationPanelFactory(ConnectionView connectionView)
  {
    this.connectionView = connectionView;
  }

  @Override
  public ConnectionInformationPanel createInformationPanel(Graphics2D graphics,
                                                           Viewport viewport,
                                                           int width,
                                                           int height)
  {
    return new ConnectionInformationPanel(connectionView,
                                          graphics,
                                          viewport,
                                          width,
                                          height);
  }
}

