package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_MVP
    extends OpCode
{
  public OpCode_MVP(int mCode, InstructionCycles cycles)
  {
    super("MVP", "Block move previous...", mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (!cpu.blockMovePrevious())
    {
      cpu.doneInstruction();
    }
  }
}

