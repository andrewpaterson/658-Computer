package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Executor;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.StopTheClock;

public class StopTheClockCycles
    extends InstructionCycles
{
  //19c
  public StopTheClockCycles(Executor<Cpu65816> consumer)
  {
    super(StopTheClock,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), E(consumer), DONE()));
  }
}

