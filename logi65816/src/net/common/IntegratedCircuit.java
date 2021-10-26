package net.common;

@SuppressWarnings({"rawtypes", "unchecked"})
public abstract class IntegratedCircuit<SNAPSHOT extends Snapshot, PINS extends Pins>
{
  private final PINS pins;

  public IntegratedCircuit(PINS pins)
  {
    this.pins = pins;
    this.pins.setIntegratedCircuit(this);
  }

  public PINS getPins()
  {
    return pins;
  }

  public void startTick()
  {
  }

  public abstract void tick();

  public SNAPSHOT createSnapshot()
  {
    return null;
  }

  public void restoreFromSnapshot(SNAPSHOT snapshot)
  {
  }
}

