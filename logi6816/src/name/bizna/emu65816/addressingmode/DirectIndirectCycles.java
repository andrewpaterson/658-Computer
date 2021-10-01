package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirect;

public class DirectIndirectCycles
    extends InstructionCycles
{
  public DirectIndirectCycles(boolean read)
  {
    //12
    super(DirectIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

