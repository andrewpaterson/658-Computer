package net.integratedcircuits.nexperia.lv165;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LV165Pins
    extends Pins<LV165Snapshot, LV165Pins, LV165>
{
  PinValue getPLB();

  PinValue getCP();

  BusValue getD();

  PinValue getCEB();

  PinValue getDS();

  void setQValue(boolean value);
}

