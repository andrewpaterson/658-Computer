package name.bizna.bus.gate;

import name.bizna.bus.common.*;
import name.bizna.util.EmulatorException;

public class Transceiver
    extends Tickable
{
  protected Omniport aPort;
  protected Omniport bPort;
  protected Uniport outputEnableB;
  protected Uniport dir;

  public Transceiver(Tickables tickables, String name, int width, Bus aBus, Bus bBus, Trace outputEnabledBTrace, Trace dirTrace)
  {
    super(tickables, name);
    this.aPort = new Omniport(this, "A", width);
    this.bPort = new Omniport(this, "B", width);
    this.outputEnableB = new Uniport(this, "OEB");
    this.dir = new Uniport(this, "DIR");

    aPort.connect(aBus);
    bPort.connect(bBus);
    outputEnableB.connect(outputEnabledBTrace);
    dir.connect(dirTrace);
  }

  @Override
  public void propagate()
  {
    TraceValue outputEnabledBValue = outputEnableB.read();
    TraceValue dirValue = dir.read();
    if (dirValue.isError() || dirValue.isNotConnected())
    {
      bPort.error();
      aPort.error();
      return;
    }
    else if (dirValue.isUnsettled())
    {
      bPort.unset();
      aPort.unset();
      return;
    }

    if (dirValue.isLow())  //A -> B
    {
      transmit(outputEnabledBValue, aPort, bPort);
    }
    else if (dirValue.isHigh())  //B -> A
    {
      transmit(outputEnabledBValue, bPort, aPort);
    }
    else
    {
      throw new EmulatorException(getDescription() + " connections are in an impossible state.");
    }
  }

  private void transmit(TraceValue outputEnabledBValue, Omniport input, Omniport output)
  {
    if (outputEnabledBValue.isError())
    {
      output.error();
    }
    else if (outputEnabledBValue.isUnsettled())
    {
      output.unset();
    }
    else if (outputEnabledBValue.isNotConnected() || outputEnabledBValue.isLow())
    {
      output.highImpedance();
    }
    else if (outputEnabledBValue.isHigh())
    {
      TraceValue readValue = input.read();
      if (readValue.isError() || readValue.isNotConnected())
      {
        output.error();
      }
      else if (readValue.isUnsettled())
      {
        output.unset();
      }
      else
      {
        long value = input.getPinsAsBoolAfterRead();
        output.writeAllPinsBool(value);
      }
    }
    else
    {
      throw new EmulatorException(getDescription() + " connections are in an impossible state.");
    }
  }

  @Override
  public String getType()
  {
    return "Bus Transceiver";
  }
}

