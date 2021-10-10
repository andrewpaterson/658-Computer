package name.bizna.bus.common;

public enum TransmissionState
{
  Input,
  Output,
  Impedance,
  NotSet;

  public boolean isOutput()
  {
    return this == Output;
  }

  public boolean isInput()
  {
    return this == Input;
  }

  public boolean isNotSet()
  {
    return this == NotSet;
  }

  public boolean isImpedance()
  {
    return this == Impedance;
  }
}

