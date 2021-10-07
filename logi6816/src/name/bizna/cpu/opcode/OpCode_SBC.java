package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_SBC
    extends OpCode
{
  public OpCode_SBC(int mCode, InstructionCycles cycles)
  {
    super("SBC", "Subtract memory and carry from A; result in A and update NZC.", mCode, cycles);
  }
}

