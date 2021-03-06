package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_PER
    extends OpCode
{
  public OpCode_PER(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    Address opCodeDataAddress = cpu.getAddressOfOpCodeData(getAddressingMode());
    int opCodeSize = 3;
    int operand = cpu.readTwoBytes(opCodeDataAddress);
    int sum = toShort (operand + opCodeSize + cpu.getProgramAddress().getOffset());
    cpu.getStack().push16Bit(sum);
    cpu.addToProgramAddressAndCycles(3, 6);
  }
}

