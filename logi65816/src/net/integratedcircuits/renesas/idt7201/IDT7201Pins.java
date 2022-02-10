package net.integratedcircuits.renesas.idt7201;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;
import net.logisim.common.LogiBus;
import net.logisim.common.LogiPin;

public interface IDT7201Pins
    extends Pins<IDT7201Snapshot, IDT7201Pins, IDT7201>
{
  BusValue getInputD();

  void setOutputQ(long value);

  void setOutputHighImpedance();

  PinValue getWB();

  PinValue getRB();

  PinValue getRSB();

  PinValue getFLB_RTB();

  PinValue getXIB();

  void setFFB(boolean value);

  void setEFB(boolean value);

  void setXOB_HFB(boolean value);

  void setOutputError();

  void setEFBError();

  void setFFBError();

  void setHFBError();
}

