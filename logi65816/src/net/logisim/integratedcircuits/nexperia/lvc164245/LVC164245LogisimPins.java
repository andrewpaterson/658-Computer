package net.logisim.integratedcircuits.nexperia.lvc164245;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc164245.LVC164245;
import net.integratedcircuits.nexperia.lvc164245.LVC164245Pins;
import net.integratedcircuits.nexperia.lvc164245.LVC164245Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc164245.LVC164245Factory.PORT_DIR;

public class LVC164245LogisimPins
    extends LogisimPins<LVC164245Snapshot, LVC164245Pins, LVC164245>
    implements LVC164245Pins
{
  protected int[][] ports;
  protected int[] dir;
  protected int[] oeb;

  public LVC164245LogisimPins()
  {
    ports = new int[2][2];
    ports[0][PORT_A_INDEX] = LVC164245Factory.PORT_A[0];
    ports[0][PORT_B_INDEX] = LVC164245Factory.PORT_B[0];
    ports[1][PORT_A_INDEX] = LVC164245Factory.PORT_A[1];
    ports[1][PORT_B_INDEX] = LVC164245Factory.PORT_B[1];

    dir = new int[2];
    dir[0] = PORT_DIR[0];
    dir[1] = PORT_DIR[1];

    oeb = new int[2];
    oeb[0] = LVC164245Factory.PORT_OEB[0];
    oeb[1] = LVC164245Factory.PORT_OEB[1];
  }

  @Override
  public void setPortError(int port, int index)
  {
    instanceState.setPort(ports[port][index], Value.createError(BitWidth.create(8)), 2 + index);
  }

  @Override
  public void setPortUnsettled(int port, int index)
  {
  }

  @Override
  public void setPortHighImpedance(int port, int index)
  {
    instanceState.setPort(ports[port][index], Value.createUnknown(BitWidth.create(8)), 2 + index);
  }

  @Override
  public PinValue getDir(int port)
  {
    PinValue pinValue = getPinValue(PORT_DIR[port]);
    PinValue direction = getIntegratedCircuit().getDirection(port);
    if (direction != pinValue)
    {
      if (pinValue.isLow())
      {
        instanceState.setPort(ports[port][PORT_B_INDEX], Value.createUnknown(BitWidth.create(8)), 4);
      }
      else if (pinValue.isHigh())
      {
        instanceState.setPort(ports[port][PORT_A_INDEX], Value.createUnknown(BitWidth.create(8)), 4);
      }
    }

    return pinValue;
  }

  @Override
  public PinValue getOEB(int port)
  {
    return getPinValue(oeb[port]);
  }

  @Override
  public BusValue getPortValue(int port, int index)
  {
    return getBusValue(ports[port][index], 8, 2 + index);
  }

  @Override
  public void setPortValue(int port, int index, long value)
  {
    instanceState.setPort(ports[port][index], Value.createKnown(BitWidth.create(8), value), 2 + index);
  }
}

