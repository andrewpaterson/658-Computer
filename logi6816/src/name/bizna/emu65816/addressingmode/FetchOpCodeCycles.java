package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.AddressingMode.OpCode;
import static name.bizna.emu65816.AddressingMode.Stack;

public class FetchOpCodeCycles
    extends InstructionCycles
{
  //22b
  public FetchOpCodeCycles()
  {
    super(OpCode,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()));
  }
}

