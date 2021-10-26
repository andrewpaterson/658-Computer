package net.nexperia.lvc4245;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

import static net.nexperia.lvc4245.LVC4245Pins.PORT_A_INDEX;
import static net.nexperia.lvc4245.LVC4245Pins.PORT_B_INDEX;

public class LVC4245
    implements IntegratedCircuit
{
  private final LVC4245Pins pins;
  private PinValue direction;

  public LVC4245(LVC4245Pins pins)
  {
    this.pins = pins;
    this.pins.setTransceiver(this);
  }

  public void tick()
  {
    direction = pins.getDir();

    if (direction.isError() || direction.isNotConnected())
    {
      pins.setPortError(PORT_A_INDEX);
      pins.setPortError(PORT_B_INDEX);
      return;
    }
    else if (direction.isUnknown())
    {
      pins.setPortUnsettled(PORT_A_INDEX);
      pins.setPortUnsettled(PORT_B_INDEX);
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
    PinValue outputEnabledB = pins.getOEB();

    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      pins.setPortError(output);
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
      BusValue readValue = pins.getPortValue(input);
      if (readValue.isError() || readValue.isNotConnected())
      {
        pins.setPortError(output);
      }
      else if (readValue.isUnknown())
      {
        pins.setPortUnsettled(output);
      }
      else
      {
        long value = readValue.getValue();
        pins.setPortValue(output, value);
      }
    }
    else
    {
      pins.setPortHighImpedance(output);
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
}

