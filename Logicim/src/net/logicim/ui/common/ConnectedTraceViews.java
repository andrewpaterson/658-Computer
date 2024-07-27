package net.logicim.ui.common;

import java.util.ArrayList;
import java.util.List;

public class ConnectedTraceViews
{
  protected ConnectionView connectionView;

  public List<ConnectionView> connectionViews;

  public ConnectedTraceViews(ConnectionView connectionView)
  {
    this.connectionView = connectionView;
    this.connectionViews = new ArrayList<>();
  }
}

