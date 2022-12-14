package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.common.voltage.Voltage;

import java.util.ArrayList;
import java.util.List;

public class FamilyVoltageConfiguration
    extends VoltageConfigurationSource
{
  protected Family family;
  protected List<VoltageConfiguration> voltageConfigurations;
  protected VoltageConfiguration ttlConfiguration;
  protected VoltageConfiguration cmosConfiguration;

  public FamilyVoltageConfiguration(Family family)
  {
    this.family = family;
    this.ttlConfiguration = null;
    this.cmosConfiguration = null;
    this.voltageConfigurations = new ArrayList<>();
  }

  @Override
  public VoltageConfiguration get(float vcc)
  {
    for (VoltageConfiguration voltageConfiguration : voltageConfigurations)
    {
      if (voltageConfiguration.vcc == vcc)
      {
        return voltageConfiguration;
      }
    }
    return null;
  }

  public void add(VoltageConfiguration voltageConfiguration, String logicLevel)
  {
    VoltageConfiguration existingVoltageConfiguration = get(voltageConfiguration.vcc);
    if (existingVoltageConfiguration != null)
    {
      throw new SimulatorException("Voltage configuration for Family [%s], VCC [%s] already added.", family, Voltage.getVoltageString(voltageConfiguration.vcc));
    }

    voltageConfigurations.add(voltageConfiguration);

    if (logicLevel.equalsIgnoreCase("TTL"))
    {
      ttlConfiguration = voltageConfiguration;
    }
    else if (logicLevel.equalsIgnoreCase("CMOS"))
    {
      cmosConfiguration = voltageConfiguration;
    }
  }

  public float getHighVoltageIn(float vcc)
  {
    return 0;
  }

  public float getLowVoltageIn(float vcc)
  {
    return 0;
  }

  public float getMidVoltageOut(float vcc)
  {
    return 0;
  }

  public float getVoltageOut(boolean value, float vcc)
  {
    return 0;
  }

  public void createOutputEvent(Timeline timeline, Port port, float voltageOut)
  {

  }

  public TraceValue getValue(float vin, float vcc)
  {
    return null;
  }

  public float calculateStartVoltage(float portVoltage, float vcc)
  {
    return 0;
  }

  public float getVoltsPerTimeLowToHigh(float vcc)
  {
    return 0;
  }

  public float getVoltsPerTimeHighToLow(float vcc)
  {
    return 0;
  }
}

