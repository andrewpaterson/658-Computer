package net.logicim.domain.common;

import static net.logicim.domain.common.TraceNet.Unsettled;

public class Trace
{
  protected TraceNet net;

  public Trace()
  {
    net = new TraceNet(this);
  }

  public float getVoltage()
  {
    if (net != null)
    {
      return net.getVoltage();
    }
    else
    {
      return Unsettled;
    }
  }

  public void update(float voltage, Port port)
  {
    net.update(voltage, port);
  }

  public TraceNet getNet()
  {
    return net;
  }

  private void setNet(TraceNet net)
  {
    this.net = net;
  }

  public Port get_DEBUG_lastPortThatUpdated()
  {
    if (net != null)
    {
      return net.get_DEBUG_lastPortThatUpdated();
    }
    else
    {
      return null;
    }
  }

  public boolean isNotConnected()
  {
    return net == null;
  }
}

