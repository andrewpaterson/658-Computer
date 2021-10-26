package net.simulation.specialised;

import net.common.IntegratedCircuit;
import net.simulation.common.Tickable;
import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

public class EconoReset
    extends Tickable
    implements IntegratedCircuit
{
  private final Uniport out;
  private int count;

  private EconoResetSnapshot snapshot;

  public EconoReset(Tickables tickables, String name, Trace out)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(out);
    this.count = 4;
    this.snapshot = null;
  }

  @Override
  public void startPropagation()
  {
    snapshot = createSnapshot();
  }

  private EconoResetSnapshot createSnapshot()
  {
    return new EconoResetSnapshot(count);
  }

  private void restoreFromSnapshot(EconoResetSnapshot snapshot)
  {
    count = snapshot.count;
  }

  public void undoPropagation()
  {
    if (snapshot != null)
    {
      restoreFromSnapshot(snapshot);
    }
  }

  @Override
  public void donePropagation()
  {
    snapshot = null;
  }

  @Override
  public String getType()
  {
    return "Econo Reset";
  }

  @Override
  protected IntegratedCircuit getIntegratedCircuit()
  {
    return this;
  }

  @Override
  public void tick()
  {
    if (count > 0)
    {
      count--;
      out.writeBool(false);
    }
    else
    {
      out.writeBool(true);
    }
  }
}

