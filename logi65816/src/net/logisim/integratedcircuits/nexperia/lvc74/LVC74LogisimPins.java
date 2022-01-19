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
  private PinValue[] dValue;

  public LVC74LogisimPins()
  {
    dValue = new PinValue[2];
  }

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
  public PinValue getPrevDValue(int flipFlop)
  {
    return dValue[flipFlop];
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

  @Override
  public long getTickCount()
  {
    return instanceState.getTickCount();
  }

  @Override
  public void donePropagation()
  {
    dValue[0] = getDValue(0);
    dValue[1] = getDValue(1);
  }
}

