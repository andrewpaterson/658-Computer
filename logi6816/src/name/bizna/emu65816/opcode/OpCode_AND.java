package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_AND
    extends OpCode
{
  public OpCode_AND(int mCode, InstructionCycles cycles)
  {
    super("AND", "Bitwise AND memory with A; result in A and update NZ.", mCode, cycles);
  }
}

