package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackRelativeSTACycles
    extends InstructionCycles
{
  public StackRelativeSTACycles()
  {
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S(), D0()), SP_inc(), new Execute1(), Write_DataLow(), new DoneInstructionIf8BitMemory()),
          new BusCycle(Address(S(), D0(), o(1)), new Execute2(), Write_DataHigh(), new DoneInstructionIf16BitMemory()));
  }
}

