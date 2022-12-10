package net.logicim.domain.common.propagation;

import net.logicim.common.SimulatorException;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.logicim.domain.common.LongTime.nanosecondsToTime;

public class FamilyVoltageConfigurationStore
{
  protected static FamilyVoltageConfigurationStore instance;

  protected Map<String, VoltageConfiguration> voltageConfigurations;

  public FamilyVoltageConfigurationStore()
  {
    voltageConfigurations = new LinkedHashMap<>();
  }

  public static FamilyVoltageConfigurationStore getInstance()
  {
    if (instance == null)
    {
      instance = new FamilyVoltageConfigurationStore();
      instance.add(new VoltageConfiguration("LVC", 3.3f,
                                            0.8f, 2.0f,
                                            0.2f, 3.1f,
                                            ns(3.0), ns(3.0)));  //From Nexperia 74LVC541
      instance.add(new VoltageConfiguration("HCT", 5.0f,
                                            0.8f, 2.0f,
                                            0.2f, 3.1f,
                                            ns(12.0), ns(12.0),
                                            ns(21), ns(21)));    //From Nexperia 74HCT541
    }
    return instance;
  }

  public static VoltageConfiguration get(String family)
  {

  }

  protected static int ns(double nanoseconds)
  {
    return nanosecondsToTime(nanoseconds);
  }

  public void add(VoltageConfiguration voltageConfiguration)
  {
    String family = voltageConfiguration.getFamily();
    VoltageConfiguration existingConfiguration = voltageConfigurations.get(family);
    if (existingConfiguration != null)
    {
      throw new SimulatorException("Voltage configuration for [%s] already added.", family);
    }
    else
    {
      voltageConfigurations.put(family, voltageConfiguration);
    }
  }
}

