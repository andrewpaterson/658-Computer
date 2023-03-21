package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;
import net.logicim.data.family.Family;
import net.logicim.domain.common.voltage.Voltage;
import net.logicim.domain.common.wire.TraceValue;
import net.logicim.ui.common.defaults.DefaultLogicLevels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static net.logicim.ui.common.defaults.DefaultLogicLevels.CMOS;
import static net.logicim.ui.common.defaults.DefaultLogicLevels.TTL;

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
      throw new SimulatorException("Voltage configuration for Family [%s], VCC [%s] already added.", family, Voltage.toVoltageString(voltageConfiguration.vcc));
    }

    voltageConfigurations.add(voltageConfiguration);

    if (logicLevel.equalsIgnoreCase(TTL))
    {
      ttlConfiguration = voltageConfiguration;
    }
    else if (logicLevel.equalsIgnoreCase(DefaultLogicLevels.CMOS))
    {
      cmosConfiguration = voltageConfiguration;
    }
  }

  public float getHighVoltageIn(float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Float.NaN;
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

  public float getLowVoltageIn(float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Float.NaN;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.getLowVoltageIn();
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateVoltage(higherConfiguration.getLowVoltageIn(), lowerConfiguration.getLowVoltageIn(), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return linearInterpolateVoltage(higherConfiguration.getLowVoltageIn(), 0, higherConfiguration.vcc, 0, vcc);
        }
      }
    }
    else
    {
      VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
      float fraction = vcc / lowerConfiguration.vcc;
      return lowerConfiguration.getLowVoltageIn() * fraction;
    }
  }

  public float getVoltageOut(boolean value, float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Float.NaN;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.getVoltageOut(value);
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateVoltage(higherConfiguration.getVoltageOut(value), lowerConfiguration.getVoltageOut(value), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return linearInterpolateVoltage(higherConfiguration.getVoltageOut(value), 0, higherConfiguration.vcc, 0, vcc);
        }
      }
    }
    else
    {
      VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
      float fraction = vcc / lowerConfiguration.vcc;
      return lowerConfiguration.getVoltageOut(value) * fraction;
    }
  }

  public long calculateHoldTime(float outVoltage, float portVoltage, float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Long.MAX_VALUE;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.calculateHoldTime(outVoltage, portVoltage);
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateTime(higherConfiguration.calculateHoldTime(outVoltage, portVoltage), lowerConfiguration.calculateHoldTime(outVoltage, portVoltage), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return Long.MAX_VALUE;
        }
      }
    }
    else
    {
      return Long.MAX_VALUE;
    }
  }

  public TraceValue getValue(float vin, float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc) || Float.isNaN(vin))
    {
      return TraceValue.Undriven;
    }

    float highVoltageIn = getHighVoltageIn(vcc);
    if (vin >= highVoltageIn)
    {
      return TraceValue.High;
    }

    float lowVoltageIn = getLowVoltageIn(vcc);
    if (vin <= lowVoltageIn)
    {
      return TraceValue.Low;
    }

    return TraceValue.Unsettled;
  }

  public float calculateStartVoltage(float portVoltage, float vcc)
  {
    if (!Float.isNaN(portVoltage))
    {
      return portVoltage;
    }
    else
    {
      return getVoltageOut(false, vcc);
    }
  }

  public float getVoltsPerTimeLowToHigh(float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Float.POSITIVE_INFINITY;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.getVoltsPerTimeLowToHigh();
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateVoltage(higherConfiguration.getVoltsPerTimeLowToHigh(), lowerConfiguration.getVoltsPerTimeLowToHigh(), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return Float.POSITIVE_INFINITY;
        }
      }
    }
    else
    {
      return Float.POSITIVE_INFINITY;
    }
  }

  public float getVoltsPerTimeHighToLow(float vcc)
  {
    if ((vcc == 0) || Float.isNaN(vcc))
    {
      return Float.POSITIVE_INFINITY;
    }

    VoltageConfiguration higherConfiguration = getEqualOrHigherConfiguration(vcc);
    if (higherConfiguration != null)
    {
      if (higherConfiguration.vcc == vcc)
      {
        return higherConfiguration.getVoltsPerTimeHighToLow();
      }
      else
      {
        VoltageConfiguration lowerConfiguration = getLowerConfiguration(vcc);
        if (lowerConfiguration != null)
        {
          return linearInterpolateVoltage(higherConfiguration.getVoltsPerTimeHighToLow(), lowerConfiguration.getVoltsPerTimeHighToLow(), higherConfiguration.vcc, lowerConfiguration.vcc, vcc);
        }
        else
        {
          return Float.POSITIVE_INFINITY;
        }
      }
    }
    else
    {
      return Float.POSITIVE_INFINITY;
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

  long linearInterpolateTime(long higherTime, long lowerTime, float higherVCC, float lowerVCC, float vcc)
  {
    float configurationVCCDiff = higherVCC - lowerVCC;
    float vccDiff = vcc - lowerVCC;
    float fraction = vccDiff / configurationVCCDiff;

    long configurationHighTimeDiff = higherTime - lowerTime;
    return lowerTime + (long) (configurationHighTimeDiff * fraction);
  }

  public VoltageConfiguration getDefaultVoltageConfiguration(String logicLevel)
  {
    if (logicLevel.equals(TTL))
    {
      return ttlConfiguration;
    }
    else if ((logicLevel.equals(CMOS)))
    {
      return cmosConfiguration;
    }
    else
    {
      return null;
    }
  }

  public void sort()
  {
    Collections.sort(voltageConfigurations);
  }
}

