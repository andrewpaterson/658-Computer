package net.logisim.integratedcircuits.nexperia.lvc541;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc541.LVC541;
import net.integratedcircuits.nexperia.lvc541.LVC541Pins;
import net.integratedcircuits.nexperia.lvc541.LVC541Snapshot;
import net.logisim.common.LogiBus;
import net.logisim.common.LogiPin;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc541.LVC541Factory.*;

public class LVC541LogisimPins
    extends LogisimPins<LVC541Snapshot, LVC541Pins, LVC541>
    implements LVC541Pins
{
  protected LogiBus[] portA;
  protected LogiBus[] portY;
  protected LogiPin[] oeb;

  public LVC541LogisimPins()
  {
    portA = new LogiBus[2];
    portY = new LogiBus[2];
    oeb = new LogiPin[2];
    portA[PORT_1_INDEX] = PORT_A[PORT_1_INDEX];
    portA[PORT_2_INDEX] = PORT_A[PORT_2_INDEX];
    portY[PORT_1_INDEX] = PORT_Y[PORT_1_INDEX];
    portY[PORT_2_INDEX] = PORT_Y[PORT_2_INDEX];
    oeb[PORT_1_INDEX] = PORT_OEB[PORT_1_INDEX];
    oeb[PORT_2_INDEX] = PORT_OEB[PORT_2_INDEX];
  }

  @Override
  public void setYError(int port)
  {
    setError(portY[port]);
  }

  @Override
  public void setYUnsettled(int port)
  {
  }

  @Override
  public void setYHighImpedance()
  {
    setHighImpedance(portY[port]);
  }

  @Override
  public void setYValue(long value)
  {
    setValue(portY[port], value);
  }

  @Override
  public PinValue getOEB(int port)
  {
    return getValue(oeb[port]);
  }

  @Override
  public BusValue getAValue()
  {
    return getValue(portA[port]);
  }
}

