package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.opcode.OpCode_PHA;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPHACycles
    extends InstructionCycles
{
  //22c
  public StackPHACycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), new Execute2(), Write_DataHigh(), SP_dec(), new NoteOne()),
          new BusCycle(Address(S()), new Execute1(), Write_DataLow(), SP_dec(), DONE()));
  }
}

