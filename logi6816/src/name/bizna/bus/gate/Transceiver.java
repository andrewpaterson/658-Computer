package name.bizna.bus.gate;

import name.bizna.bus.common.*;
import name.bizna.util.EmulatorException;

public class Transceiver
    extends Tickable
{
  protected Omniport in;
  protected Omniport out;
  protected Uniport outputEnableB;

  public Transceiver(Tickables tickables, String name, int width, Bus inBus, Bus outBus, Trace outputEnabledBTrace)
  {
    super(tickables, name);
    this.in = new Omniport(this, "In", width);
    this.out = new Omniport(this, "Out", width);
    this.outputEnableB = new Uniport(this, "OEB");

    in.connect(inBus);
    out.connect(outBus);
    outputEnableB.connect(outputEnabledBTrace);
  }

  @Override
  public void propagate()
  {
    TraceValue outputEnabledBValue = outputEnableB.read();
    if (outputEnabledBValue.isError())
    {
      out.error();
    }
    else if (outputEnabledBValue.isUnsettled())
    {
      out.unset();
    }
    else if (outputEnabledBValue.isNotConnected() || outputEnabledBValue.isLow())
    {
      out.highImpedance();
    }
    else if (outputEnabledBValue.isHigh())
    {
      TraceValue readValue = in.read();
      if (readValue.isError() || readValue.isNotConnected())
      {
        out.error();
      }
      else if (readValue.isUnsettled())
      {
        out.unset();
      }
      else
      {
        long value = in.getPinsAsBoolAfterRead();
        out.writeAllPinsBool(value);
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

