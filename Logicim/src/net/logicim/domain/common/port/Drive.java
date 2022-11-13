package net.logicim.domain.common.port;

import static net.logicim.domain.common.port.DriveStrength.*;

public class Drive
{
  protected DriveStrength strength;
  protected float voltage;

  public Drive()
  {
    strength = DriveStrength.None;
    voltage = 0;
  }

  public boolean isDriven()
  {
    return strength == Full;
  }

  public float getVoltage()
  {
    return voltage;
  }

  public void driveVoltage(float voltage)
  {
    strength = Full;
    this.voltage = voltage;
  }
}

