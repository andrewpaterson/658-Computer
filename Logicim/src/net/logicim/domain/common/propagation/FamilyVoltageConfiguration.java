package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.voltage.Voltage;

import java.util.ArrayList;
import java.util.List;

public class FamilyVoltageConfiguration
{
  protected String family;
  protected List<VoltageConfiguration> voltageConfigurations;
  protected VoltageConfiguration ttlConfiguration;
  protected VoltageConfiguration cmosConfiguration;

  public FamilyVoltageConfiguration(String family)
  {
    this.family = family;
    this.ttlConfiguration = null;
    this.cmosConfiguration = null;
    this.voltageConfigurations = new ArrayList<>();
  }

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
}

