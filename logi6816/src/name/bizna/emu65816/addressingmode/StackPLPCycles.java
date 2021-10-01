package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPLPCycles
    extends InstructionCycles
{
  //22b
  public StackPLPCycles()
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), o(1)), IO()),
          new BusCycle(Address(S(), o(1)), Read_DataLow(), SP_inc(), new Execute1(), DONE()));
  }
}

