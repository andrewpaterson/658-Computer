package net.nexperia.lvc4245;

import net.common.BusValue;
import net.common.PinValue;

import static net.nexperia.lvc4245.LVC4245Pins.PORT_A_INDEX;
import static net.nexperia.lvc4245.LVC4245Pins.PORT_B_INDEX;

public class LVC4245
{
  private final LVC4245Pins pins;

  public LVC4245(LVC4245Pins pins)
  {
    this.pins = pins;
    this.pins.setTransceiver(this);
  }

  public void tick()
  {
    PinValue dir = pins.getDir();

    if (dir.isError() || dir.isNotConnected())
    {
      pins.setPortError(PORT_A_INDEX);
      pins.setPortError(PORT_B_INDEX);
      return;
    }
    else if (dir.isUnknown())
    {
      pins.setPortUnsettled(PORT_A_INDEX);
      pins.setPortUnsettled(PORT_B_INDEX);
      return;
    }

    if (dir.isLow())  //B -> A
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
    PinValue outputEnabledBValue = pins.getOEB();

    if (outputEnabledBValue.isError() || outputEnabledBValue.isNotConnected())
    {
      pins.setPortError(output);
    }
    else
    {
      transmit(!outputEnabledBValue.isHigh(), input, output);
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
        pins.setPortValue(output, readValue.getValue());
      }
    }
    else
    {
      pins.setPortHighImpedance(output);
    }
  }
}

