package net.logicim.data.port.event;

public class DriveEventData
    extends PortOutputEventData
{
  protected float voltage;

  public DriveEventData(long time, float voltage)
  {
    super(time);
    this.voltage = voltage;
  }
}

