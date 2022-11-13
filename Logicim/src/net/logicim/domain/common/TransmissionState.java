package net.logicim.domain.common;

import net.logicim.common.util.StringUtil;

public enum TransmissionState
{
  Input,
  Output,
  Impedance;

  public boolean isOutput()
  {
    return this == Output;
  }

  public boolean isInput()
  {
    return this == Input;
  }

  public boolean isImpedance()
  {
    return this == Impedance;
  }

  public String toEnumString()
  {
    return StringUtil.toEnumString(this);
  }
}

