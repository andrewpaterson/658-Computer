package net.integratedcircuits.nexperia.lsf0102;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LSF0102Pins
    extends Pins<LSF0102Snapshot, LSF0102Pins, LSF0102>
{
  BusValue getA();

  BusValue getB();

  PinValue getEN();

  void setA(long value);

  void setB(long value);
}

