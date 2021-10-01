package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPHXPHYCycles
    extends InstructionCycles
{
  //22c
  public StackPHXPHYCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S()), new Execute2(), Write_DataHigh(), SP_dec(), new NoteOneIndices()),
          new BusCycle(Address(S()), new Execute1(), Write_DataLow(), SP_dec(), DONE()));
  }
}

