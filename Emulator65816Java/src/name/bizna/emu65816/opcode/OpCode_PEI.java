package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_PEI
    extends OpCode
{
  public OpCode_PEI(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int operand = cpu.readTwoBytes(opCodeDataAddress);
    cpu.getStack().push16Bit(operand);
    int opCycles = Binary.lower8BitsOf(cpu.getD()) != 0 ? 1 : 0;
    cpu.addToProgramAddressAndCycles(2, 6 + opCycles);
  }
}
