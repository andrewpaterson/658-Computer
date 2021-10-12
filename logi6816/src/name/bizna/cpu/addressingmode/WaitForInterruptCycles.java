package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.WaitForInterrupt;

public class WaitForInterruptCycles
    extends InstructionCycles
{
  public WaitForInterruptCycles(Executor<Cpu65816> consumer)
  {
    super(WaitForInterrupt,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), new WaitOperation(), E(consumer), DONE()));
  }
}

