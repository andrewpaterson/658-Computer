package net.integratedcircuits.nexperia.lsf0204;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LSF0204Pins
    extends Pins<LSF0204Snapshot, LSF0204Pins, LSF0204>
{
  BusValue getA();

  BusValue getB();

  PinValue getEN();

  void setA(long value);

  void setB(long value);
}

