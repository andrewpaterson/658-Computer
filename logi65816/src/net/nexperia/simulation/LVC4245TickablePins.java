package net.nexperia.simulation;

import net.nexperia.lvc4245.LVC4245;
import net.nexperia.lvc4245.LVC4245Pins;
import net.simulation.common.*;
import net.util.EmulatorException;

public class LVC4245TickablePins
    extends Tickable
    implements LVC4245Pins
{
  protected LVC4245 transceiver;

  protected Omniport aPort;
  protected Omniport bPort;
  protected Uniport outputEnableB;
  protected Uniport dir;

  public LVC4245TickablePins(Tickables tickables,
                             String name,
                             int width,
                             Bus aBus,
                             Bus bBus,
                             Trace outputEnabledBTrace,
                             Trace dirTrace)
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
  public void setTransceiver(LVC4245 transceiver)
  {
    this.transceiver = transceiver;
  }

  @Override
  public void startPropagation()
  {
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

    if (dirValue.isLow())  //B -> A
    {
      transmit(outputEnabledBValue, bPort, aPort);
    }
    else if (dirValue.isHigh())  //A -> B
    {
      transmit(outputEnabledBValue, aPort, bPort);
    }
    else
    {
      throw new EmulatorException(getDescription() + " connections are in an impossible state.");
    }
  }

  @Override
  public void donePropagation()
  {
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
    else
    {
      transmit(outputEnabledBValue.isNotConnected() || outputEnabledBValue.isLow(), input, output);
    }
  }

  private void transmit(boolean outputEnabled, Omniport input, Omniport output)
  {
    if (outputEnabled)
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
      output.highImpedance();
    }
  }

  @Override
  public String getType()
  {
    return "Bus Transceiver";
  }
}

