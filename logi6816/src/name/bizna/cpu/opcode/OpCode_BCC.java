package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_BCC
    extends OpCode
{
  public OpCode_BCC(int mCode, InstructionCycles cycles)
  {
    super("BCC", "Branch on Carry Clear (C=0)", mCode, cycles);
  }
}

