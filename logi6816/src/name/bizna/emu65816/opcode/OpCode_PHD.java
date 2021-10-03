package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHD
    extends OpCode
{
  public OpCode_PHD(int mCode, InstructionCycles cycles)
  {
    super("PHD", "Push Direct Register on Stack", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.push16Bit(cpu.getDirectPage());
    cpu.addToProgramAddressAndCycles(1, 4);
  }
}

