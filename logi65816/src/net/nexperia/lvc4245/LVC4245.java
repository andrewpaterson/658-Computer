package net.nexperia.lvc4245;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

import static net.nexperia.lvc4245.LVC4245Pins.PORT_A_INDEX;
import static net.nexperia.lvc4245.LVC4245Pins.PORT_B_INDEX;

public class LVC4245
    extends IntegratedCircuit<LVC4245Snapshot, LVC4245Pins>
{
  private PinValue direction;

  public LVC4245(String name, LVC4245Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    direction = getPins().getDir();

    if (direction.isError() || direction.isNotConnected())
    {
      getPins().setPortError(PORT_A_INDEX);
      getPins().setPortError(PORT_B_INDEX);
      return;
    }
    else if (direction.isUnknown())
    {
      getPins().setPortUnsettled(PORT_A_INDEX);
      getPins().setPortUnsettled(PORT_B_INDEX);
      return;
    }

    if (direction.isLow())  //B -> A
    {
      transmit(PORT_B_INDEX, PORT_A_INDEX);
    }
    else
    {
      transmit(PORT_A_INDEX, PORT_B_INDEX);
    }
  }

  private void transmit(int input, int output)
  {
    PinValue outputEnabledB = getPins().getOEB();

    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      getPins().setPortError(output);
    }
    else
    {
      transmit(!outputEnabledB.isHigh(), input, output);
    }
  }

  private void transmit(boolean outputEnabled, int input, int output)
  {
    if (outputEnabled)
    {
      BusValue readValue = getPins().getPortValue(input);
      if (readValue.isError() || readValue.isNotConnected())
      {
        getPins().setPortError(output);
      }
      else if (readValue.isUnknown())
      {
        getPins().setPortUnsettled(output);
      }
      else
      {
        long value = readValue.getValue();
        getPins().setPortValue(output, value);
      }
    }
    else
    {
      getPins().setPortHighImpedance(output);
    }
  }

  public LVC4245Snapshot createSnapshot()
  {
    return new LVC4245Snapshot(direction);
  }

  public void restoreFromSnapshot(LVC4245Snapshot snapshot)
  {
    direction = snapshot.direction;
  }

  public String getDirectionString()
  {
    if (direction != null)
    {
      if (direction.isHigh())
      {
        return "A->B";
      }
      else if (direction.isLow())
      {
        return "B->A";
      }
    }

    return "###";
  }

  public boolean isDirectionValid()
  {
    return direction != null && (direction.isHigh() || direction.isLow());
  }

  @Override
  public String getType()
  {
    return "Bus Transceiver";
  }
}

