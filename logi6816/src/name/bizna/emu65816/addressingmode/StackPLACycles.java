package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPLACycles
    extends InstructionCycles
{
  //22b
  public StackPLACycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc(), new Execute1(), new DoneInstructionIf8BitMemory()),
          new BusCycle(Address(S(), o(1)), Read_DataHigh(), SP_inc(), new Execute2(), new DoneInstructionIf16BitMemory()));
  }
}

