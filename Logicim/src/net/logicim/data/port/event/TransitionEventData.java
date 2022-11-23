package net.logicim.data.port.event;

public class TransitionEventData
    extends PortInputEventData
{
  protected float voltage;

  public TransitionEventData(long time, float voltage)
  {
    super(time);
    this.voltage = voltage;
  }
}

