package name.bizna.bus;

import name.bizna.bus.logic.ComplexTickable;
import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;

public class TickableCpu
    implements ComplexTickable
{
  private final Cpu65816 cpu;

  public TickableCpu(Cpu65816 cpu)
  {
    this.cpu = cpu;
  }

  @Override
  public boolean propagate()
  {
    Pins65816 pins = cpu.getPins();

    cpu.tick();

    boolean settled = true;

    return settled;
  }

  public boolean isStopped()
  {
    return cpu.isStopped();
  }

  public void dump()
  {
    cpu.dump();
  }

  @Override
  public void undoInternalPropagationChange()
  {

  }
}

