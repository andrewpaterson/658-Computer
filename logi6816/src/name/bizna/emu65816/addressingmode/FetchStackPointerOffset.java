package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

public class StackPointerOffset
    extends DataBusCycleOperation
{
  public StackPointerOffset(boolean notMemoryLock)
  {
    super(true, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.setStackPointerOffset(cpu.getPinData());
  }
}

