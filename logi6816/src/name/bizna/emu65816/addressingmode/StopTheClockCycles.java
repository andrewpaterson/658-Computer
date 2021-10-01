package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StopTheClock;

public class StopTheClockCycles
    extends InstructionCycles
{
  //19c
  public StopTheClockCycles()
  {
    super(StopTheClock,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), new Execute1(), DONE()));
  }
}

