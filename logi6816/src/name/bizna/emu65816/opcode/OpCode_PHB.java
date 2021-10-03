package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(int mCode, InstructionCycles cycles)
  {
    super("PHB", "Push Data Bank Register on Stack", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.push8Bit(cpu.getDataBank());
    cpu.addToProgramAddressAndCycles(1, 3);
  }
}

