package net.integratedcircuits.maxim.ds1813;

import net.common.PinValue;
import net.common.Pins;

public interface DS1813Pins
    extends Pins<DS1813Snapshot, DS1813Pins, DS1813>
{
  void setOut();

  PinValue getReset();

  void startCounter();
}

