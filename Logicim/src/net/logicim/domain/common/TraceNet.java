package net.logicim.domain.common;

import java.util.ArrayList;
import java.util.List;

public class TraceNet
{
  public static final float Unsettled = 0.5f;
  public static final float NotConnected = -1.0f;

  protected List<Trace> traces;
  protected float voltage;
  protected int width;

  protected Port drivingPort;

  public TraceNet(List<Trace> connected)
  {
    traces = connected;
    width = traces.size();
    voltage = -1.0f;

    drivingPort = null;
  }

  public TraceNet(Trace trace)
  {
    traces = new ArrayList<>();
    traces.add(trace);
    width = 1;
    voltage = -1.0f;

    drivingPort = null;
  }

  public void unsettle()
  {
    voltage = Unsettled;
    drivingPort = null;
  }

  public void update(float value, Port port)
  {
    drivingPort = port;
    this.voltage = value;
  }

  public float getVoltage()
  {
    return voltage;
  }

  public Port getDrivingPort()
  {
    return drivingPort;
  }
}

