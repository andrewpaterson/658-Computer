package net.logisim.integratedcircuits.renesas.idt7201;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.renesas.idt7201.IDT7201;
import net.integratedcircuits.renesas.idt7201.IDT7201Pins;
import net.integratedcircuits.renesas.idt7201.IDT7201Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.renesas.idt7201.IDT7201Factory.*;

public class IDT7201LogisimPins
    extends LogisimPins<IDT7201Snapshot, IDT7201Pins, IDT7201>
    implements IDT7201Pins
{
  @Override
  public BusValue getInputD()
  {
    return getValue(PORT_D);
  }

  @Override
  public void setOutputQ(long value)
  {
    setValue(PORT_Q, value);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }

  @Override
  public PinValue getWB()
  {
    return getValue(PORT_WRITE);
  }

  @Override
  public PinValue getRB()
  {
    return getValue(PORT_READ);
  }

  @Override
  public PinValue getRSB()
  {
    return getValue(PORT_RESET);
  }

  @Override
  public PinValue getFLB_RTB()
  {
    return getValue(PORT_FL_RT);
  }

  @Override
  public PinValue getXIB()
  {
    return getValue(PORT_XI);
  }

  @Override
  public void setFFB(boolean value)
  {
    setValue(PORT_FULL, value);
  }

  @Override
  public void setEFB(boolean value)
  {
    setValue(PORT_EMPTY, value);
  }

  @Override
  public void setXOB_HFB(boolean value)
  {
    setValue(PORT_XO_HF, value);
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_D);
  }

  @Override
  public void setEFBError()
  {
    setError(PORT_EMPTY);
  }

  @Override
  public void setFFBError()
  {
    setError(PORT_FULL);
  }

  @Override
  public void setHFBError()
  {
    setError(PORT_XO_HF);
  }
}

