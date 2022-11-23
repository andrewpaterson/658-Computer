package net.logicim.data.port.event;

public class SlewEventData
    extends PortOutputEventData
{
  protected float startVoltage;
  protected float endVoltage;
  protected long slewTime;

  public SlewEventData(long time, float startVoltage, float endVoltage, long slewTime)
  {
    super(time);
    this.startVoltage = startVoltage;
    this.endVoltage = endVoltage;
    this.slewTime = slewTime;
  }
}

