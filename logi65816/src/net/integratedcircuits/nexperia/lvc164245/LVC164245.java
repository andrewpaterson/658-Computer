package net.integratedcircuits.nexperia.lvc164245;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

import static net.integratedcircuits.nexperia.lvc164245.LVC164245Pins.PORT_A_INDEX;
import static net.integratedcircuits.nexperia.lvc164245.LVC164245Pins.PORT_B_INDEX;

public class LVC164245
    extends IntegratedCircuit<LVC164245Snapshot, LVC164245Pins>
{
  public static final String TYPE = "Bus Transceiver";

  private final PinValue[] direction;

  public LVC164245(String name, LVC164245Pins pins)
  {
    super(name, pins);
    direction = new PinValue[2];
  }

  public void tick()
  {
    tickPort(0);
    tickPort(1);
  }

  private void tickPort(int port)
  {
    direction[port] = getPins().getDir(port);

    if (direction[port].isError() || direction[port].isNotConnected())
    {
      getPins().setPortError(port, PORT_A_INDEX);
      getPins().setPortError(port, PORT_B_INDEX);
      return;
    }
    else if (direction[port].isUnknown())
    {
      getPins().setPortUnsettled(port, PORT_A_INDEX);
      getPins().setPortUnsettled(port, PORT_B_INDEX);
      return;
    }

    if (direction[port].isLow())  //B -> A
    {
      transmit(port, PORT_B_INDEX, PORT_A_INDEX);
    }
    else
    {
      transmit(port, PORT_A_INDEX, PORT_B_INDEX);
    }
  }

  private void transmit(int port, int input, int output)
  {
    PinValue outputEnabledB = getPins().getOEB(port);

    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      getPins().setPortError(port, output);
    }
    else
    {
      transmit(port, !outputEnabledB.isHigh(), input, output);
    }
  }

  private void transmit(int port, boolean outputEnabled, int input, int output)
  {
    if (outputEnabled)
    {
      BusValue readValue = getPins().getPortValue(port, input);
      if (readValue.isError() || readValue.isNotConnected())
      {
        getPins().setPortError(port, output);
      }
      else if (readValue.isUnknown())
      {
        getPins().setPortUnsettled(port, output);
      }
      else
      {
        long value = readValue.getValue();
        getPins().setPortValue(port, output, value);
      }
    }
    else
    {
      getPins().setPortHighImpedance(port, output);
    }
  }

  public LVC164245Snapshot createSnapshot()
  {
    return new LVC164245Snapshot(direction[0], direction[1]);
  }

  public void restoreFromSnapshot(LVC164245Snapshot snapshot)
  {
    direction[0] = snapshot.direction1;
    direction[1] = snapshot.direction2;
  }

  public String getDirectionString(int port)
  {
    if (direction[port] != null)
    {
      if (direction[port].isHigh())
      {
        return "A->B";
      }
      else if (direction[port].isLow())
      {
        return "B->A";
      }
    }

    return "###";
  }

  public boolean isDirectionValid(int port)
  {
    return direction[port] != null && (direction[port].isHigh() || direction[port].isLow());
  }
  public PinValue getDirection(int port)
  {
    return direction[port];
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

