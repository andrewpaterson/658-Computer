package net.logisim.integratedcircuits.nexperia.lvc16244;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc16244.LVC16244;
import net.integratedcircuits.nexperia.lvc16244.LVC16244Pins;
import net.integratedcircuits.nexperia.lvc16244.LVC16244Snapshot;
import net.logisim.common.LogiBus;
import net.logisim.common.LogiPin;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc16244.LVC16244Factory.*;

public class LVC16244LogisimPins
    extends LogisimPins<LVC16244Snapshot, LVC16244Pins, LVC16244>
    implements LVC16244Pins
{
  protected LogiBus[] portA;
  protected LogiBus[] portY;
  protected LogiPin[] oeb;

  public LVC16244LogisimPins()
  {
    portA = new LogiBus[4];
    portY = new LogiBus[4];
    oeb = new LogiPin[4];
    portA[PORT_1_INDEX] = PORT_A[PORT_1_INDEX];
    portA[PORT_2_INDEX] = PORT_A[PORT_2_INDEX];
    portA[PORT_3_INDEX] = PORT_A[PORT_3_INDEX];
    portA[PORT_4_INDEX] = PORT_A[PORT_4_INDEX];
    portY[PORT_1_INDEX] = PORT_Y[PORT_1_INDEX];
    portY[PORT_2_INDEX] = PORT_Y[PORT_2_INDEX];
    portY[PORT_3_INDEX] = PORT_Y[PORT_3_INDEX];
    portY[PORT_4_INDEX] = PORT_Y[PORT_4_INDEX];
    oeb[PORT_1_INDEX] = PORT_OEB[PORT_1_INDEX];
    oeb[PORT_2_INDEX] = PORT_OEB[PORT_2_INDEX];
    oeb[PORT_3_INDEX] = PORT_OEB[PORT_3_INDEX];
    oeb[PORT_4_INDEX] = PORT_OEB[PORT_4_INDEX];
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
  public void setYHighImpedance(int port)
  {
    setHighImpedance(portY[port]);
  }

  @Override
  public void setYValue(int port, long value)
  {
    setValue(portY[port], value);
  }

  @Override
  public PinValue getOEB(int port)
  {
    return getValue(oeb[port]);
  }

  @Override
  public BusValue getAValue(int port)
  {
    return getValue(portA[port]);
  }
}

