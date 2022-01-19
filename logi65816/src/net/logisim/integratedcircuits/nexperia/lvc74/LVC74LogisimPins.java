package net.logisim.integratedcircuits.nexperia.lvc74;

import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc74.LVC74;
import net.integratedcircuits.nexperia.lvc74.LVC74Pins;
import net.integratedcircuits.nexperia.lvc74.LVC74Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc74.LVC74Factory.*;

public class LVC74LogisimPins
    extends LogisimPins<LVC74Snapshot, LVC74Pins, LVC74>
    implements LVC74Pins
{
  @Override
  public PinValue getCPValue(int flipFlop)
  {
    return getValue(PORT_CP[flipFlop]);
  }

  @Override
  public PinValue getRDValue(int flipFlop)
  {
    return getValue(PORT_RD[flipFlop]);
  }

  @Override
  public PinValue getDValue(int flipFlop)
  {
    return getValue(PORT_D[flipFlop]);
  }

  @Override
  public PinValue getSDValue(int flipFlop)
  {
    return getValue(PORT_SD[flipFlop]);
  }

  @Override
  public void setValue(int flipFlop, boolean value)
  {
    setValue(PORT_Q[flipFlop], value);
    setValue(PORT_QB[flipFlop], !value);
  }
}

