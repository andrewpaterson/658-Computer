package net.logisim.integratedcircuits.renesas.qs3253;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.renesas.qs3253.QS3253;
import net.integratedcircuits.renesas.qs3253.QS3253Pins;
import net.integratedcircuits.renesas.qs3253.QS3253Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.renesas.qs3253.QS3253Factory.*;

public class QS3253LogisimPins
    extends LogisimPins<QS3253Snapshot, QS3253Pins, QS3253>
    implements QS3253Pins
{
  @Override
  public BusValue getInputA()
  {
    return getValue(PORT_INPUT_A);
  }

  @Override
  public BusValue getInputB()
  {
    return getValue(PORT_INPUT_B);
  }

  @Override
  public void setYA(boolean value)
  {
    setValue(PORT_YA, value);
  }

  @Override
  public void setYB(boolean value)
  {
    setValue(PORT_YB, value);
  }

  @Override
  public BusValue getSelect()
  {
    return getValue(PORT_SELECT);
  }

  @Override
  public PinValue getEAB()
  {
    return getValue(PORT_ENABLE_AB);
  }

  @Override
  public PinValue getEBB()
  {
    return getValue(PORT_ENABLE_BB);
  }

  @Override
  public void setYAUnsettled()
  {
  }

  @Override
  public void setYBUnsettled()
  {
  }

  @Override
  public void setYAHighImpedance()
  {
    setHighImpedance(PORT_YA);
  }

  @Override
  public void setYBHighImpedance()
  {
    setHighImpedance(PORT_YB);
  }

  @Override
  public void setYAError()
  {
    setError(PORT_YA);
  }

  @Override
  public void setYBError()
  {
    setError(PORT_YB);
  }
}

