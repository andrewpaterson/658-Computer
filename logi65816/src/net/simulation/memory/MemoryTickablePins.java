package net.simulation.memory;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;
import net.simulation.common.*;

public class MemoryTickablePins
    extends TickablePins<MemorySnapshot, MemoryTickablePins, Memory>
    implements Pins<MemorySnapshot, MemoryTickablePins, Memory>
{
  protected final Omniport addressBus;
  protected final Omniport dataBus;
  protected final Uniport writeEnableB;
  protected final Uniport outputEnableB;
  protected final Uniport chipEnableB;

  public MemoryTickablePins(Tickables tickables,
                            Bus addressBus,
                            Bus dataBus,
                            Trace writeEnableBTrace,
                            Trace outputEnabledBTrace,
                            Trace chipEnabledBTrace)
  {
    super(tickables);
    this.addressBus = new Omniport(this, "Address Bus", 16);
    this.dataBus = new Omniport(this, "Data Bus", 8);
    this.writeEnableB = new Uniport(this, "WEB");
    this.outputEnableB = new Uniport(this, "OEB");
    this.chipEnableB = new Uniport(this, "CEB");

    this.addressBus.connect(addressBus);
    this.dataBus.connect(dataBus);
    this.writeEnableB.connect(writeEnableBTrace);
    this.outputEnableB.connect(outputEnabledBTrace);
    this.chipEnableB.connect(chipEnabledBTrace);
  }

  public PinValue getWriteEnableB()
  {
    return getPinValue(writeEnableB);
  }

  public BusValue getAddress()
  {
    return getBusValue(addressBus);
  }

  public PinValue getOutputEnableB()
  {
    return getPinValue(outputEnableB);
  }

  public void setDataError()
  {
    dataBus.error();
  }

  public void setDataUnsettled()
  {
    dataBus.unset();
  }

  public void setDataValue(long data)
  {
    dataBus.writeAllPinsBool(data);
  }

  public void setDataHighImpedance()
  {
    dataBus.highImpedance();
  }

  public BusValue getData()
  {
    return getBusValue(dataBus);
  }

  public void updateSnapshot(long oldAddress, long oldValue)
  {
    snapshot.propagateWroteMemory = true;
    snapshot.oldAddress = oldAddress;
    snapshot.oldValue = oldValue;
  }

  public void updateSnapshot()
  {
    snapshot.propagateWroteMemory = false;
  }

  public PinValue getChipEnableB()
  {
    return getPinValue(chipEnableB);
  }
}

