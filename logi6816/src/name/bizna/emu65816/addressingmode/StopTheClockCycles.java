package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.StopTheClock;

public class StopTheClockCycles
    extends InstructionCycles
{
  //19c
  public StopTheClockCycles(Consumer<Cpu65816> consumer)
  {
    super(StopTheClock,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), E(consumer), DONE()));
  }
}

