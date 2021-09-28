package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Binary.getLowByte;

public class NoteFour
    extends DataBusCycleOperation
{
  private boolean nextWillRead;

  public NoteFour(boolean notMemoryLock, boolean nextWillRead)
  {
    super(false, false, notMemoryLock, true, true);
    this.nextWillRead = nextWillRead;
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return !(getLowByte(cpu.getAddress().getOffset()) + getLowByte(cpu.getY()) > 0xFF ||
             !nextWillRead ||
             cpu.isIndex16Bit());
  }
}

