package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PEA
    extends OpCode
{
  public OpCode_PEA(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.readTwoBytes(opCodeDataAddress);
    cpu.getStack().push16Bit(operand);
    cpu.addToProgramAddressAndCycles(3, 5);
  }
}
