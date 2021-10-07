package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_AND
    extends OpCode
{
  public OpCode_AND(int mCode, InstructionCycles cycles)
  {
    super("AND", "Bitwise AND memory with A; result in A and update NZ.", mCode, cycles);
  }
}

