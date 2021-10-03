package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PEA
    extends OpCode
{
  public OpCode_PEA(int mCode, InstructionCycles cycles)
  {
    super("PEA", "Push Absolute Address", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.getData();
    cpu.push16Bit(operand);
    cpu.addToProgramAddressAndCycles(3, 5);
  }
}
