package net.logicim.domain.common.propagation;

import net.logicim.domain.common.Timeline;
import net.logicim.domain.common.port.Port;
import net.logicim.domain.common.trace.TraceValue;

public class TestVoltageConfiguration
    extends VoltageConfigurationSource
{
  protected VoltageConfiguration voltageConfiguration;

  public TestVoltageConfiguration(VoltageConfiguration voltageConfiguration)
  {
    this.voltageConfiguration = voltageConfiguration;
  }

  public TestVoltageConfiguration(float vcc,
                                  float lowVoltageIn,
                                  float highVoltageIn,
                                  float lowVoltageOut,
                                  float highVoltageOut,
                                  int highToLowPropagation,
                                  int lowToHighPropagation)
  {
    voltageConfiguration = new VoltageConfiguration(vcc,
                                                    lowVoltageIn,
                                                    highVoltageIn,
                                                    lowVoltageOut,
                                                    highVoltageOut,
                                                    highToLowPropagation,
                                                    lowToHighPropagation);
  }

  @Override
  public VoltageConfiguration get(float vcc)
  {
    return voltageConfiguration;
  }

  @Override
  public float getLowVoltageIn(float vcc)
  {
    return voltageConfiguration.getLowVoltageIn();
  }

  @Override
  public float getHighVoltageIn(float vcc)
  {
    return voltageConfiguration.getHighVoltageIn();
  }

  @Override
  public float getMidVoltageOut(float vcc)
  {
    return voltageConfiguration.getMidVoltageOut();
  }

  @Override
  public void createOutputEvent(Timeline timeline, Port port, float voltageOut)
  {
    voltageConfiguration.createOutputEvent(timeline, port, voltageOut);
  }

  @Override
  public float getVoltageOut(boolean value, float vcc)
  {
    return voltageConfiguration.getVoltageOut(value);
  }

  @Override
  public TraceValue getValue(float voltage, float vcc)
  {
    return voltageConfiguration.getValue(voltage);
  }

  @Override
  public float calculateStartVoltage(float portVoltage, float vcc)
  {
    return voltageConfiguration.calculateStartVoltage(portVoltage);
  }

  @Override
  public float getVoltsPerTimeLowToHigh(float vcc)
  {
    return voltageConfiguration.getVoltsPerTimeLowToHigh();
  }

  @Override
  public float getVoltsPerTimeHighToLow(float vcc)
  {
    return voltageConfiguration.getVoltsPerTimeHighToLow();
  }
}
