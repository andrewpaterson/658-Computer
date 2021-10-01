package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectCycles
    extends InstructionCycles
{
  //10a
  public DirectCycles(boolean read)
  {
    super(Direct,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DP(), D0(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

