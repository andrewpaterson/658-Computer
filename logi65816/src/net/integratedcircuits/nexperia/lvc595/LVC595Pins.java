package net.integratedcircuits.nexperia.lvc595;

import net.common.PinValue;
import net.common.Pins;

public interface LVC595Pins
    extends Pins<LVC595Snapshot, LVC595Pins, LVC595>
{
  void setQ(int value);

  void setQ7S(boolean value);

  PinValue getMRB();

  PinValue getSHCP();

  PinValue getSTCP();

  PinValue getOEB();

  PinValue getDS();

  void setQError();

  void setQ7SError();

  void setQImpedance();
}

