package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceValue;
import net.logicim.domain.common.voltage.Voltage;

import java.util.ArrayList;
import java.util.Collections;
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

  public VoltageConfiguration getEqualOrHigherConfiguration(float vcc)
  {
    for (VoltageConfiguration voltageConfiguration : voltageConfigurations)
    {
      if (voltageConfiguration.vcc >= vcc)
      {
        return voltageConfiguration;
      }
    }
    return null;
  }

  public VoltageConfiguration getLowerConfiguration(float vcc)
  {
    VoltageConfiguration previousVoltageConfiguration = null;
    for (VoltageConfiguration voltageConfiguration : voltageConfigurations)
    {
      if (voltageConfiguration.vcc >= vcc)
      {
        return previousVoltageConfiguration;
      }
      previousVoltageConfiguration = voltageConfiguration;
    }
    return previousVoltageConfiguration;
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
    if (vcc == 0)
    {
      return 0;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.getHighVoltageIn();
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateVoltage(higherConfiguration.getHighVoltageIn(), lowerConfiguration.getHighVoltageIn(), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return linearInterpolateVoltage(higherConfiguration.getHighVoltageIn(), 0, higherConfiguration.vcc, 0, vcc);
        }
      }
    }
    else
    {
      VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
      float fraction = vcc / lowerConfiguration.vcc;
      return lowerConfiguration.getHighVoltageIn() * fraction;
    }
  }

  float linearInterpolateVoltage(float higherVoltage, float lowerVoltage, float higherVCC, float lowerVCC, float vcc)
  {
    float configurationVCCDiff = higherVCC - lowerVCC;
    float vccDiff = vcc - lowerVCC;
    float fraction = vccDiff / configurationVCCDiff;

    float configurationHighVoltageDiff = higherVoltage - lowerVoltage;
    return lowerVoltage + configurationHighVoltageDiff * fraction;
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

  public void sort()
  {
    Collections.sort(voltageConfigurations);
  }
}
